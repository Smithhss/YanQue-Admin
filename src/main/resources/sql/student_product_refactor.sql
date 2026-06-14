create table if not exists student_product (
    id bigint primary key auto_increment comment '学生产品关系ID',
    student_id bigint not null comment '学生ID',
    product_id varchar(64) not null comment '产品ID',
    source_order_no varchar(64) not null comment '来源支付订单号',
    status varchar(30) not null default 'ACTIVE' comment '状态：ACTIVE启用，INACTIVE停用',
    created_at datetime not null default current_timestamp comment '创建时间',
    updated_at datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    unique key uk_source_order_no (source_order_no),
    key idx_student_id (student_id),
    key idx_product_id (product_id),
    key idx_status (status)
) comment '学生产品关联表';

insert ignore into student_product (student_id, product_id, source_order_no, status, created_at, updated_at)
select id, product_id, source_order_no, status, created_at, updated_at
from student
where product_id is not null
  and product_id <> ''
  and source_order_no is not null
  and source_order_no <> '';

alter table student drop index uk_source_order_no;
alter table student drop index idx_product_id;
alter table student drop column source_order_no;
alter table student drop column product_id;
