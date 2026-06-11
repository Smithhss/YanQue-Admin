package cn.yanque.models.teaching.course.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 修改课程响应对象。
 */
@Data
@Schema(description = "修改课程响应")
public class CourseUpdateRes {

    /** 课程ID */
    @Schema(description = "课程ID")
    private Long id;
}
