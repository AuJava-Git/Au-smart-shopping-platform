package cn.fly.smartshopping.service.impl;

import cn.fly.smartshopping.entity.ProductReview;
import cn.fly.smartshopping.mapper.OrderItemMapper;
import cn.fly.smartshopping.mapper.ProductReviewMapper;
import cn.fly.smartshopping.service.ProductReviewService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductReviewServiceImpl implements ProductReviewService {
    private final ProductReviewMapper reviewMapper;
    private final OrderItemMapper orderItemMapper;

    public ProductReviewServiceImpl(ProductReviewMapper reviewMapper, OrderItemMapper orderItemMapper) {
        this.reviewMapper = reviewMapper;
        this.orderItemMapper = orderItemMapper;
    }

    @Override public List<ProductReview> getByProductId(Long productId, Long userId) { return reviewMapper.selectByProductId(productId, userId); }

    @Override public boolean hasPaidPurchase(Long userId, Long productId) {
        return orderItemMapper.countPaidPurchase(userId, productId) > 0;
    }

    @Override public int getRemainingReviewCount(Long userId, Long productId) {
        return Math.max(0, orderItemMapper.countPaidPurchase(userId, productId)
                - reviewMapper.countByUserAndProduct(userId, productId));
    }

    @Override
    public ProductReview create(Long userId, Long productId, Integer rating, String content, Boolean anonymous) {
        if (!hasPaidPurchase(userId, productId)) {
            throw new IllegalArgumentException("购买并支付该商品后才可以评论");
        }
        if (getRemainingReviewCount(userId, productId) <= 0) {
            throw new IllegalArgumentException("你已经评论过该商品；再次购买并支付后才能发布新评论，你也可以追加或删除现有评论");
        }
        ProductReview review = new ProductReview();
        review.setUserId(userId); review.setProductId(productId); review.setRating(rating);
        review.setContent(content.trim()); review.setAnonymous(Boolean.TRUE.equals(anonymous));
        review.setAuditStatus(0);
        reviewMapper.insert(review);
        return review;
    }

    @Override public ProductReview appendMyReview(Long userId, Long reviewId, String content) {
        ProductReview review = reviewMapper.selectOne(new LambdaQueryWrapper<ProductReview>()
                .eq(ProductReview::getId, reviewId).eq(ProductReview::getUserId, userId));
        if (review == null) throw new IllegalArgumentException("只能追加自己的评论");
        String oldContent = review.getContent() == null ? "" : review.getContent();
        String appended = content.trim();
        if (oldContent.length() + appended.length() + 7 > 500) {
            throw new IllegalArgumentException("追加后评论总长度不能超过500字");
        }
        review.setContent(oldContent + "\n追加评论：" + appended);
        reviewMapper.updateById(review);
        return review;
    }

    @Override
    public boolean deleteMyReview(Long userId, Long reviewId) {
        return reviewMapper.delete(new LambdaQueryWrapper<ProductReview>()
                .eq(ProductReview::getId, reviewId)
                .eq(ProductReview::getUserId, userId)) > 0;
    }
}
