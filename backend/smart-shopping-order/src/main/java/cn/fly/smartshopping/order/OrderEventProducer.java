package cn.fly.smartshopping.order;

import cn.fly.smartshopping.common.OrderCreatedEvent;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {
    private final RocketMQTemplate rocketMQTemplate;

    public OrderEventProducer(RocketMQTemplate rocketMQTemplate) {
        this.rocketMQTemplate = rocketMQTemplate;
    }

    public void publish(OrderCreatedEvent event) {
        rocketMQTemplate.convertAndSend("smart-order-created", event);
    }
}
