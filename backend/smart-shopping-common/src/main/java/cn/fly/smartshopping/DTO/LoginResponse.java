package cn.fly.smartshopping.DTO;

import java.io.Serial;
import java.io.Serializable;

/**
 * 登录成功后返回给前端的数据对象。
 *
 * 不包含用户密码。
 *
 * LoginResponse 会被保存到 Spring Session，
 * 而当前项目使用 Redis 保存 Session，
 * 因此必须实现 Serializable。
 */
public class LoginResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String nickname;

    private String email;

    private String phone;

    /**
     * 用户角色：
     * 0：普通用户
     * 1：管理员
     */
    private Integer role;

    public LoginResponse() {
    }

    public LoginResponse(
            Long id,
            String username,
            String nickname,
            String email,
            String phone,
            Integer role
    ) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }
}
