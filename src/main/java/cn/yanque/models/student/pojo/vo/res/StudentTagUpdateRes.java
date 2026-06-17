package cn.yanque.models.student.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 学生标签更新响应。
 */
@Data
@Schema(description = "学生标签更新响应")
public class StudentTagUpdateRes {

    @Schema(description = "学生ID")
    private Long studentId;

    @Schema(description = "学生标签")
    private String studentTag;
}
