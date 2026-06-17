package cn.yanque.models.teaching.course.pojo.bo;

import lombok.Data;

/**
 * 课程作业标准查询条件。
 * <p>
 * Mapper 层只接收 BO，避免把 Controller 请求对象传进 SQL 层。
 */
@Data
public class QueryCourseHomeworkTemplateBo {

    private Long courseId;

    private String teachingMode;

    private String stageName;

    private Integer dayNumber;

    private String status;

    private Long excludeId;
}
