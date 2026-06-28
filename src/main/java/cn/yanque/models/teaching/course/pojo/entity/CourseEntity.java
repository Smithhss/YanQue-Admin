package cn.yanque.models.teaching.course.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * 课程实体,对应 sys_course 表。
 */
@Data
public class CourseEntity {

    /** 课程ID */
    private Long id;

    /** 课程名称 */
    private String courseName;

    /** 课程天数 */
    private Integer courseDays;

    /** 上课方式:ONLINE线上,OFFLINE线下 */
    private String teachingMode;

    /** 课程资料路径 */
    private String materialPath;

    /** 创建时间 */
    private Date createdAt;

    /** 更新时间 */
    private Date updatedAt;
}
