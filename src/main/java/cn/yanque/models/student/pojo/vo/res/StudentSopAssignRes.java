package cn.yanque.models.student.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 学生入学SOP分配响应。
 */
@Data
@Schema(description = "学生入学SOP分配响应")
public class StudentSopAssignRes {

    @Schema(description = "SOP记录ID")
    private Long id;

    @Schema(description = "学生ID")
    private Long studentId;
}
