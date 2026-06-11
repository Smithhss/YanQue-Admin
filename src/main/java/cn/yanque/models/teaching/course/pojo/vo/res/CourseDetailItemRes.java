package cn.yanque.models.teaching.course.pojo.vo.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 课程详情列表项响应对象。
 */
@Data
@Schema(description = "课程详情响应")
public class CourseDetailItemRes {

    /** 课程详情ID */
    @Schema(description = "课程详情ID")
    private Long id;

    /** 所属课程ID */
    @Schema(description = "课程ID")
    private Long courseId;

    /** 阶段名称 */
    @Schema(description = "阶段")
    private String stageName;

    /** 第几天 */
    @Schema(description = "第几天")
    private Integer dayNumber;

    /** 上课内容 */
    @Schema(description = "上课内容")
    private String classContent;

    /** 创建时间 */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createdAt;

    /** 更新时间 */
    @Schema(description = "更新时间")
    private Date updatedAt;
}
