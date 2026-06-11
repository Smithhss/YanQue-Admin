package cn.yanque.models.teaching.clazz.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * 班级实体，对应 sys_class 表。
 */
@Data
public class ClazzEntity {

    /** 班级ID */
    private Long id;

    /** 班级期数 */
    private String classPeriod;

    /** 班主任用户ID */
    private Long headTeacherId;

    /** 校区ID */
    private Long campusId;

    /** 课程ID */
    private Long courseId;

    /** 创建时间 */
    private Date createdAt;

    /** 更新时间 */
    private Date updatedAt;
}
