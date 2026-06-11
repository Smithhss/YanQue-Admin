package cn.yanque.models.teaching.course.pojo.vo.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "课程详情响应")
public class CourseDetailItemRes {

    @Schema(description = "课程详情ID")
    private Long id;

    @Schema(description = "课程ID")
    private Long courseId;

    @Schema(description = "阶段")
    private String stageName;

    @Schema(description = "第几天")
    private Integer dayNumber;

    @Schema(description = "上课内容")
    private String classContent;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createdAt;

    @Schema(description = "更新时间")
    private Date updatedAt;
}
