create table if not exists student_sop (
    id bigint primary key auto_increment comment '学生SOP记录ID',
    student_id bigint not null comment '学生ID',
    mentor_id bigint not null comment '导师用户ID',
    sop_video_object_key varchar(255) null comment 'SOP视频对象Key',
    sop_video_file_name varchar(255) null comment 'SOP视频文件名',
    sop_time datetime null comment 'SOP时间',
    status varchar(30) not null default 'ASSIGNED' comment '状态：ASSIGNED已分配，COMPLETED已完成，CANCELED已取消',
    remark varchar(500) null comment '备注',
    created_at datetime not null default current_timestamp comment '创建时间',
    updated_at datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    key idx_student_status (student_id, status),
    key idx_mentor_id (mentor_id),
    key idx_sop_time (sop_time)
) comment '学生入学SOP记录表';
