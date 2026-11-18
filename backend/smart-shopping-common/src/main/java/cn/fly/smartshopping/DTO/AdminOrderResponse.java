package cn.fly.smartshopping.DTO;

import cn.fly.smartshopping.entity.Order;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AdminOrderResponse {
    private Order order;
    private String username;

    public AdminOrderResponse() {}
    public AdminOrderResponse(Order order, String username) { this.order = order; this.username = username; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    // 顶层字段让前端可以直接使用 orderNo、userId、totalAmount 等属性。
    public Long getId() { return order == null ? null : order.getId(); }
    public String getOrderNo() { return order == null ? null : order.getOrderNo(); }
    public Long getUserId() { return order == null ? null : order.getUserId(); }
    public BigDecimal getTotalAmount() { return order == null ? null : order.getTotalAmount(); }
    public Integer getStatus() { return order == null ? null : order.getStatus(); }
    public LocalDateTime getCreateTime() { return order == null ? null : order.getCreateTime(); }
    public LocalDateTime getUpdateTime() { return order == null ? null : order.getUpdateTime(); }
}
