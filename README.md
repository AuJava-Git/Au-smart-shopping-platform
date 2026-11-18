# Au-smart-shopping-platform
智能购物微服务平台  基于 Spring Boot、Spring Cloud Alibaba、Nacos、Gateway、OpenFeign、Sentinel、Redis、RocketMQ、MySQL、MyBatis-Plus 和 Vue 构建智能购物平台。系统按用户、商品、订单、客服等领域拆分微服务，通过 Nacos 实现服务注册与配置管理，使用 Gateway 统一路由，OpenFeign 完成服务间调用，RocketMQ 异步处理订单创建事件，Redis 实现 Session 共享和热点商品缓存，Sentinel 实现接口流量控制，并通过 WebSocket 实现客服实时通信和商品卡片推送。
