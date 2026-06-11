package cn.yanque.models.teaching.course.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 新增课程详情响应对象。
 */
@Data
@Schema(description = "创建课程详情响应")
public class CourseDetailCreateRes {

    /** 课程详情ID */
    @Schema(description = "课程详情ID")
    private Long id;
}
