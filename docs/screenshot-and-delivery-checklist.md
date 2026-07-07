# 六条路线截图与交付清单

## 路线一：项目功能与完整性

| 材料 | 截图位置 | 状态 |
| --- | --- | --- |
| Docker 容器运行状态 | Docker Desktop 或 `docker ps` 输出 | 待截图 |
| Nacos 服务列表 | `http://127.0.0.1:8848/nacos` | 待截图 |
| 登录成功返回 Token | Postman 登录接口 | 待截图 |
| 库存扣减前 | Postman 查询 `/api/stock/P001` | 待截图 |
| 创建订单成功 | Postman 创建 `/api/orders` | 待截图 |
| 库存扣减后 | Postman 再次查询 `/api/stock/P001` | 待截图 |
| 订单记录 | Postman 查询 `/api/orders/my` | 待截图 |
| 消息记录 | Postman 查询 `/api/messages/events` | 待截图 |

## 路线二：技术规范与架构设计

| 材料 | 文件或位置 | 状态 |
| --- | --- | --- |
| 系统架构图 | `docs/architecture-and-report-material.md` | 已完成 |
| 订单创建流程图 | `docs/architecture-and-report-material.md` | 已完成 |
| Gateway 路由配置 | `gateway/src/main/resources/application.yml` | 待截图 |
| Feign 接口 | `auth-service/.../UserFeignClient.java`、`order-service/.../StockFeignClient.java` | 待截图 |
| Seata 全局事务 | `order-service/.../OrderServiceImpl.java` | 待截图 |
| RocketMQ 生产者 | `order-service/.../OrderEventProducer.java` | 待截图 |
| RocketMQ 消费者 | `message-service/.../OrderEventConsumer.java` | 待截图 |
| Sentinel 配置 | `common/common-core/.../SentinelRuleConfig.java` | 待截图 |

## 路线三：代码测试

| 材料 | 文件或位置 | 状态 |
| --- | --- | --- |
| Postman 集合 | `postman/SpringCloud-Alibaba-Order-System.postman_collection.json` | 已完成 |
| Postman 环境 | `postman/SpringCloud-Alibaba-Order-System.postman_environment.json` | 已完成 |
| JMeter 脚本 | `jmeter/springcloud-order-pressure-test.jmx` | 已完成 |
| JMeter 原始结果 | `jmeter/results.jtl` | 已完成 |
| JMeter HTML 报告 | `jmeter/html-report/index.html` | 已完成 |
| JMeter 结果摘要 | `docs/jmeter-test-result-summary.md` | 已完成 |

## 路线四：代码质量与注释

| 材料 | 位置 | 状态 |
| --- | --- | --- |
| Maven 编译成功截图 | `mvn test -DskipTests` 输出 | 已验证，待截图 |
| 模块结构说明 | `docs/architecture-and-report-material.md` | 已完成 |
| 统一返回结构代码 | `common/common-core/.../ApiResponse.java` | 待截图 |
| 全局异常处理代码 | `common/common-security/.../GlobalExceptionHandler.java` | 待截图 |

## 路线五：文档与报告质量

| 材料 | 文件或位置 | 状态 |
| --- | --- | --- |
| 报告正文材料 | `docs/architecture-and-report-material.md` | 已完成 |
| 测试说明 | `docs/test-execution-guide.md` | 已完成 |
| 压测摘要 | `docs/jmeter-test-result-summary.md` | 已完成 |
| WPS 正式报告 | WPS 文档 | 待排版 |

## 路线六：演示与答辩表现

| 材料 | 文件或位置 | 状态 |
| --- | --- | --- |
| 3-5 分钟讲稿 | `docs/defense-script.md` | 已完成 |
| 高频问题答案 | `docs/defense-script.md` | 已完成 |
| 演示顺序 | `docs/defense-script.md` | 已完成 |
| 备用截图包 | 手动截图目录 | 待整理 |

## 最终提交建议

建议最终提交或展示以下材料：

- 项目源码。
- Postman 集合和环境文件。
- JMeter `.jmx`、`.jtl` 和 HTML 报告。
- WPS 正式报告。
- 答辩讲稿。
- 关键截图文件夹。
