package cn.fly.smartshopping.service.impl;

import cn.fly.smartshopping.entity.Product;
import cn.fly.smartshopping.entity.ShoppingCart;
import cn.fly.smartshopping.mapper.ProductMapper;
import cn.fly.smartshopping.mapper.ShoppingCartMapper;
import cn.fly.smartshopping.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartMapper shoppingCartMapper;
    private final ProductMapper productMapper;

    public ShoppingCartServiceImpl(ShoppingCartMapper shoppingCartMapper, ProductMapper productMapper) {
        this.shoppingCartMapper = shoppingCartMapper;
        this.productMapper = productMapper;
    }

    @Override
    public ShoppingCart add(Long userId, Long productId, Integer quantity) {
        if (quantity == null || quantity < 1) throw new IllegalArgumentException("商品数量至少为1");
        Product product = requireProduct(productId);
        ShoppingCart cart = findCart(userId, productId);
        int newQuantity = quantity + (cart == null ? 0 : cart.getQuantity());
        checkStock(product, newQuantity);
        if (cart == null) {
            cart = new ShoppingCart();
            cart.setUserId(userId); cart.setProductId(productId); cart.setQuantity(quantity);
            shoppingCartMapper.insert(cart);
        } else {
            cart.setQuantity(newQuantity); shoppingCartMapper.updateById(cart);
        }
        return fillProductInfo(cart);
    }

    @Override
    public List<ShoppingCart> getByUserId(Long userId) {
        LambdaQueryWrapper<ShoppingCart> w = new LambdaQueryWrapper<>();
        w.eq(ShoppingCart::getUserId, userId).orderByDesc(ShoppingCart::getUpdateTime);
        List<ShoppingCart> carts = shoppingCartMapper.selectList(w);
        carts.forEach(this::fillProductInfo);
        return carts;
    }

    @Override
    public ShoppingCart updateQuantity(Long userId, Long cartId, Integer quantity) {
        if (quantity == null || quantity < 1) throw new IllegalArgumentException("商品数量至少为1");
        LambdaQueryWrapper<ShoppingCart> w = new LambdaQueryWrapper<>();
        w.eq(ShoppingCart::getId, cartId).eq(ShoppingCart::getUserId, userId);
        ShoppingCart cart = shoppingCartMapper.selectOne(w);
        if (cart == null) throw new IllegalArgumentException("购物车记录不存在");
        Product product = requireProduct(cart.getProductId());
        checkStock(product, quantity);
        cart.setQuantity(quantity); shoppingCartMapper.updateById(cart);
        return fillProductInfo(cart);
    }

    @Override
    public boolean delete(Long userId, Long cartId) {
        LambdaQueryWrapper<ShoppingCart> w = new LambdaQueryWrapper<>();
        w.eq(ShoppingCart::getId, cartId).eq(ShoppingCart::getUserId, userId);
        return shoppingCartMapper.delete(w) > 0;
    }

    private ShoppingCart findCart(Long userId, Long productId) {
        LambdaQueryWrapper<ShoppingCart> w = new LambdaQueryWrapper<>();
        w.eq(ShoppingCart::getUserId, userId).eq(ShoppingCart::getProductId, productId);
        return shoppingCartMapper.selectOne(w);
    }

    private Product requireProduct(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) throw new IllegalArgumentException("商品不存在");
        return product;
    }

    private void checkStock(Product product, int quantity) {
        if (product.getStock() == null) throw new IllegalArgumentException("商品库存数据异常");
        if (quantity > product.getStock()) throw new IllegalArgumentException("商品库存不足");
    }

    private ShoppingCart fillProductInfo(ShoppingCart cart) {
        Product product = productMapper.selectById(cart.getProductId());
        if (product != null) {
            cart.setProductName(product.getName());
            cart.setProductPrice(product.getPrice());
            cart.setProductDescription(product.getDescription());
        }
        return cart;
    }
}
