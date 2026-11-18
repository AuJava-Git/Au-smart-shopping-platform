package cn.fly.smartshopping.controller;

import cn.fly.smartshopping.DTO.LoginResponse;
import cn.fly.smartshopping.common.Result;
import cn.fly.smartshopping.entity.ProductFavorite;
import cn.fly.smartshopping.service.ProductFavoriteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
public class ProductFavoriteController {

    private final ProductFavoriteService favoriteService;

    public ProductFavoriteController(
            ProductFavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    /**
     * 查询当前登录用户的收藏列表
     */
    @GetMapping
    public Result<List<ProductFavorite>> getMyFavorites(
            HttpSession session) {

        LoginResponse user = getLoginUser(session);

        if (user == null) {
            return Result.unauthorized("请先登录");
        }

        return Result.success(
                favoriteService.getMyFavorites(user.getId())
        );
    }

    /**
     * 检查当前商品是否已收藏
     */
    @GetMapping("/check/{productId}")
    public Result<Boolean> check(
            @PathVariable Long productId,
            HttpSession session) {

        LoginResponse user = getLoginUser(session);

        // 未登录时默认没有收藏
        if (user == null) {
            return Result.success(false);
        }

        boolean result = favoriteService.isFavorite(
                user.getId(),
                productId
        );

        return Result.success(result);
    }

    /**
     * 收藏商品
     */
    @PostMapping("/{productId}")
    public Result<Boolean> add(
            @PathVariable Long productId,
            HttpSession session) {

        LoginResponse user = getLoginUser(session);

        if (user == null) {
            return Result.unauthorized("请先登录");
        }

        boolean success = favoriteService.add(
                user.getId(),
                productId
        );

        if (!success) {
            return Result.error("商品已经收藏过了");
        }

        return Result.success("收藏成功", true);
    }

    /**
     * 取消收藏
     */
    @DeleteMapping("/{productId}")
    public Result<Boolean> remove(
            @PathVariable Long productId,
            HttpSession session) {

        LoginResponse user = getLoginUser(session);

        if (user == null) {
            return Result.unauthorized("请先登录");
        }

        boolean success = favoriteService.remove(
                user.getId(),
                productId
        );

        return Result.success("取消收藏成功", success);
    }

    private LoginResponse getLoginUser(HttpSession session) {
        return (LoginResponse) session.getAttribute("loginUser");
    }
}
