package cn.fly.smartshopping.controller;

import cn.fly.smartshopping.DTO.LoginResponse;
import cn.fly.smartshopping.DTO.OrderDetailResponse;
import cn.fly.smartshopping.common.Result;
import cn.fly.smartshopping.entity.Order;
import cn.fly.smartshopping.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) { this.orderService = orderService; }

    @PostMapping
    public Result<Order> createOrder(@RequestBody(required = false) CreateOrderRequest request, HttpSession session) {
        LoginResponse user = requireLogin(session);
        return Result.success("订单创建成功", orderService.createOrder(user.getId(), request == null ? null : request.addressId));
    }

    @GetMapping
    public Result<List<Order>> getMyOrders(HttpSession session) {
        LoginResponse user = requireLogin(session);
        return Result.success(orderService.getMyOrders(user.getId()));
    }

    @GetMapping("/{orderId}")
    public Result<OrderDetailResponse> getOrderDetail(@PathVariable Long orderId, HttpSession session) {
        LoginResponse user = requireLogin(session);
        return Result.success(orderService.getOrderDetail(user.getId(), orderId));
    }

    @PutMapping("/{orderId}/pay")
    public Result<Order> pay(@PathVariable Long orderId, HttpSession session) {
        LoginResponse user = requireLogin(session);
        return Result.success("支付成功", orderService.pay(user.getId(), orderId));
    }

    @PutMapping("/{orderId}/cancel")
    public Result<Order> cancel(@PathVariable Long orderId, HttpSession session) {
        LoginResponse user = requireLogin(session);
        return Result.success("订单已取消", orderService.cancel(user.getId(), orderId));
    }

    private LoginResponse requireLogin(HttpSession session) {
        LoginResponse user = (LoginResponse) session.getAttribute("loginUser");
        if (user == null) throw new IllegalArgumentException("请先登录");
        return user;
    }
    public static class CreateOrderRequest { public Long addressId; }
}
