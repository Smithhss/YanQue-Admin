create table if not exists sys_class_schedule (
    id bigint primary key auto_increment comment '课表ID',
    class_id bigint not null comment '班级ID',
    teacher_id bigint null comment '老师ID',
    schedule_date date not null comment '上课日期',
    course_detail_id bigint null comment '课程详情ID',
    course_content varchar(1000) not null comment '课程内容',
    class_type varchar(32) not null comment '上课类型：CLASS/SELF_STUDY/HOLIDAY',
    created_at datetime not null default current_timestamp comment '创建时间',
    updated_at datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    unique key uk_class_date (class_id, schedule_date),
    index idx_teacher_id (teacher_id),
    index idx_course_detail_id (course_detail_id)
) comment '班级课表表';
