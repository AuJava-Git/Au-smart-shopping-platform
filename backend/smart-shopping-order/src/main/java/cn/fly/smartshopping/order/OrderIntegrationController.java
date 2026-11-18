package cn.fly.smartshopping.order;

import cn.fly.smartshopping.common.ApiResult;
import cn.fly.smartshopping.common.OrderCreatedEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/internal/orders")
public class OrderIntegrationController {
    private final UserClient userClient;
    private final OrderEventProducer eventProducer;

    public OrderIntegrationController(UserClient userClient, OrderEventProducer eventProducer) {
        this.userClient = userClient;
        this.eventProducer = eventProducer;
    }

    @PostMapping("/demo")
    public ApiResult<String> demo(
            @RequestParam Long userId,
            @RequestParam Long orderId,
            @RequestParam BigDecimal amount
    ) {
        ApiResult<Boolean> user = userClient.exists(userId);
        if (user.data() == null || !user.data()) {
            return ApiResult.error("用户不存在");
        }
        eventProducer.publish(new OrderCreatedEvent(orderId, userId, amount));
        return ApiResult.success("订单事件已发送");
    }
}
