package cn.fly.smartshopping.service;

import cn.fly.smartshopping.DTO.LoginRequest;
import cn.fly.smartshopping.DTO.LoginResponse;
import cn.fly.smartshopping.DTO.UpdatePasswordRequest;
import cn.fly.smartshopping.DTO.UpdateProfileRequest;
import cn.fly.smartshopping.entity.User;

public interface UserService {
    User getById(Long id);
    boolean create(User user);
    LoginResponse login(LoginRequest request);
    LoginResponse updateProfile(Long userId, UpdateProfileRequest request);
    void updatePassword(Long userId, UpdatePasswordRequest request);
}
