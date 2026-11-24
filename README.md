# Au慧心购物平台企业级微服务版

本版本以之前的“六大模块完成版”为业务基线，保留用户、商品、订单、购物车、地址、分类、收藏、评论审核、站内消息、积分、客服和智能推荐等业务；技术栈统一为 Spring Boot 3.5、Spring Cloud Alibaba、Nacos、Gateway、OpenFeign、Sentinel、Redis、RocketMQ、MySQL、MyBatis-Plus、Vue、Docker。以此纪念我的第一个项目，对于毕业设计有需要的同学，这个是一个很好的选择，小众且完整有亮点。

## 目录
- `backend`：Maven 父子微服务工程
- `frontend`：Vue 前端，统一请求 Gateway:9000
- `database-smart_shop.sql`：基础业务表和数据
- `database-feature-migration.sql`：地址、站内消息、积分及审核字段迁移
- `docker-compose.yml`：MySQL、Redis、Nacos、RocketMQ NameServer、Sentinel
- `组件使用说明.md`：组件落点、接口、调用链和设计思想

## 当前迁移策略
第一版使用共享 MySQL 数据库，先保证业务迁移和服务边界可运行；服务之间通过 Nacos 注册发现，订单服务保留 OpenFeign 与 RocketMQ 示例，Redis 用于 Session/购物车/缓存，Sentinel 用于流量保护。后续再按领域拆库，避免在业务未稳定时引入分布式事务。

## 我在下面放一些截图展示，具体内容可以自行下载使用

平台展示
<img width="1028" height="734" alt="屏幕截图 2026-11-22 183000" src="https://github.com/user-attachments/assets/88e970d8-13af-4b08-b9e7-88fa9c74c630" />

登录/注册界面
<img width="1034" height="728" alt="屏幕截图 2026-11-22 183028" src="https://github.com/user-attachments/assets/38ee48b8-74c7-4f30-8f86-4e93430d26dd" />

商品列表页面（截图导致泛白，正常使用无问题）
<img width="1032" height="733" alt="屏幕截图 2026-11-22 183055" src="https://github.com/user-attachments/assets/04785c53-2078-4f6b-9978-079ce416ced4" />

商品描述界面（截图导致泛白，正常使用无问题）
<img width="1030" height="733" alt="屏幕截图 2026-11-22 183118" src="https://github.com/user-attachments/assets/6acb32dc-cdbb-4994-8152-b44fdd66c570" />

商品分类部分（截图导致泛白，正常使用无问题）
<img width="1017" height="722" alt="屏幕截图 2026-11-22 183218" src="https://github.com/user-attachments/assets/3964a4cc-b91f-447d-be02-eb0cb8e06891" />

购物车界面（截图导致泛白，正常使用无问题）
<img width="1040" height="746" alt="屏幕截图 2026-11-22 183303" src="https://github.com/user-attachments/assets/c46b75cd-14ae-4442-8941-4262fd0f417c" />

提交订单界面（截图导致泛白，正常使用无问题）
<img width="1037" height="732" alt="屏幕截图 2026-11-22 183328" src="https://github.com/user-attachments/assets/f2cf40ee-b5bd-4dac-8533-a9558e6fa803" />

我的订单界面（截图导致泛白，正常使用无问题）
<img width="1036" height="725" alt="屏幕截图 2026-11-22 183343" src="https://github.com/user-attachments/assets/2a3d690b-9db4-40e3-adae-72f6af3b6df4" />

智能客服界面，接入api即可使用（截图导致泛白，正常使用无问题）
<img width="1085" height="736" alt="屏幕截图 2026-11-22 183510" src="https://github.com/user-attachments/assets/00fa2450-79e5-407a-9643-8338eef3ced1" />

管理员主界面（截图导致泛白，正常使用无问题）
<img width="1040" height="740" alt="屏幕截图 2026-11-22 183609" src="https://github.com/user-attachments/assets/9aa456b1-be88-4992-af52-9ed480c6c73c" />

管理员管理商品界面（截图导致泛白，正常使用无问题）
<img width="1027" height="733" alt="屏幕截图 2026-11-22 183638" src="https://github.com/user-attachments/assets/c1f4f2fb-adf5-4e75-a4fc-aee1b8cec562" />

管理员管理订单界面（截图导致泛白，正常使用无问题）
<img width="1037" height="721" alt="屏幕截图 2026-11-22 183649" src="https://github.com/user-attachments/assets/0e9c843c-66ea-4afb-8f10-a84e08e592b0" />

管理员管理分类界面（截图导致泛白，正常使用无问题）
<img width="1087" height="736" alt="屏幕截图 2026-11-22 183658" src="https://github.com/user-attachments/assets/58f244c4-4a4f-4294-bdc6-c0a853defd63" />

管理员管理评论页面（截图导致泛白，正常使用无问题）
<img width="1051" height="719" alt="屏幕截图 2026-11-22 183708" src="https://github.com/user-attachments/assets/63214dbd-cf44-4b9b-b655-5fa8f40c08ee" />
