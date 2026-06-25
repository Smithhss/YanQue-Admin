-- F014 线下学生宿舍管理：楼栋 / 房间 / 床位 / 入住记录
-- 层级：sys_campus(已有) → dorm_building → dorm_room → dorm_bed → student(已有)
-- 业务约束（性别隔离 / 一人一床 / 床位唯一占用）在 Service 层强校验，
-- 因历史入住记录会重复 student_id，无法用唯一索引表达"当前唯一在住"。

create table if not exists dorm_building (
    id bigint primary key auto_increment comment '楼栋ID',
    campus_id bigint not null comment '所属校区ID（sys_campus.id）',
    building_name varchar(64) not null comment '楼栋名称，如"1号楼"',
    gender_type varchar(10) not null comment '性别类型：MALE男寝/FEMALE女寝',
    manager_name varchar(32) null comment '宿管姓名',
    manager_phone varchar(20) null comment '宿管电话',
    status varchar(20) not null default 'ENABLED' comment '状态：ENABLED启用/DISABLED停用',
    created_at datetime not null default current_timestamp comment '创建时间',
    updated_at datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    key idx_campus_id (campus_id)
) comment '宿舍楼栋表';

create table if not exists dorm_room (
    id bigint primary key auto_increment comment '房间ID',
    building_id bigint not null comment '所属楼栋ID（dorm_building.id）',
    room_no varchar(32) not null comment '房间号，如"301"',
    floor int not null default 1 comment '楼层',
    capacity int not null comment '床位容量',
    room_type varchar(20) null comment '房型：FOUR四人间/SIX六人间等',
    status varchar(20) not null default 'ENABLED' comment '状态：ENABLED启用/DISABLED停用/MAINTENANCE维修',
    created_at datetime not null default current_timestamp comment '创建时间',
    updated_at datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    unique key uk_building_room (building_id, room_no),
    key idx_building_id (building_id)
) comment '宿舍房间表';

create table if not exists dorm_bed (
    id bigint primary key auto_increment comment '床位ID',
    room_id bigint not null comment '所属房间ID（dorm_room.id）',
    bed_no varchar(16) not null comment '床位号，如"A"或"1"',
    status varchar(20) not null default 'FREE' comment '状态：FREE空闲/OCCUPIED占用/LOCKED锁定',
    current_student_id bigint null comment '当前入住学生ID（student.id），空闲时为null',
    created_at datetime not null default current_timestamp comment '创建时间',
    updated_at datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    unique key uk_room_bed (room_id, bed_no),
    key idx_room_id (room_id),
    key idx_current_student_id (current_student_id)
) comment '宿舍床位表';

create table if not exists dorm_assignment (
    id bigint primary key auto_increment comment '入住记录ID',
    student_id bigint not null comment '学生ID（student.id）',
    bed_id bigint not null comment '床位ID（dorm_bed.id）',
    room_id bigint not null comment '房间ID（冗余，便于查询）',
    building_id bigint not null comment '楼栋ID（冗余，便于查询）',
    check_in_date date not null comment '入住日期',
    check_out_date date null comment '退宿日期，在住时为null',
    status varchar(20) not null default 'LIVING' comment '状态：LIVING在住/CHECKED_OUT已退宿',
    assigned_by bigint not null comment '分配操作人（管理端用户ID）',
    remark varchar(255) null comment '备注',
    created_at datetime not null default current_timestamp comment '创建时间',
    updated_at datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    key idx_student_id (student_id),
    key idx_bed_id (bed_id),
    key idx_status (status)
) comment '宿舍入住记录表';
