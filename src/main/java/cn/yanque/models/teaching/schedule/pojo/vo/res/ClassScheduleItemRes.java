package cn.yanque.models.teaching.schedule.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "班级课表列表项")
public class ClassScheduleItemRes {

    /** 课表ID */
    @Schema(description = "课表ID")
    private Long id;

    /** 班级ID */
    @Schema(description = "班级ID")
    private Long classId;

    /** 老师用户ID */
    @Schema(description = "老师ID")
    private Long teacherId;

    /** 上课日期 */
    @Schema(description = "上课日期")
    private Date scheduleDate;

    /** 课程详情ID */
    @Schema(description = "课程详情ID")
    private Long courseDetailId;

    /** 课程内容 */
    @Schema(description = "课程内容")
    private String courseContent;

    /** 上课类型 */
    @Schema(description = "上课类型")
    private String classType;
}
