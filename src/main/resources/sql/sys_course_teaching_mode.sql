alter table sys_course
    add column teaching_mode varchar(20) not null default 'ONLINE' comment '上课方式：ONLINE线上，OFFLINE线下' after course_days;
