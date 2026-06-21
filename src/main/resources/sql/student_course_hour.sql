create table if not exists student_course_hour (
    id bigint primary key auto_increment comment '课时账户ID',
    student_id bigint not null comment '学生ID',
    total_hours decimal(7,1) not null default 0 comment '累计获得课时',
    used_hours decimal(7,1) not null default 0 comment '已消耗课时',
    remaining_hours decimal(7,1) not null default 0 comment '剩余课时（可为负，表示欠课时）',
    created_at datetime not null default current_timestamp comment '创建时间',
    updated_at datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    unique key uk_student_id (student_id)
) comment '学生课时账户表';
