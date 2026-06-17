alter table student_followup_record
    add column followup_video_object_key varchar(512) null comment '回访会议视频对象Key' after followup_content,
    add column followup_video_file_name varchar(255) null comment '回访会议视频文件名' after followup_video_object_key;
