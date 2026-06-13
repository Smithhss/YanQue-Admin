alter table order_payment
    add column created_at datetime not null default current_timestamp comment '创建时间' after pay_success_time,
    add column updated_at datetime not null default current_timestamp on update current_timestamp comment '更新时间' after created_at;
