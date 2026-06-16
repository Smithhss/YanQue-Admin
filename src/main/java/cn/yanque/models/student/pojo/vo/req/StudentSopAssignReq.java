package cn.yanque.models.student.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 学生入学SOP分配请求。
 */
@Data
@Schema(description = "学生入学SOP分配请求")
public class StudentSopAssignReq {

    @NotNull(message = "导师不能为空")
    @Schema(description = "导师用户ID")
    private Long mentorId;
}
