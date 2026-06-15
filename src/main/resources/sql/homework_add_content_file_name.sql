alter table homework
    add column content_file_name varchar(255) null comment '作业内容文件名' after content_object_key;

update homework
set content_file_name = substring_index(content_object_key, '/', -1)
where content_file_name is null;

alter table homework
    modify column content_file_name varchar(255) not null comment '作业内容文件名';
