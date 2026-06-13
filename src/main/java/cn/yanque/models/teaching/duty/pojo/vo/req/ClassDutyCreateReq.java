package cn.yanque.models.teaching.duty.pojo.vo.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "新增值班请求")
public class ClassDutyCreateReq {

    @Schema(description = "班级ID")
    private Long classId;

    @Schema(description = "校区ID")
    private Long campusId;

    @NotNull(message = "老师不能为空")
    @Schema(description = "老师ID")
    private Long teacherId;

    @NotNull(message = "值班日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    @Schema(description = "值班日期")
    private Date dutyDate;

    @NotBlank(message = "值班类型不能为空")
    @Schema(description = "值班类型")
    private String dutyType;

    @Schema(description = "备注")
    private String remark;
}
