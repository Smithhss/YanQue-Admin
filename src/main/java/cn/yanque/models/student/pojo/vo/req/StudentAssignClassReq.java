package cn.yanque.models.student.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 学生分配班级请求。
 */
@Data
@Schema(description = "学生分配班级请求")
public class StudentAssignClassReq {

    @NotNull(message = "班级ID不能为空")
    @Schema(description = "班级ID")
    private Long classId;
}
