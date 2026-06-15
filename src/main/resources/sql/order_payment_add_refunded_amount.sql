alter table order_payment
    add column refunded_amount decimal(10, 2) not null default 0.00 comment '已申请退款金额，包含退款处理中和退款成功金额' after order_amount;
