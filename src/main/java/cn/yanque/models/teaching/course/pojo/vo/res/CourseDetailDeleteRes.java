package cn.yanque.models.teaching.course.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 删除课程详情响应对象。
 */
@Data
@Schema(description = "删除课程详情响应")
public class CourseDetailDeleteRes {

    /** 课程详情ID */
    @Schema(description = "课程详情ID")
    private Long id;
}
