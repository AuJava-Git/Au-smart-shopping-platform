# 智能购物平台企业级微服务版

本版本以之前的“六大模块完成版”为业务基线，保留用户、商品、订单、购物车、地址、分类、收藏、评论审核、站内消息、积分、客服和智能推荐等业务；技术栈统一为 Spring Boot 3.5、Spring Cloud Alibaba、Nacos、Gateway、OpenFeign、Sentinel、Redis、RocketMQ、MySQL、MyBatis-Plus、Vue、Docker。本人后续可能要转嵌入式，以此纪念我的第一个项目，对于毕业设计有需要的同学，这个是一个很好的选择，小众且完整有亮点。

## 目录
- `backend`：Maven 父子微服务工程
- `frontend`：Vue 前端，统一请求 Gateway:9000
- `database-smart_shop.sql`：基础业务表和数据
- `database-feature-migration.sql`：地址、站内消息、积分及审核字段迁移
- `docker-compose.yml`：MySQL、Redis、Nacos、RocketMQ NameServer、Sentinel
- `组件使用说明.md`：组件落点、接口、调用链和设计思想

## 当前迁移策略
第一版使用共享 MySQL 数据库，先保证业务迁移和服务边界可运行；服务之间通过 Nacos 注册发现，订单服务保留 OpenFeign 与 RocketMQ 示例，Redis 用于 Session/购物车/缓存，Sentinel 用于流量保护。后续再按领域拆库，避免在业务未稳定时引入分布式事务。

平台展示
<img width="1028" height="734" alt="屏幕截图 2026-11-22 183000" src="https://github.com/user-attachments/assets/88e970d8-13af-4b08-b9e7-88fa9c74c630" />


