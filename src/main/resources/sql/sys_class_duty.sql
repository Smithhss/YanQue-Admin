create table if not exists sys_class_duty
(
    id bigint primary key auto_increment comment '值班ID',
    class_id bigint null comment '班级ID，校区统一值班为空',
    campus_id bigint not null comment '校区ID',
    teacher_id bigint not null comment '老师ID',
    duty_date date not null comment '值班日期',
    duty_type varchar(50) not null comment '值班类型：EVENING_STUDY_CLASS/EVENING_STUDY_CAMPUS/SELF_STUDY_CLASS',
    start_time varchar(10) not null comment '开始时间',
    end_time varchar(10) not null comment '结束时间',
    remark varchar(255) null comment '备注',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    index idx_class_date (class_id, duty_date),
    index idx_campus_date (campus_id, duty_date),
    index idx_teacher_date (teacher_id, duty_date),
    index idx_duty_type (duty_type)
) comment='值班表';
