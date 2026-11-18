package cn.fly.smartshopping.common;

import java.io.Serializable;
import java.math.BigDecimal;

public record OrderCreatedEvent(
        Long orderId,
        Long userId,
        BigDecimal amount
) implements Serializable {
}
