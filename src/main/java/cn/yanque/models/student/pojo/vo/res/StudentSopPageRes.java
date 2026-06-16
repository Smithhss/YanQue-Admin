package cn.yanque.models.student.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 学生入学SOP分页项。
 */
@Data
@Schema(description = "学生入学SOP分页项")
public class StudentSopPageRes {

    @Schema(description = "SOP记录ID")
    private Long id;

    @Schema(description = "学生ID")
    private Long studentId;

    @Schema(description = "学生姓名")
    private String studentName;

    @Schema(description = "学生手机号")
    private String studentPhone;

    @Schema(description = "导师ID")
    private Long mentorId;

    @Schema(description = "导师名称")
    private String mentorName;

    @Schema(description = "SOP视频对象Key")
    private String sopVideoObjectKey;

    @Schema(description = "SOP视频文件名")
    private String sopVideoFileName;

    @Schema(description = "SOP时间")
    private Date sopTime;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private Date createdAt;

    @Schema(description = "更新时间")
    private Date updatedAt;
}
