create table if not exists sys_class (
    id bigint primary key auto_increment comment '班级ID',
    class_period varchar(64) not null comment '班级期数',
    head_teacher_id bigint not null comment '班主任ID',
    campus_id bigint not null comment '校区ID',
    course_id bigint not null comment '课程ID',
    created_at datetime not null default current_timestamp comment '创建时间',
    updated_at datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    index idx_head_teacher_id (head_teacher_id),
    index idx_campus_id (campus_id),
    index idx_course_id (course_id)
) comment '班级表';
