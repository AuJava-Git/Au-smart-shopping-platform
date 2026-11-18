package cn.fly.smartshopping.service.impl;

import cn.fly.smartshopping.DTO.AdminOrderResponse;
import cn.fly.smartshopping.DTO.OrderDetailResponse;
import cn.fly.smartshopping.common.OrderStatus;
import cn.fly.smartshopping.entity.*;
import cn.fly.smartshopping.mapper.*;
import cn.fly.smartshopping.service.OrderService;
import cn.fly.smartshopping.service.PointService;
import cn.fly.smartshopping.mapper.UserAddressMapper;
import cn.fly.smartshopping.entity.UserAddress;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ShoppingCartMapper shoppingCartMapper;
    private final ProductMapper productMapper;
    private final UserMapper userMapper;
    private final PointService pointService;
    private final UserAddressMapper addressMapper;

    public OrderServiceImpl(OrderMapper orderMapper, OrderItemMapper orderItemMapper,
                            ShoppingCartMapper shoppingCartMapper, ProductMapper productMapper,
                            UserMapper userMapper, PointService pointService, UserAddressMapper addressMapper) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.shoppingCartMapper = shoppingCartMapper;
        this.productMapper = productMapper;
        this.userMapper = userMapper;
        this.pointService = pointService;
        this.addressMapper = addressMapper;
    }

    @Override
    @Transactional
    public Order createOrder(Long userId, Long addressId) {
        LambdaQueryWrapper<ShoppingCart> cw = new LambdaQueryWrapper<>();
        cw.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> carts = shoppingCartMapper.selectList(cw);
        if (carts.isEmpty()) throw new IllegalArgumentException("购物车为空，无法提交订单");

        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        if (addressId == null) {
            UserAddress a = addressMapper.selectOne(new LambdaQueryWrapper<UserAddress>().eq(UserAddress::getUserId, userId).eq(UserAddress::getIsDefault, true).last("LIMIT 1"));
            if (a != null) addressId = a.getId();
        } else if (addressMapper.selectOne(new LambdaQueryWrapper<UserAddress>().eq(UserAddress::getId, addressId).eq(UserAddress::getUserId, userId)) == null) {
            throw new IllegalArgumentException("收货地址不存在或不属于当前用户");
        }
        if (addressId == null) throw new IllegalArgumentException("请先添加收货地址");
        order.setAddressId(addressId);
        order.setTotalAmount(BigDecimal.ZERO);
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.insert(order);

        BigDecimal total = BigDecimal.ZERO;
        for (ShoppingCart cart : carts) {
            Product product = productMapper.selectById(cart.getProductId());
            if (product == null) throw new IllegalArgumentException("商品不存在：" + cart.getProductId());
            if (product.getPrice() == null || product.getStock() == null) {
                throw new IllegalArgumentException("商品数据异常：" + product.getName());
            }

            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity()));
            OrderItem item = new OrderItem();
            item.setOrderId(order.getId());
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setProductPrice(product.getPrice());
            item.setQuantity(cart.getQuantity());
            item.setSubtotal(subtotal);
            orderItemMapper.insert(item);

            LambdaUpdateWrapper<Product> sw = new LambdaUpdateWrapper<>();
            sw.eq(Product::getId, product.getId())
              .ge(Product::getStock, cart.getQuantity())
              .set(Product::getStock, product.getStock() - cart.getQuantity());
            if (productMapper.update(null, sw) != 1) {
                throw new IllegalArgumentException("商品库存不足：" + product.getName());
            }
            total = total.add(subtotal);
            shoppingCartMapper.deleteById(cart.getId());
        }

        order.setTotalAmount(total);
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);
        return order;
    }

    @Override
    public List<Order> getMyOrders(Long userId) {
        LambdaQueryWrapper<Order> w = new LambdaQueryWrapper<>();
        w.eq(Order::getUserId, userId).orderByDesc(Order::getCreateTime);
        return orderMapper.selectList(w);
    }

    @Override
    public OrderDetailResponse getOrderDetail(Long userId, Long orderId) {
        Order order = requireUserOrder(userId, orderId);
        LambdaQueryWrapper<OrderItem> w = new LambdaQueryWrapper<>();
        w.eq(OrderItem::getOrderId, orderId);
        return new OrderDetailResponse(order, orderItemMapper.selectList(w));
    }

    @Override
    public List<AdminOrderResponse> getAllOrders() {
        LambdaQueryWrapper<Order> w = new LambdaQueryWrapper<>();
        w.orderByDesc(Order::getCreateTime);
        List<AdminOrderResponse> result = new ArrayList<>();
        for (Order order : orderMapper.selectList(w)) {
            User user = userMapper.selectById(order.getUserId());
            result.add(new AdminOrderResponse(order, user == null ? null : user.getUsername()));
        }
        return result;
    }

    @Override
    @Transactional
    public Order updateStatus(Long orderId, Integer status) {
        if (!OrderStatus.isValid(status)) throw new IllegalArgumentException("订单状态不合法");
        Order order = requireOrder(orderId);
        if (order.getStatus() == OrderStatus.CANCELLED && status != OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("已取消订单不能恢复");
        }
        order.setStatus(status);
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);
        return order;
    }

    @Override
    @Transactional
    public Order pay(Long userId, Long orderId) {
        Order order = requireUserOrder(userId, orderId);
        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) throw new IllegalArgumentException("当前订单不可支付");
        order.setStatus(OrderStatus.PAID);
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);
        pointService.earn(userId, order.getTotalAmount().intValue(), "订单支付奖励积分", orderId);
        return order;
    }

    @Override
    @Transactional
    public Order cancel(Long userId, Long orderId) {
        Order order = requireUserOrder(userId, orderId);
        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) throw new IllegalArgumentException("当前订单不可取消");
        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);
        return order;
    }

    private Order requireOrder(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) throw new IllegalArgumentException("订单不存在");
        return order;
    }

    private Order requireUserOrder(Long userId, Long orderId) {
        LambdaQueryWrapper<Order> w = new LambdaQueryWrapper<>();
        w.eq(Order::getId, orderId).eq(Order::getUserId, userId);
        Order order = orderMapper.selectOne(w);
        if (order == null) throw new IllegalArgumentException("订单不存在");
        return order;
    }

    private String generateOrderNo() {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return time + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
}
