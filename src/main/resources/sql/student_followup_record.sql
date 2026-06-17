create table if not exists student_followup_record
(
    id bigint primary key auto_increment comment '主键ID',
    student_id bigint not null comment '学生ID',
    learning_plan_id bigint null comment '线上学习计划ID，历史兼容字段',
    student_tag varchar(64) not null comment '生成时学生标签快照',
    followup_tag_id bigint not null comment '回访标签配置ID',
    enroll_date date not null comment '线上入学日期',
    last_followup_time datetime null comment '生成时上一条回访时间',
    due_date date not null comment '应回访日期',
    followup_interval_days int not null comment '生成时回访间隔天数快照',
    status varchar(32) not null default 'NEED_FOLLOWUP' comment '状态：NEED_FOLLOWUP需要回访，FOLLOWED已回访，CANCELED已取消',
    followup_user_id bigint null comment '回访人ID',
    followup_time datetime null comment '回访时间',
    followup_content varchar(2000) null comment '回访内容',
    followup_video_object_key varchar(512) null comment '回访会议视频对象Key',
    followup_video_file_name varchar(255) null comment '回访会议视频文件名',
    remark varchar(512) null comment '备注',
    created_at datetime not null default current_timestamp comment '创建时间',
    updated_at datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    unique key uk_student_followup_due (student_id, due_date),
    key idx_student_followup_due_status (due_date, status),
    key idx_student_followup_student (student_id),
    key idx_student_followup_tag (student_tag),
    key idx_student_followup_user (followup_user_id)
) comment '学生回访记录表';

alter table student_followup_record
    modify column learning_plan_id bigint null comment '线上学习计划ID，历史兼容字段';
