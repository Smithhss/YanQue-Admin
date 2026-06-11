create table if not exists sys_campus (
    id bigint primary key auto_increment comment '校区ID',
    campus_location varchar(255) not null comment '校区地点',
    manager_name varchar(64) not null comment '负责人',
    manager_phone varchar(32) not null comment '负责人电话',
    created_at datetime not null default current_timestamp comment '创建时间',
    updated_at datetime not null default current_timestamp on update current_timestamp comment '更新时间'
) comment '校区表';
