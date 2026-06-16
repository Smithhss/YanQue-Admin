create table if not exists exam_question
(
    id               bigint auto_increment primary key,
    question_type    varchar(32)     not null comment '题目类型：SINGLE单选，MULTIPLE多选，JUDGE判断，FILL填空，SHORT简答，PROGRAMMING编程',
    question_content text            not null comment '题干',
    answer_content   text            not null comment '正确答案',
    analysis_content text            null comment '答案解析',
    difficulty       varchar(32)     not null default 'NORMAL' comment '难度：VERY_EASY很简单，EASY简单，NORMAL普通，HARD困难，VERY_HARD很困难',
    status           varchar(32)     not null default 'ENABLED' comment '状态：ENABLED启用，DISABLED停用',
    created_at       datetime        not null comment '创建时间',
    updated_at       datetime        not null comment '更新时间',
    index idx_exam_question_type (question_type),
    index idx_exam_question_status (status),
    index idx_exam_question_difficulty (difficulty)
) comment '题库题目表';

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

create table if not exists exam_question_option
(
    id             bigint auto_increment primary key,
    question_id    bigint       not null comment '题目ID',
    option_key     varchar(16)  not null comment '选项标识：A/B/C/D',
    option_content text         not null comment '选项内容',
    created_at     datetime     not null comment '创建时间',
    updated_at     datetime     not null comment '更新时间',
    unique key uk_exam_question_option_key (question_id, option_key),
    index idx_exam_question_option_question (question_id)
) comment '题目选项表';
