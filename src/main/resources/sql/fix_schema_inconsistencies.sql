-- =====================================================================
-- 修复数据库 Schema 不一致问题
-- 执行顺序: 在 init-db.sh 三阶段完成后运行，或独立执行
-- 幂等: 重复执行不影响已有数据（列已存在会报错，属正常可忽略）
-- =====================================================================

-- 1. prepay_order.order_status 注释修正: PAID -> SUCCESS
ALTER TABLE prepay_order
    MODIFY order_status varchar(30) NOT NULL DEFAULT 'PENDING_PAYMENT'
    COMMENT '订单状态: PENDING_PAYMENT待支付, SUCCESS已支付, CANCELED已取消';

-- 2. order_payment.product_id 类型统一: varchar(64) -> bigint
-- 注意: 如果表中已有非数字数据, 需先清理
ALTER TABLE order_payment
    MODIFY product_id bigint NOT NULL COMMENT '产品ID';

-- 3. exam 相关表 created_at/updated_at 添加 DEFAULT CURRENT_TIMESTAMP
ALTER TABLE exam
    MODIFY created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    MODIFY updated_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间';

ALTER TABLE exam_paper
    MODIFY created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    MODIFY updated_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间';

ALTER TABLE exam_question
    MODIFY created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    MODIFY updated_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间';

ALTER TABLE exam_question_option
    MODIFY created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    MODIFY updated_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间';

ALTER TABLE student_exam_record
    MODIFY created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    MODIFY updated_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间';

ALTER TABLE student_exam_answer
    MODIFY created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    MODIFY updated_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间';

ALTER TABLE exam_question_course
    MODIFY created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间';

-- 4. dorm_assignment 添加唯一约束防重复入住
-- 使用函数索引方式: MySQL 8.0 不支持条件唯一索引, 此处对 (student_id) 加索引供业务查询
-- 重复入住防护由 Service 层保证
ALTER TABLE dorm_assignment
    ADD INDEX idx_student_id (student_id);
