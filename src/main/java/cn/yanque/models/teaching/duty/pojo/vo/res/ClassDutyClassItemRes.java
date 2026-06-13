package cn.yanque.models.teaching.duty.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "班级值班排班项")
public class ClassDutyClassItemRes {

    @Schema(description = "课表ID")
    private Long scheduleId;

    @Schema(description = "班级ID")
    private Long classId;

    @Schema(description = "班级期数")
    private String classPeriod;

    @Schema(description = "校区ID")
    private Long campusId;

    @Schema(description = "校区名称")
    private String campusName;

    @Schema(description = "上课类型")
    private String classType;

    @Schema(description = "上课类型描述")
    private String classTypeDesc;

    @Schema(description = "课程内容")
    private String courseContent;

    @Schema(description = "值班类型")
    private String dutyType;

    @Schema(description = "值班类型描述")
    private String dutyTypeDesc;

    @Schema(description = "开始时间")
    private String startTime;

    @Schema(description = "结束时间")
    private String endTime;

    @Schema(description = "老师ID")
    private Long teacherId;

    @Schema(description = "老师名称")
    private String teacherName;
}
