package cn.yanque.models.student.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 学生分配班级响应。
 */
@Data
@Schema(description = "学生分配班级响应")
public class StudentAssignClassRes {

    @Schema(description = "学生ID")
    private Long studentId;

    @Schema(description = "班级ID")
    private Long classId;
}
