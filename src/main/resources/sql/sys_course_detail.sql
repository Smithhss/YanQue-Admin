create table if not exists sys_course_detail (
    id bigint primary key auto_increment comment '课程详情ID',
    course_id bigint not null comment '课程ID',
    stage_name varchar(128) not null comment '阶段',
    day_number int null comment '第几天，线下课程使用',
    class_content varchar(1000) null comment '上课内容，线下课程使用',
    created_at datetime not null default current_timestamp comment '创建时间',
    updated_at datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    index idx_course_id (course_id)
) comment '课程详情表';
