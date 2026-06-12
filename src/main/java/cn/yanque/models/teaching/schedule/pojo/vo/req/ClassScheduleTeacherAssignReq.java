package cn.yanque.models.teaching.schedule.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "班级课表阶段老师分配请求")
public class ClassScheduleTeacherAssignReq {

    /** 阶段老师分配列表 */
    @Valid
    @NotEmpty(message = "阶段老师分配不能为空")
    @Schema(description = "阶段老师分配列表")
    private List<StageTeacherAssignItem> stages;

    @Data
    @Schema(description = "阶段老师分配项")
    public static class StageTeacherAssignItem {

        /** 阶段名称 */
        @NotNull(message = "阶段名称不能为空")
        @Schema(description = "阶段名称")
        private String stageName;

        /** 老师ID */
        @NotNull(message = "老师ID不能为空")
        @Schema(description = "老师ID")
        private Long teacherId;
    }
}
