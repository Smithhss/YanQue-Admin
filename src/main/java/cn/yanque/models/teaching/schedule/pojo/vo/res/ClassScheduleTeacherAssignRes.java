package cn.yanque.models.teaching.schedule.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "班级课表阶段老师分配响应")
public class ClassScheduleTeacherAssignRes {

    /** 班级ID */
    @Schema(description = "班级ID")
    private Long classId;

    /** 更新课表数量 */
    @Schema(description = "更新课表数量")
    private Integer updateCount;
}
