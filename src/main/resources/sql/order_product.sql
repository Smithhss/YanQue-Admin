create table if not exists order_product (
    id bigint primary key auto_increment comment '产品ID',
    course_content varchar(1000) not null comment '课程内容',
    teaching_mode varchar(20) not null comment '上课方式：ONLINE线上，OFFLINE线下',
    price decimal(10, 2) not null comment '价格',
    created_at datetime not null default current_timestamp comment '创建时间',
    updated_at datetime not null default current_timestamp on update current_timestamp comment '更新时间'
) comment '订单产品表';
