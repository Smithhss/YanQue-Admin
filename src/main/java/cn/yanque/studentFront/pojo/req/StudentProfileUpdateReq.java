package cn.yanque.studentFront.pojo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "学生更新个人资料请求")
public class StudentProfileUpdateReq {

    @Schema(description = "学历")
    private String education;

    @Schema(description = "届数")
    private Integer gradeYear;

    @Schema(description = "学校")
    private String school;

    @Schema(description = "专业")
    private String major;
}
