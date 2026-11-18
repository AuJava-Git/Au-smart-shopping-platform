package cn.fly.smartshopping.order.controller;

import cn.fly.smartshopping.common.Result;
import cn.fly.smartshopping.entity.Order;
import cn.fly.smartshopping.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 仅供 chat-service 调用，不通过 Gateway 暴露。 */
@RestController
@RequestMapping("/internal/orders")
public class InternalOrderController {

    private final OrderService orderService;

    @Value("${internal.api-token:smart-shopping-internal}")
    private String internalToken;

    public InternalOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/user/{userId}")
    public Result<List<Order>> getUserOrders(
            @PathVariable Long userId,
            @RequestHeader(value = "X-Internal-Token", required = false) String token) {
        if (!internalToken.equals(token)) {
            return Result.unauthorized("内部接口令牌无效");
        }
        return Result.success(orderService.getMyOrders(userId));
    }
}
