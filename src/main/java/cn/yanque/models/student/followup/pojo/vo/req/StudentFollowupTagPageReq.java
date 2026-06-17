package cn.yanque.models.student.followup.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Schema(description = "学生回访标签配置分页查询请求")
public class StudentFollowupTagPageReq {

    @Schema(description = "学生标签")
    private String studentTag;

    @Schema(description = "状态")
    private String status;

    @Min(value = 1, message = "页码不能小于1")
    @Schema(description = "页码", defaultValue = "1")
    private Integer pageNum = 1;

    @Min(value = 1, message = "每页条数不能小于1")
    @Schema(description = "每页条数", defaultValue = "10")
    private Integer pageSize = 10;
}
