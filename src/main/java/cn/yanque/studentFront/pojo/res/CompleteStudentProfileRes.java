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

    @Schema(description = "正式登录JWT token")
    private String token;

    @Schema(description = "正式登录签名密钥")
    private String signSecret;

    @Schema(description = "学生信息")
    private StudentInfoRes student;
}
