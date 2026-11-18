package cn.fly.smartshopping.chat;

import cn.fly.smartshopping.common.OrderCreatedEvent;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(
        topic = "smart-order-created",
        consumerGroup = "smart-chat-order-group",
        nameServer = "${rocketmq.name-server}"
)

public class OrderCreatedListener implements RocketMQListener<OrderCreatedEvent> {
    @Override
    public void onMessage(OrderCreatedEvent event) {
        System.out.println("[chat-service] 收到订单创建事件：" + event);
    }
}
