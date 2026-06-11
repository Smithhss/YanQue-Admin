package cn.yanque.models.teaching.course.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "删除课程详情响应")
public class CourseDetailDeleteRes {

    @Schema(description = "课程详情ID")
    private Long id;
}
