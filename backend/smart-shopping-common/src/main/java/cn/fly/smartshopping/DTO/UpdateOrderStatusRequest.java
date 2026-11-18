package cn.fly.smartshopping.DTO;

import jakarta.validation.constraints.NotNull;

public class UpdateOrderStatusRequest {
    @NotNull(message = "订单状态不能为空")
    private Integer status;

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
