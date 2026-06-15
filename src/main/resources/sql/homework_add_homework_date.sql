alter table homework
    add column homework_date date null comment '作业日期' after class_id;

update homework
set homework_date = date(start_time)
where homework_date is null;

alter table homework
    modify column homework_date date not null comment '作业日期';

alter table homework
    add unique key uk_class_homework_date (class_id, homework_date);

alter table homework
    add key idx_homework_date (homework_date);
