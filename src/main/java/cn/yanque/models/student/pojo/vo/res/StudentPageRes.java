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

    @Schema(description = "上课方式：ONLINE线上，OFFLINE线下")
    private String teachingMode;

    @Schema(description = "班级ID")
    private Long classId;

    @Schema(description = "班级期数")
    private String classPeriod;

    @Schema(description = "产品内容")
    private String productContent;

    @Schema(description = "学生标签")
    private String studentTag;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "是否已分配入学SOP")
    private Boolean sopAssigned;

    @Schema(description = "SOP记录ID")
    private Long sopId;

    @Schema(description = "SOP导师ID")
    private Long sopMentorId;

    @Schema(description = "SOP导师名称")
    private String sopMentorName;

    @Schema(description = "SOP视频对象Key")
    private String sopVideoObjectKey;

    @Schema(description = "SOP视频文件名")
    private String sopVideoFileName;

    @Schema(description = "SOP时间")
    private Date sopTime;

    @Schema(description = "创建时间")
    private Date createdAt;

    @Schema(description = "更新时间")
    private Date updatedAt;
}
