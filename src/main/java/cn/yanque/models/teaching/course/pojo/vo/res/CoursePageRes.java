package cn.yanque.models.teaching.course.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 课程分页列表响应对象。
 */
@Data
@Schema(description = "课程分页响应")
public class CoursePageRes {

    /** 课程ID */
    @Schema(description = "课程ID")
    private Long id;

    /** 课程名称 */
    @Schema(description = "课程名称")
    private String courseName;

    /** 课程总天数 */
    @Schema(description = "课程天数")
    private Integer courseDays;

    /** 上课方式 */
    @Schema(description = "上课方式：ONLINE线上，OFFLINE线下")
    private String teachingMode;

    /** 课程资料路径 */
    @Schema(description = "资料路径")
    private String materialPath;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private Date createdAt;
}
