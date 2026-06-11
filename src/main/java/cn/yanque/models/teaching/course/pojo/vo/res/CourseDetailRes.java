package cn.yanque.models.teaching.course.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "课程详情响应")
public class CourseDetailRes {

    @Schema(description = "课程ID")
    private Long id;

    @Schema(description = "课程名称")
    private String courseName;

    @Schema(description = "课程天数")
    private Integer courseDays;

    @Schema(description = "资料路径")
    private String materialPath;

    @Schema(description = "创建时间")
    private Date createdAt;

    @Schema(description = "更新时间")
    private Date updatedAt;
}
