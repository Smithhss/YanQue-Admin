package cn.yanque.models.student.followup.pojo.vo.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Schema(description = "学生回访记录分页查询请求")
public class StudentFollowupRecordPageReq {

    @Schema(description = "学生姓名")
    private String studentName;

    @Schema(description = "学生手机号")
    private String studentPhone;

    @Schema(description = "学生标签")
    private String studentTag;

    @Schema(description = "回访状态")
    private String status;

    @Schema(description = "回访人ID")
    private Long followupUserId;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "应回访开始日期")
    private Date startDate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "应回访结束日期")
    private Date endDate;

    @Min(value = 1, message = "页码不能小于1")
    @Schema(description = "页码", defaultValue = "1")
    private Integer pageNum = 1;

    @Min(value = 1, message = "每页条数不能小于1")
    @Schema(description = "每页条数", defaultValue = "10")
    private Integer pageSize = 10;
}
