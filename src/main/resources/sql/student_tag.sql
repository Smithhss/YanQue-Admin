alter table student
    add column student_tag varchar(50) null comment '学生标签' after class_id;

create index idx_student_tag on student (student_tag);

insert into sys_config (k, v)
values ('student.tag.options', '普通学员,摆烂学员,失联学员,已就业学员,高潜力学员,持续关注学员')
on duplicate key update
    v = values(v);
