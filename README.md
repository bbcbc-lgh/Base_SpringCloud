# Base_SpringCloud

一个基于 Spring Boot 3.2、Spring Cloud 2023 和 Spring Cloud Alibaba 的多模块微服务示例项目。项目围绕“用户注册登录、网关鉴权、下单、库存扣减、订单消息通知”这一条典型业务链路组织代码，适合作为学习 Spring Cloud Alibaba 微服务架构的基础工程。

## 项目定位

本项目已经具备一个微服务 Demo/课程作业级项目所需的核心结构：服务拆分、网关入口、认证鉴权、服务注册发现、OpenFeign 调用、Sentinel 接入、Seata 分布式事务、RocketMQ 消息事件、MyBatis Plus 持久化和公共组件抽取。

如果要用于生产环境，还需要继续补充自动化测试、CI/CD、容器化部署、配置中心隔离、日志采集、监控告警、灰度发布、密钥管理等工程能力。

## 技术栈

| 类型 | 技术 |
| --- | --- |
| 基础框架 | Spring Boot 3.2.4, Spring Cloud 2023.0.1 |
| 微服务组件 | Spring Cloud Alibaba 2023.0.1.0 |
| 服务注册/配置 | Nacos |
| 网关 | Spring Cloud Gateway |
| 服务调用 | OpenFeign, Spring Cloud LoadBalancer |
| 限流熔断 | Sentinel |
| 分布式事务 | Seata |
| 消息队列 | RocketMQ |
| 数据访问 | MyBatis Plus, MySQL |
| 缓存 | Redis |
| 链路追踪 | Micrometer Tracing / Brave |
| 构建工具 | Maven |
| Java 版本 | JDK 17 |

## 模块说明

```text
Base_SpringCloud
├── common
│   ├── common-core       # 通用返回、异常、Token、配置类
│   ├── common-log        # 操作日志注解与 AOP
│   ├── common-redis      # Redis 配置与缓存工具
│   └── common-security   # 请求鉴权上下文、拦截器、全局异常处理
├── gateway               # API 网关，统一路由和 Token 校验
├── monitor-service       # 监控入口服务
├── auth-service          # 登录、注册、Token 签发
├── user-service          # 用户信息管理
├── order-service         # 订单创建、订单查询、Seata 全局事务发起方
├── stock-service         # 库存查询与扣减
└── message-service       # 订单事件消费与消息记录
```

## 架构概览

```text
Client
  |
  v
Spring Cloud Gateway :8001
  |-- /api/auth/**     -> auth-service :8006
  |-- /api/users/**    -> user-service :8002
  |-- /api/orders/**   -> order-service :8003
  |-- /api/messages/** -> message-service :8004
  `-- /api/stock/**    -> stock-service :8005

order-service --OpenFeign--> stock-service
order-service --RocketMQ--> message-service

Nacos      : service discovery / config
Sentinel   : flow control
Seata      : distributed transaction
MySQL      : business data
Redis      : cache support
```

## 端口约定

| 服务 | 端口 |
| --- | --- |
| gateway | 8001 |
| user-service | 8002 |
| order-service | 8003 |
| message-service | 8004 |
| stock-service | 8005 |
| auth-service | 8006 |
| monitor-service | 8007 |

## 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.8+
- MySQL 8+
- Nacos
- Sentinel Dashboard
- Seata Server
- RocketMQ
- Redis

### 2. 初始化数据库

项目内各业务服务提供了独立的 `schema.sql`：

```text
user-service/src/main/resources/schema.sql
order-service/src/main/resources/schema.sql
stock-service/src/main/resources/schema.sql
message-service/src/main/resources/schema.sql
```

默认数据库：

```text
springcloud_user
springcloud_business
```

可以根据自己的环境调整各服务 `application.yml` 中的数据库连接配置，或通过环境变量覆盖。

### 3. 常用环境变量

| 变量名 | 默认值 | 说明 |
| --- | --- | --- |
| NACOS_ADDR | 127.0.0.1:8848 | Nacos 地址 |
| NACOS_NAMESPACE | public | Nacos 命名空间 |
| SENTINEL_DASHBOARD | 127.0.0.1:8858 | Sentinel 控制台 |
| SEATA_ADDR | 127.0.0.1:8091 | Seata 地址 |
| ROCKETMQ_NAME_SERVER | 127.0.0.1:9876 | RocketMQ NameServer |
| REDIS_HOST | 127.0.0.1 | Redis 主机 |
| REDIS_PORT | 6379 | Redis 端口 |
| DB_USERNAME | root | 数据库用户名 |
| DB_PASSWORD | root | 数据库密码 |
| APP_TOKEN_SECRET | gzu-demo-secret | Token 签名密钥 |

### 4. 编译项目

```bash
mvn clean package -DskipTests
```

### 5. 启动顺序

建议先启动基础设施，然后按下面顺序启动业务服务：

```bash
mvn -pl user-service spring-boot:run
mvn -pl stock-service spring-boot:run
mvn -pl message-service spring-boot:run
mvn -pl auth-service spring-boot:run
mvn -pl order-service spring-boot:run
mvn -pl gateway spring-boot:run
mvn -pl monitor-service spring-boot:run
```

## 核心接口

### 认证服务

```http
POST /api/auth/register
POST /api/auth/login
GET  /api/auth/me
```

### 用户服务

```http
GET  /api/users/me
POST /internal/users
GET  /internal/users/{username}
```

### 库存服务

```http
GET  /api/stock/{productCode}
POST /internal/stock/deduct
```

### 订单服务

```http
POST /api/orders
GET  /api/orders/my
```

### 消息服务

```http
GET  /api/messages
GET  /api/messages/{id}
```

## 业务链路

1. 用户通过 `auth-service` 注册或登录。
2. `auth-service` 调用 `user-service` 创建或查询用户。
3. 登录成功后返回 Token。
4. 客户端携带 Token 访问 `gateway`。
5. `gateway` 校验 Token，并向下游服务透传用户上下文。
6. 用户创建订单时，`order-service` 开启 Seata 全局事务。
7. `order-service` 写入订单，并通过 OpenFeign 调用 `stock-service` 扣减库存。
8. 扣减成功后，`order-service` 发送 RocketMQ 订单事件。
9. `message-service` 消费事件并记录消息。

## 当前完善度评估

已具备：

- 清晰的 Maven 多模块结构
- 网关统一入口与认证上下文透传
- 公共返回体、异常、日志、安全组件抽取
- 注册发现、配置中心、限流、事务、消息队列等 Alibaba 微服务组件接入
- 用户、认证、订单、库存、消息等核心业务闭环
- 项目可通过 `mvn clean package -DskipTests` 编译

仍可增强：

- 增加单元测试和集成测试
- 增加 Docker Compose 或 Kubernetes 部署文件
- 增加 GitHub Actions CI
- 抽离更多敏感配置并提供 `.env.example`
- 增加 API 文档，例如 Knife4j/OpenAPI
- 增加日志采集、指标监控和告警配置

## 编译验证

当前代码已通过：

```bash
mvn clean package -DskipTests
```

## License

This project is for learning and demonstration purposes.
