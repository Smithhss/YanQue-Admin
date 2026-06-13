alter table order_product
    add column teaching_mode varchar(20) not null default 'OFFLINE' comment '上课方式：ONLINE线上，OFFLINE线下' after course_content;
