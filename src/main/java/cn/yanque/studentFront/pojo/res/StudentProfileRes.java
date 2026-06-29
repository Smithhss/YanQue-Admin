package cn.yanque.studentFront.pojo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "学生个人资料")
public class StudentProfileRes {

    @Schema(description = "学生ID")
    private Long id;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "性别:MALE男/FEMALE女")
    private String gender;

    @Schema(description = "学历")
    private String education;

    @Schema(description = "届数")
    private Integer gradeYear;

    @Schema(description = "学校")
    private String school;

    @Schema(description = "专业")
    private String major;

    @Schema(description = "授课方式:ONLINE/OFFLINE")
    private String teachingMode;

    @Schema(description = "状态")
    private String status;
}
