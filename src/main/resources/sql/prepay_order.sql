create table if not exists prepay_order (
    id bigint primary key auto_increment comment '预支付订单ID',
    order_no varchar(32) not null comment '订单号',
    student_name varchar(50) not null comment '学生姓名',
    student_phone varchar(30) not null comment '手机号',
    product_id bigint not null comment '产品ID',
    product_amount decimal(10, 2) not null comment '产品金额',
    discount_amount decimal(10, 2) not null default 0.00 comment '优惠金额',
    order_status varchar(30) not null default 'PENDING_PAYMENT' comment '订单状态：PENDING_PAYMENT待支付，SUCCESS已支付，CANCELED已取消',
    created_at datetime not null default current_timestamp comment '创建时间',
    updated_at datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    unique key uk_order_no (order_no),
    key idx_student_phone (student_phone),
    key idx_product_id (product_id),
    key idx_order_status (order_status)
) comment '预支付订单表';
