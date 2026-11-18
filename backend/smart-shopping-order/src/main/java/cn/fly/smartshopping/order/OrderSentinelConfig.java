package cn.fly.smartshopping.order;

import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderSentinelConfig {
    // Sentinel Dashboard 地址和流控规则在 application.yml 中配置。
    // 真实订单接口迁移后，在方法上增加 @SentinelResource 即可。
}
