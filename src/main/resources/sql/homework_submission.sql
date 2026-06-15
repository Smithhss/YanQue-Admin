create table if not exists homework_submission (
    id bigint primary key auto_increment comment '作业提交ID',
    homework_id bigint not null comment '作业ID',
    student_id bigint not null comment '学生ID',
    class_id bigint not null comment '班级ID',
    content_object_key varchar(500) not null comment '提交内容对象存储Key',
    content_file_name varchar(255) not null comment '提交内容文件名',
    submit_time datetime not null comment '提交时间',
    late_submitted tinyint(1) not null default 0 comment '是否逾期提交',
    teacher_remark varchar(500) null comment '老师批注',
    score int null comment '分数',
    created_at datetime not null default current_timestamp comment '创建时间',
    updated_at datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    unique key uk_homework_student (homework_id, student_id),
    key idx_student_id (student_id),
    key idx_class_id (class_id),
    key idx_submit_time (submit_time)
) comment '作业提交表';
