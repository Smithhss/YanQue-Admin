create table if not exists student_course_hour_log (
    id bigint primary key auto_increment comment '课时流水ID',
    student_id bigint not null comment '学生ID',
    change_type varchar(20) not null comment '变动类型：CONSUME消耗/ADJUST调整/REVERT回滚',
    change_hours decimal(7,1) not null comment '变动课时（正为增加，负为减少）',
    remaining_after decimal(7,1) not null comment '变动后剩余课时（快照）',
    schedule_id bigint null comment '关联课次ID（消耗/回滚时记录）',
    reason varchar(255) null comment '变动原因（手动调整时填写）',
    operator_id bigint not null comment '操作人（管理端用户ID）',
    created_at datetime not null default current_timestamp comment '创建时间',
    key idx_student_id (student_id),
    key idx_schedule_id (schedule_id)
) comment '学生课时流水表';
