package cn.yanque.models.teaching.course.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 课程详情实体，对应 sys_course_detail 表。
 */
@Data
public class CourseDetailEntity {

    /** 课程详情ID */
    private Long id;

    /** 所属课程ID */
    private Long courseId;

    /** 阶段名称，例如基础、进阶、项目 */
    private String stageName;

    /** 第几天上课 */
    private Integer dayNumber;

    /** 当天上课内容 */
    private String classContent;

    /** 创建时间 */
    private Date createdAt;

    /** 更新时间 */
    private Date updatedAt;
}
