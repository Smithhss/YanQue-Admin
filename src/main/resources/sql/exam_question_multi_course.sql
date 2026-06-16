create table if not exists exam_question_course
(
    question_id bigint   not null comment '题目ID',
    course_id   bigint   not null comment '课程ID',
    stage_name  varchar(128) not null comment '阶段名称',
    created_at  datetime not null comment '创建时间',
    primary key (question_id, course_id, stage_name),
    index idx_exam_question_course_course (course_id),
    index idx_exam_question_course_stage (stage_name)
) comment '题目关联课程阶段表';

-- 如果已经执行过上一版 exam_question_course（没有 stage_name），需要先按实际数据库情况迁移或重建关系表。
