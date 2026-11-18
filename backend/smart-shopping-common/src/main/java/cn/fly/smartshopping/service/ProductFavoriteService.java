package cn.fly.smartshopping.service;

import cn.fly.smartshopping.entity.ProductFavorite;

import java.util.List;

public interface ProductFavoriteService {

    List<ProductFavorite> getMyFavorites(Long userId);

    boolean isFavorite(Long userId, Long productId);

    boolean add(Long userId, Long productId);

    boolean remove(Long userId, Long productId);
}
