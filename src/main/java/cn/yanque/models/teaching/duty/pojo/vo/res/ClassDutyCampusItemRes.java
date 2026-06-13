package cn.yanque.models.teaching.duty.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "校区统一值班排班项")
public class ClassDutyCampusItemRes {

    @Schema(description = "校区ID")
    private Long campusId;

    @Schema(description = "校区名称")
    private String campusName;

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
