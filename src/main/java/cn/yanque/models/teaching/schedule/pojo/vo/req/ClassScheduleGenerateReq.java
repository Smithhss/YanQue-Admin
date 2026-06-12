package cn.yanque.models.teaching.schedule.pojo.vo.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "生成班级课表请求")
public class ClassScheduleGenerateReq {

    /** 班级ID */
    @NotNull(message = "班级ID不能为空")
    @Schema(description = "班级ID")
    private Long classId;

    /** 第一天上课日期 */
    @NotNull(message = "第一天上课时间不能为空")
    @Schema(description = "第一天上课时间，格式：yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date firstClassDate;
}
