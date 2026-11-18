package cn.fly.smartshopping.controller;

import cn.fly.smartshopping.DTO.LoginRequest;
import cn.fly.smartshopping.DTO.LoginResponse;
import cn.fly.smartshopping.DTO.UpdatePasswordRequest;
import cn.fly.smartshopping.DTO.UpdateProfileRequest;
import cn.fly.smartshopping.common.Result;
import cn.fly.smartshopping.entity.User;
import cn.fly.smartshopping.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Result<Boolean> register(@Valid @RequestBody User user) {
        if (!userService.create(user)) return Result.error("用户名已存在");
        return Result.success("注册成功", true);
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request,
                                       HttpSession session) {
        LoginResponse response = userService.login(request);
        if (response == null) return Result.error("用户名或密码错误");
        session.setAttribute("loginUser", response);
        return Result.success("登录成功", response);
    }

    @GetMapping("/me")
    public Result<LoginResponse> getCurrentUser(HttpSession session) {
        LoginResponse loginUser = getLoginUser(session);
        if (loginUser == null) return Result.unauthorized("请先登录");
        return Result.success(loginUser);
    }

    @PutMapping("/profile")
    public Result<LoginResponse> updateProfile(@Valid @RequestBody UpdateProfileRequest request,
                                               HttpSession session) {
        LoginResponse loginUser = requireLogin(session);
        LoginResponse updated = userService.updateProfile(loginUser.getId(), request);
        session.setAttribute("loginUser", updated);
        return Result.success("个人资料修改成功", updated);
    }

    @PutMapping("/password")
    public Result<Void> updatePassword(@Valid @RequestBody UpdatePasswordRequest request,
                                       HttpSession session) {
        LoginResponse loginUser = requireLogin(session);
        userService.updatePassword(loginUser.getId(), request);
        return Result.success("密码修改成功", null);
    }

    @PostMapping("/logout")
    public Result<String> logout(HttpSession session) {
        session.invalidate();
        return Result.success("退出登录成功", null);
    }

    private LoginResponse requireLogin(HttpSession session) {
        LoginResponse loginUser = getLoginUser(session);
        if (loginUser == null) throw new IllegalArgumentException("请先登录");
        return loginUser;
    }

    private LoginResponse getLoginUser(HttpSession session) {
        return (LoginResponse) session.getAttribute("loginUser");
    }
}
