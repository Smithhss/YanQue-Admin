alter table student
    add column teaching_mode varchar(20) not null default 'ONLINE' comment '上课方式：ONLINE线上，OFFLINE线下' after major,
    add column class_id bigint null comment '班级ID，线下班必填' after teaching_mode,
    add index idx_teaching_mode (teaching_mode),
    add index idx_class_id (class_id);
