create table if not exists student_followup_tag
(
    id bigint primary key auto_increment comment '主键ID',
    student_tag varchar(64) not null comment '学生标签',
    followup_interval_days int not null comment '回访间隔天数',
    status varchar(32) not null default 'ACTIVE' comment '状态：ACTIVE启用，INACTIVE停用',
    remark varchar(512) null comment '备注',
    created_at datetime not null default current_timestamp comment '创建时间',
    updated_at datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    unique key uk_student_followup_tag_student_tag (student_tag)
) comment '学生回访标签配置表';
