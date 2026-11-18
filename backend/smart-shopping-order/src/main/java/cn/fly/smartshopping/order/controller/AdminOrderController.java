package cn.fly.smartshopping.controller;

import cn.fly.smartshopping.DTO.AdminOrderResponse;
import cn.fly.smartshopping.DTO.UpdateOrderStatusRequest;
import cn.fly.smartshopping.common.Result;
import cn.fly.smartshopping.entity.Order;
import cn.fly.smartshopping.security.RequireAdmin;
import cn.fly.smartshopping.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/orders")
public class AdminOrderController {
    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) { this.orderService = orderService; }

    @GetMapping
    @RequireAdmin
    public Result<List<AdminOrderResponse>> getAllOrders() {
        return Result.success(orderService.getAllOrders());
    }

    @PutMapping("/{orderId}/status")
    @RequireAdmin
    public Result<Order> updateStatus(@PathVariable Long orderId,
                                      @Valid @RequestBody UpdateOrderStatusRequest request) {
        return Result.success("订单状态修改成功", orderService.updateStatus(orderId, request.getStatus()));
    }
}
