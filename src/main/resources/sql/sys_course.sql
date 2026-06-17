create table if not exists sys_course (
    id bigint primary key auto_increment comment '课程ID',
    course_name varchar(128) not null comment '课程名称',
    course_days int not null comment '课程天数',
    teaching_mode varchar(20) not null default 'ONLINE' comment '上课方式：ONLINE线上，OFFLINE线下',
    material_path varchar(500) not null comment '资料路径',
    created_at datetime not null default current_timestamp comment '创建时间',
    updated_at datetime not null default current_timestamp on update current_timestamp comment '更新时间'
) comment '课程表';
