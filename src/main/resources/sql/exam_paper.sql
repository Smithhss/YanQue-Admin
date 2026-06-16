-- 试卷表。
create table if not exists exam_paper (
    id bigint primary key auto_increment comment '试卷ID',
    paper_name varchar(128) not null comment '试卷名称',
    course_id bigint not null comment '课程ID',
    stage_name varchar(64) null comment '阶段名称，为空表示整门课程考试',
    total_score decimal(10, 1) not null comment '总分数',
    created_at datetime not null comment '创建时间',
    updated_at datetime not null comment '更新时间',
    index idx_exam_paper_course_stage (course_id, stage_name),
    index idx_exam_paper_name (paper_name)
) comment '试卷表';

-- 试卷题目关联表。
create table if not exists exam_paper_question (
    id bigint primary key auto_increment comment '关联ID',
    paper_id bigint not null comment '试卷ID',
    question_id bigint not null comment '题目ID',
    question_score decimal(10, 1) not null comment '题目分数',
    created_at datetime not null comment '创建时间',
    updated_at datetime not null comment '更新时间',
    unique key uk_exam_paper_question (paper_id, question_id),
    index idx_exam_paper_question_paper (paper_id),
    index idx_exam_paper_question_question (question_id)
) comment '试卷题目关联表';
