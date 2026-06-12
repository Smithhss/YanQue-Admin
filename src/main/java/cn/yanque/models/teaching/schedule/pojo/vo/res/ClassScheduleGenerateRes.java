package cn.yanque.models.teaching.schedule.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "生成班级课表响应")
public class ClassScheduleGenerateRes {

    /** 班级ID */
    @Schema(description = "班级ID")
    private Long classId;

    /** 生成课表数量 */
    @Schema(description = "生成课表数量")
    private Integer scheduleCount;
}
