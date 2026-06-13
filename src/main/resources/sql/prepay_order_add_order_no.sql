alter table prepay_order
    add column order_no varchar(32) not null default '' comment '订单号' after id;

update prepay_order
set order_no = concat('YQ', date_format(created_at, '%Y%m%d%H%i%s'), lpad(id, 6, '0'))
where order_no = '';

alter table prepay_order
    modify column order_no varchar(32) not null comment '订单号';

alter table prepay_order
    add unique key uk_order_no (order_no);
