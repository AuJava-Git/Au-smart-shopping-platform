package cn.fly.smartshopping.service.impl;

import cn.fly.smartshopping.DTO.LoginRequest;
import cn.fly.smartshopping.DTO.LoginResponse;
import cn.fly.smartshopping.DTO.UpdatePasswordRequest;
import cn.fly.smartshopping.DTO.UpdateProfileRequest;
import cn.fly.smartshopping.entity.User;
import cn.fly.smartshopping.mapper.UserMapper;
import cn.fly.smartshopping.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    @Transactional
    public boolean create(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        if (userMapper.selectOne(wrapper) != null) return false;

        user.setStatus(1);
        user.setRole(0);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.insert(user) > 0;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, request.getUsername());
        User user = userMapper.selectOne(wrapper);
        if (user == null || user.getStatus() == null || user.getStatus() != 1) return null;
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) return null;
        return toLoginResponse(user);
    }

    @Override
    @Transactional
    public LoginResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = requireUser(userId);
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        userMapper.updateById(user);
        return toLoginResponse(userMapper.selectById(userId));
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, UpdatePasswordRequest request) {
        User user = requireUser(userId);
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("原密码不正确");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userMapper.updateById(user);
    }

    private User requireUser(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new IllegalArgumentException("用户不存在");
        return user;
    }

    private LoginResponse toLoginResponse(User user) {
        Integer role = user.getRole() == null ? 0 : user.getRole();
        return new LoginResponse(user.getId(), user.getUsername(), user.getNickname(),
                user.getEmail(), user.getPhone(), role);
    }
}
