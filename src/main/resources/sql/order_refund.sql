create table if not exists order_refund (
    id bigint primary key auto_increment comment '退款订单ID',
    refund_order_no varchar(64) not null comment '退款订单号',
    payment_order_no varchar(64) not null comment '原支付订单号',
    payment_amount decimal(10, 2) not null comment '原支付金额',
    refund_amount decimal(10, 2) null comment '退款金额',
    status varchar(30) not null comment '退款状态：INIT初始化，PROCESSING处理中，SUCCESS成功，FAIL失败，CLOSED关闭',
    reason varchar(200) null comment '退款原因',
    unique_refund_no varchar(128) null comment '易宝退款流水号',
    fail_reason varchar(500) null comment '失败原因',
    refund_success_time datetime null comment '退款成功时间',
    created_at datetime not null default current_timestamp comment '创建时间',
    updated_at datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    unique key uk_refund_order_no (refund_order_no),
    key idx_payment_order_no (payment_order_no),
    key idx_status (status)
) comment '退款订单表';
