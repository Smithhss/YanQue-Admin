create table if not exists class_attendance (
    id bigint primary key auto_increment comment '考勤记录ID',
    schedule_id bigint not null comment '课次ID（sys_class_schedule.id）',
    class_id bigint not null comment '班级ID（冗余，便于按班统计）',
    student_id bigint not null comment '学生ID',
    schedule_date date not null comment '上课日期（冗余自课次，便于按日期查询）',
    status varchar(20) not null comment '考勤状态：PRESENT出勤/LATE迟到/LEAVE请假/ABSENT旷课',
    leave_reason varchar(255) null comment '请假/备注原因',
    hour_deducted decimal(5,1) not null default 0 comment '本次扣减课时（快照，用于改点名时回滚）',
    operator_id bigint not null comment '点名操作人（管理端用户ID）',
    created_at datetime not null default current_timestamp comment '创建时间',
    updated_at datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    unique key uk_schedule_student (schedule_id, student_id),
    key idx_class_id (class_id),
    key idx_student_id (student_id),
    key idx_schedule_date (schedule_date)
) comment '班级考勤记录表';
