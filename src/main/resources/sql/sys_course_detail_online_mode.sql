alter table sys_course_detail
    modify column day_number int null comment '第几天，线下课程使用',
    modify column class_content varchar(1000) null comment '上课内容，线下课程使用';

update sys_course_detail d
join sys_course c on d.course_id = c.id
set d.day_number = null,
    d.class_content = null
where c.teaching_mode = 'ONLINE';
