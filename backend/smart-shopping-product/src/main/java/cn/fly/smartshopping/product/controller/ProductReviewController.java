package cn.fly.smartshopping.controller;

import cn.fly.smartshopping.DTO.AppendReviewRequest;
import cn.fly.smartshopping.DTO.CreateReviewRequest;
import cn.fly.smartshopping.DTO.LoginResponse;
import cn.fly.smartshopping.DTO.ReviewEligibilityResponse;
import cn.fly.smartshopping.common.Result;
import cn.fly.smartshopping.entity.ProductReview;
import cn.fly.smartshopping.service.ProductReviewService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ProductReviewController {
    private final ProductReviewService reviewService;
    public ProductReviewController(ProductReviewService reviewService) { this.reviewService = reviewService; }

    @GetMapping("/product/{productId}")
    public Result<List<ProductReview>> getByProduct(@PathVariable Long productId, HttpSession session) {
        LoginResponse user = getLoginUser(session);
        return Result.success(reviewService.getByProductId(productId, user == null ? null : user.getId()));
    }

    @GetMapping("/eligibility/{productId}")
    public Result<ReviewEligibilityResponse> eligibility(@PathVariable Long productId, HttpSession session) {
        LoginResponse user = getLoginUser(session);
        if (user == null) {
            return Result.success(new ReviewEligibilityResponse(false, false, false, "登录后且购买该商品后才能评论"));
        }
        boolean purchased = reviewService.hasPaidPurchase(user.getId(), productId);
        boolean canReview = reviewService.getRemainingReviewCount(user.getId(), productId) > 0;
        String message = !purchased ? "购买并支付该商品后才可以评论" : canReview
                ? "你可以发表评论" : "你已经评论过该商品；再次购买并支付后才能发布新评论，可追加或删除现有评论";
        return Result.success(new ReviewEligibilityResponse(true, purchased, canReview, message));
    }

    @PostMapping
    public Result<ProductReview> create(@Valid @RequestBody CreateReviewRequest request, HttpSession session) {
        LoginResponse user = getLoginUser(session);
        if (user == null) return Result.unauthorized("请先登录后再评论");
        return Result.success("评论发布成功", reviewService.create(user.getId(), request.getProductId(), request.getRating(), request.getContent(), request.getAnonymous()));
    }

    @PutMapping("/{reviewId}/append")
    public Result<ProductReview> append(@PathVariable Long reviewId, @Valid @RequestBody AppendReviewRequest request, HttpSession session) {
        LoginResponse user = getLoginUser(session);
        if (user == null) return Result.unauthorized("请先登录");
        return Result.success("追加评论成功", reviewService.appendMyReview(user.getId(), reviewId, request.getContent()));
    }

    @DeleteMapping("/{reviewId}")
    public Result<Boolean> delete(@PathVariable Long reviewId, HttpSession session) {
        LoginResponse user = getLoginUser(session);
        if (user == null) return Result.unauthorized("请先登录");
        return Result.success("评论删除成功", reviewService.deleteMyReview(user.getId(), reviewId));
    }

    private LoginResponse getLoginUser(HttpSession session) { return (LoginResponse) session.getAttribute("loginUser"); }
}
