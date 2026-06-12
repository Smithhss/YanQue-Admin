package cn.yanque.models.teaching.schedule.pojo.vo.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "班级当天课表详情响应")
public class ClassScheduleDateDetailRes {

    /** 课表ID */
    @Schema(description = "课表ID")
    private Long id;

    /** 班级ID */
    @Schema(description = "班级ID")
    private Long classId;

    /** 老师ID */
    @Schema(description = "老师ID")
    private Long teacherId;

    /** 老师名称 */
    @Schema(description = "老师名称")
    private String teacherName;

    /** 上课日期 */
    @Schema(description = "上课日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date scheduleDate;

    /** 课程详情ID */
    @Schema(description = "课程详情ID")
    private Long courseDetailId;

    /** 阶段名称 */
    @Schema(description = "阶段名称")
    private String stageName;

    /** 第几天 */
    @Schema(description = "第几天")
    private Integer dayNumber;

    /** 课程内容 */
    @Schema(description = "课程内容")
    private String courseContent;

    /** 上课类型 */
    @Schema(description = "上课类型")
    private String classType;
}
