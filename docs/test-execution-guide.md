# SpringCloud Alibaba 微服务电商订单系统测试说明

## 一、测试环境

- Gateway: `http://127.0.0.1:8001`
- Nacos: `http://127.0.0.1:8848/nacos`
- Sentinel Dashboard: `http://127.0.0.1:8858`
- SkyWalking UI: `http://127.0.0.1:8080`
- MySQL 数据库: `springcloud_user`、`springcloud_business`
- 固定商品: `P001`
- 初始库存: `100`
- 演示账号: `demo_user`
- 演示密码: `demo@123456`

## 二、服务启动顺序

1. 启动 Docker 基础组件：Nacos、Seata、Sentinel、RocketMQ、Redis、SkyWalking。
2. 启动 MySQL。
3. 初始化数据库脚本：
   - `user-service/src/main/resources/schema.sql`
   - `stock-service/src/main/resources/schema.sql`
   - `order-service/src/main/resources/schema.sql`
   - `message-service/src/main/resources/schema.sql`
4. 启动微服务：
   - `user-service`
   - `stock-service`
   - `message-service`
   - `auth-service`
   - `order-service`
   - `gateway`
   - `monitor-service`

## 三、Postman 测试

导入以下两个文件：

- `postman/SpringCloud-Alibaba-Order-System.postman_collection.json`
- `postman/SpringCloud-Alibaba-Order-System.postman_environment.json`

建议截图：

- 登录成功并返回 Token。
- 查询库存，记录扣减前库存。
- 创建订单成功。
- 再次查询库存，记录扣减后库存。
- 查询我的订单。
- 查询消息记录。
- 未携带 Token 访问失败。
- 库存不足下单失败。

## 四、JMeter 压测

建议压测接口：

- 登录接口：`POST /api/auth/login`
- 查询库存接口：`GET /api/stock/P001`
- 创建订单接口：`POST /api/orders`

建议线程组：

- 10 线程，循环 5 次。
- 50 线程，循环 5 次。
- 100 线程，循环 5 次。

建议截图：

- 聚合报告。
- 察看结果树。
- 响应时间图。
- 错误率。

## 五、已验证业务链路

本地已跑通：

`注册/登录 -> 获取 Token -> 查询库存 -> 创建订单 -> 扣减库存 -> RocketMQ 消息消费 -> 查询订单 -> 查询消息记录`

验证结果示例：

- 创建订单前 `P001` 库存：`100`
- 创建订单后 `P001` 库存：`99`
- 订单状态：`CREATED`
- 消息类型：`ORDER_CREATED`
