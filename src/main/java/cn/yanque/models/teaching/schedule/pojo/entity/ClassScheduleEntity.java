package cn.yanque.models.teaching.schedule.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * 班级课表实体,对应 sys_class_schedule 表。
 */
@Data
public class ClassScheduleEntity {

    /** 课表ID */
    private Long id;

    /** 班级ID */
    private Long classId;

    /** 老师用户ID,生成课表后再分配 */
    private Long teacherId;

    /** 上课日期 */
    private Date scheduleDate;

    /** 课程详情ID,自习和放假可为空 */
    private Long courseDetailId;

    /** 课程内容快照 */
    private String courseContent;

    /** 上课类型:CLASS,SELF_STUDY,HOLIDAY */
    private String classType;

    /** 创建时间 */
    private Date createdAt;

    /** 更新时间 */
    private Date updatedAt;
}
