create table if not exists course_homework_template (
    id bigint primary key auto_increment comment '课程作业标准ID',
    course_id bigint not null comment '课程ID',
    teaching_mode varchar(20) not null comment '上课方式：ONLINE线上，OFFLINE线下',
    stage_name varchar(64) null comment '阶段名称，线上课程使用',
    day_number int null comment '第几天，线下课程使用',
    content_object_key varchar(500) not null comment '作业标准文档对象Key',
    content_file_name varchar(255) not null comment '作业标准文档文件名',
    status varchar(20) not null default 'ACTIVE' comment '状态：ACTIVE生效，INACTIVE失效',
    remark varchar(500) null comment '备注',
    created_at datetime not null default current_timestamp comment '创建时间',
    updated_at datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    unique key uk_course_stage (course_id, teaching_mode, stage_name),
    unique key uk_course_day (course_id, teaching_mode, day_number),
    key idx_course_homework_template_course (course_id),
    key idx_course_homework_template_status (status)
) comment '课程作业标准/训练集表';
