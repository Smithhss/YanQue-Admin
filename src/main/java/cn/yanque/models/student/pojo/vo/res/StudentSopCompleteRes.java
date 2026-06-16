package cn.yanque.models.student.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 学生入学SOP完成响应。
 */
@Data
@Schema(description = "学生入学SOP完成响应")
public class StudentSopCompleteRes {

    @Schema(description = "SOP记录ID")
    private Long id;
}
