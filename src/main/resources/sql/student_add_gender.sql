-- F014：学生表增加性别字段
-- 用途：宿舍按性别隔离楼栋（男生→男寝/女生→女寝），分配时校验。
-- 历史数据 gender 为 null，由资料完善流程（completeProfile）逐步补全。
alter table student
    add column gender varchar(10) null comment '性别：MALE男/FEMALE女（F014宿舍分配用）' after student_phone;
