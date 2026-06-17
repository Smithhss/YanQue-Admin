create table if not exists student_learning_plan (
    id bigint primary key auto_increment comment '线上学习计划ID',
    student_id bigint not null comment '学生ID',
    course_id bigint not null comment '课程ID',
    sop_id bigint not null comment 'SOP记录ID',
    start_date date not null comment '开始学习日期',
    status varchar(20) not null default 'ACTIVE' comment '状态：ACTIVE生效，CANCELED取消',
    created_at datetime not null default current_timestamp comment '创建时间',
    updated_at datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    key idx_student_learning_plan_student (student_id),
    key idx_student_learning_plan_course (course_id),
    key idx_student_learning_plan_sop (sop_id),
    key idx_student_learning_plan_status (status)
) comment '线上学员学习计划表';

create table if not exists student_learning_calendar (
    id bigint primary key auto_increment comment '学习日历ID',
    plan_id bigint not null comment '学习计划ID',
    student_id bigint not null comment '学生ID',
    study_date date not null comment '学习日期',
    stage_name varchar(128) not null comment '阶段名称',
    day_index int not null comment '计划总第几天',
    stage_day_index int not null comment '阶段第几天',
    status varchar(20) not null default 'TODO' comment '状态：TODO待学习，DONE已完成',
    created_at datetime not null default current_timestamp comment '创建时间',
    updated_at datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    unique key uk_learning_calendar_plan_date (plan_id, study_date),
    key idx_learning_calendar_student (student_id),
    key idx_learning_calendar_plan (plan_id)
) comment '线上学员每日学习日历表';
