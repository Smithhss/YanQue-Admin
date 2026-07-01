create table if not exists ai_chat_session (
    id bigint primary key auto_increment comment '主键ID',
    student_id bigint not null comment '学生ID',
    title varchar(100) default null comment '会话标题',
    status varchar(20) not null default 'ACTIVE' comment '状态：ACTIVE 正常，DELETED 删除',
    created_at datetime not null default current_timestamp comment '创建时间',
    updated_at datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    key idx_student_status_updated (student_id, status, updated_at)
) comment 'AI问答会话表';

create table if not exists ai_chat_message (
    id bigint primary key auto_increment comment '主键ID',
    session_id bigint not null comment '会话ID',
    role varchar(20) not null comment '角色：user 用户，assistant AI',
    content text not null comment '消息内容',
    model varchar(100) default null comment '模型名称',
    tokens int default null comment '消耗Token数量',
    created_at datetime not null default current_timestamp comment '创建时间',
    key idx_session_id (session_id),
    key idx_session_created (session_id, created_at)
) comment 'AI问答消息表';
