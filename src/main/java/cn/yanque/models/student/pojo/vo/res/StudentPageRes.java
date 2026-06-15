package cn.yanque.models.student.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 学生分页项响应。
 */
@Data
@Schema(description = "学生分页项")
public class StudentPageRes {

    @Schema(description = "学生ID")
    private Long id;

    @Schema(description = "学员编号")
    private String studentNo;

    @Schema(description = "学生姓名")
    private String studentName;

    @Schema(description = "手机号")
    private String studentPhone;

    @Schema(description = "学历")
    private String education;

    @Schema(description = "届数")
    private Integer gradeYear;

    @Schema(description = "学校")
    private String school;

    @Schema(description = "专业")
    private String major;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "创建时间")
    private Date createdAt;

    @Schema(description = "更新时间")
    private Date updatedAt;
}
