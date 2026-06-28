# YanQue-Admin 开发指南

## 技术栈
- Spring Boot 3.2.0 / Java 17 / MyBatis / MySQL 8.0 / Redis 7
- 支付: 支付宝沙箱(JDK原生HTTP), 易宝(mock/SDK)
- TOS: 火山引擎对象存储

## 架构约定
- 状态字段: 统一使用 String + Enum.name(), 不允许硬编码字符串
- Mapper 更新/删除: 必须检查返回 rows, rows==0 抛 BusinessException
- Mapper XML 关键更新: WHERE 子句须包含旧状态/旧值做 CAS 守卫
- 分页: PageHelper + 自算 pageNum/pageSize 默认值
- 异常: 统一抛 BusinessException 静态实例或 .newInstance()
- Controller 路由: 避免 {id} 路径变量与固定路径冲突, 冲突时拆分子资源 Controller

## 状态枚举清单
| 枚举 | 用途 | 值 |
|------|------|---|
| EnableStatusEnum | 通用启停 | ENABLED, DISABLED |
| ActiveEnum | 活跃状态 | ACTIVE |
| OrderStatusEnum | 支付订单 | INIT, FAIL, PROCESSING, SUCCESS, TIMEOUT |
| PrepayOrderStatusEnum | 预付单 | PENDING_PAYMENT, SUCCESS, CANCELED |
| DormBedStatusEnum | 床位 | FREE, OCCUPIED |
| DormAssignmentStatusEnum | 入住 | LIVING, CHECKED_OUT |
| TeachingModeEnum | 授课模式 | ONLINE, OFFLINE |
| ExamStatusEnum | 考试状态 | NOT_STARTED, IN_PROGRESS, SUBMITTED |
| GradingStatusEnum | 批改状态 | PENDING, COMPLETED |

## 数据库
- SQL 脚本: src/main/resources/sql/ (手动维护, 无 Flyway)
- 初始化: yq-deploy/db-init/init-db.sh 分三阶段执行
- RBAC 核心表: 由 yq-deploy/db-init/00_core_schema.sql 重建
- Schema 修复: src/main/resources/sql/fix_schema_inconsistencies.sql

## 支付流程
- prepay_order(PENDING_PAYMENT→SUCCESS) → order_payment(INIT→PROCESSING→SUCCESS)
- 异步回调验签: RSA2 (SHA256withRSA)
- 同步Return: /alipay/return 后端验签后重定向前端
- 超时处理: 15分钟→trade query→确认未付→标记TIMEOUT→close trade
- 学生端签名: HMAC-SHA256 + JWT + Redis nonce

## CI/CD
- 部署: yq-deploy 仓库 push main → GitHub Actions 构建→GHCR→SSH pull+up
- 镜像: ghcr.io/smithhss/yanque-admin, yanque-admin-web, yanque-student-web
