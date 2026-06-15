alter table homework
    add column answer_object_key varchar(500) null comment '答案对象存储Key' after content_file_name,
    add column answer_file_name varchar(255) null comment '答案文件名' after answer_object_key,
    add column answer_student_visible tinyint(1) not null default 0 comment '答案学生是否可见' after answer_file_name;
