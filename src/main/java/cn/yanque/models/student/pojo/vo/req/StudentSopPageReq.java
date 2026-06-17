package cn.yanque.models.student.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 学生入学SOP分页查询请求。
 */
@Data
@Schema(description = "学生入学SOP分页查询请求")
public class StudentSopPageReq {

    @Schema(description = "学生姓名")
    private String studentName;

    @Schema(description = "手机号")
    private String studentPhone;

    @Schema(description = "导师ID")
    private Long mentorId;

    @Schema(description = "状态：ASSIGNED已分配，CANCELED已取消")
    private String status;

    @Min(value = 1, message = "页码不能小于1")
    @Schema(description = "页码", defaultValue = "1")
    private Integer pageNum = 1;

    @Min(value = 1, message = "每页条数不能小于1")
    @Schema(description = "每页条数", defaultValue = "10")
    private Integer pageSize = 10;
}
