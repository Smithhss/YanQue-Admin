alter table student_followup_record
    add column last_followup_time datetime null comment '生成时上一条回访时间' after enroll_date;
