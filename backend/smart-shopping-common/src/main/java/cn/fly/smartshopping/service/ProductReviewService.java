package cn.fly.smartshopping.service;

import cn.fly.smartshopping.entity.ProductReview;
import java.util.List;

public interface ProductReviewService {
    List<ProductReview> getByProductId(Long productId, Long userId);
    boolean hasPaidPurchase(Long userId, Long productId);
    int getRemainingReviewCount(Long userId, Long productId);
    ProductReview create(Long userId, Long productId, Integer rating, String content, Boolean anonymous);
    ProductReview appendMyReview(Long userId, Long reviewId, String content);
    boolean deleteMyReview(Long userId, Long reviewId);
}
