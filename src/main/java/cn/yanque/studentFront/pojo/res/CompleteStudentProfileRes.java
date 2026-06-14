package cn.yanque.studentFront.pojo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 学生资料完善响应。
 */
@Data
@Schema(description = "学生资料完善响应")
public class CompleteStudentProfileRes {

    @Schema(description = "学生ID")
    private Long studentId;

    @Schema(description = "是否完成")
    private Boolean completed;
}
