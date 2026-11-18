package cn.fly.smartshopping.service;

import cn.fly.smartshopping.DTO.AdminOrderResponse;
import cn.fly.smartshopping.DTO.OrderDetailResponse;
import cn.fly.smartshopping.entity.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(Long userId, Long addressId);
    List<Order> getMyOrders(Long userId);
    OrderDetailResponse getOrderDetail(Long userId, Long orderId);
    List<AdminOrderResponse> getAllOrders();
    Order updateStatus(Long orderId, Integer status);
    Order pay(Long userId, Long orderId);
    Order cancel(Long userId, Long orderId);
}
