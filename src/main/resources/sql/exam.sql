-- 考试安排表。
create table if not exists exam (
    id bigint primary key auto_increment comment '考试ID',
    paper_id bigint not null comment '试卷ID',
    class_id bigint not null comment '班级ID',
    start_time datetime not null comment '可进入考试开始时间',
    end_time datetime not null comment '可进入考试截止时间',
    duration_minutes int not null comment '学生个人答题时长，单位分钟',
    invigilator_user_id bigint not null comment '监考老师用户ID',
    answer_visible tinyint(1) not null default 0 comment '是否向学生公布答卷判定结果',
    created_at datetime not null comment '创建时间',
    updated_at datetime not null comment '更新时间',
    index idx_exam_paper (paper_id),
    index idx_exam_class_time (class_id, start_time, end_time),
    index idx_exam_invigilator_user (invigilator_user_id)
) comment '考试安排表';

-- 学生考试记录表。学生点“开始考试”时创建，后续答题、提交、评分都挂在这条记录下。
create table if not exists student_exam_record (
    id bigint primary key auto_increment comment '学生考试记录ID',
    exam_id bigint not null comment '考试ID',
    student_id bigint not null comment '学生ID',
    start_time datetime not null comment '实际开始答题时间',
    deadline_time datetime not null comment '当前学生本次考试截止时间',
    submit_time datetime null comment '提交时间',
    status varchar(32) not null comment '状态：IN_PROGRESS/SUBMITTED/TIMEOUT',
    grading_status varchar(32) not null default 'PENDING' comment '批改状态：PENDING/GRADING/COMPLETED',
    score decimal(10, 2) null comment '得分',
    created_at datetime not null comment '创建时间',
    updated_at datetime not null comment '更新时间',
    unique key uk_exam_student (exam_id, student_id),
    index idx_student_exam_record_student (student_id, status),
    index idx_student_exam_record_exam (exam_id)
) comment '学生考试记录表';

-- 学生考试答案表。一题一行，便于后续批改、统计正确率和导出。
create table if not exists student_exam_answer (
    id bigint primary key auto_increment comment '学生考试答案ID',
    record_id bigint not null comment '学生考试记录ID',
    exam_id bigint not null comment '考试ID',
    paper_id bigint not null comment '试卷ID',
    question_id bigint not null comment '题目ID',
    question_type varchar(32) not null comment '题型',
    question_score decimal(10, 2) not null comment '题目分数',
    answer_content text null comment '学生答案内容',
    correct tinyint(1) null comment '是否正确，主观题/编程题可为空',
    score decimal(10, 2) null comment '本题得分',
    created_at datetime not null comment '创建时间',
    updated_at datetime not null comment '更新时间',
    unique key uk_record_question (record_id, question_id),
    index idx_student_exam_answer_record (record_id),
    index idx_student_exam_answer_exam (exam_id),
    index idx_student_exam_answer_question (question_id)
) comment '学生考试答案表';

-- 如果之前已经执行过旧版 exam.sql，请手动执行下面迁移，再按实际数据回填监考老师用户ID：
-- alter table exam add column invigilator_user_id bigint null comment '监考老师用户ID' after duration_minutes;
-- update exam set invigilator_user_id = 1 where invigilator_user_id is null;
-- alter table exam modify invigilator_user_id bigint not null;
-- alter table exam drop column invigilator;
-- alter table exam add index idx_exam_invigilator_user (invigilator_user_id);
-- alter table exam add column answer_visible tinyint(1) not null default 0 comment '是否向学生公布答卷判定结果' after invigilator_user_id;
-- 如果 student_exam_record 已经存在，请执行：
-- alter table student_exam_record add column grading_status varchar(32) not null default 'PENDING' comment '批改状态：PENDING/GRADING/COMPLETED' after status;
-- update student_exam_record set grading_status = 'COMPLETED' where status = 'SUBMITTED' and score is not null;
