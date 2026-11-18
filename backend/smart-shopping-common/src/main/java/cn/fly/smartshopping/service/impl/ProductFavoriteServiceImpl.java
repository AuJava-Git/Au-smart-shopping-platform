package cn.fly.smartshopping.service.impl;

import cn.fly.smartshopping.entity.ProductFavorite;
import cn.fly.smartshopping.mapper.ProductFavoriteMapper;
import cn.fly.smartshopping.service.ProductFavoriteService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductFavoriteServiceImpl
        implements ProductFavoriteService {

    private final ProductFavoriteMapper favoriteMapper;

    public ProductFavoriteServiceImpl(
            ProductFavoriteMapper favoriteMapper) {
        this.favoriteMapper = favoriteMapper;
    }

    @Override
    public List<ProductFavorite> getMyFavorites(Long userId) {
        return favoriteMapper.selectMyFavorites(userId);
    }

    @Override
    public boolean isFavorite(Long userId, Long productId) {
        return favoriteMapper.selectCount(
                new LambdaQueryWrapper<ProductFavorite>()
                        .eq(ProductFavorite::getUserId, userId)
                        .eq(ProductFavorite::getProductId, productId)
        ) > 0;
    }

    @Override
    public boolean add(Long userId, Long productId) {
        if (isFavorite(userId, productId)) {
            return false;
        }

        ProductFavorite favorite = new ProductFavorite();
        favorite.setUserId(userId);
        favorite.setProductId(productId);

        return favoriteMapper.insert(favorite) > 0;
    }

    @Override
    public boolean remove(Long userId, Long productId) {
        return favoriteMapper.delete(
                new LambdaQueryWrapper<ProductFavorite>()
                        .eq(ProductFavorite::getUserId, userId)
                        .eq(ProductFavorite::getProductId, productId)
        ) > 0;
    }
}
