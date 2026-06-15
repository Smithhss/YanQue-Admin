create table if not exists order_payment (
    id bigint primary key auto_increment comment '支付订单ID',
    order_no varchar(64) not null comment '支付订单号',
    student_phone varchar(30) not null comment '学生手机号',
    student_name varchar(50) not null comment '学生姓名',
    product_id varchar(64) not null comment '产品ID',
    order_amount decimal(10, 2) not null comment '订单支付金额',
    refunded_amount decimal(10, 2) not null default 0.00 comment '已申请退款金额，包含退款处理中和退款成功金额',
    prepay_order_no varchar(32) not null comment '预支付订单号',
    status varchar(30) not null comment '支付订单状态',
    unique_order_no varchar(128) null comment '支付渠道唯一订单号',
    pay_success_time datetime null comment '支付成功时间',
    created_at datetime not null default current_timestamp comment '创建时间',
    updated_at datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    unique key uk_order_no (order_no),
    key idx_student_phone (student_phone),
    key idx_prepay_order_no (prepay_order_no),
    key idx_status (status)
) comment '支付订单表';
