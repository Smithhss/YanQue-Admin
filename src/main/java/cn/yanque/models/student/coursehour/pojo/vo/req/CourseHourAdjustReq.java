package cn.yanque.models.student.coursehour.pojo.vo.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseHourAdjustReq {

    @NotNull(message = "学生ID不能为空")
    private Long studentId;

    /** 变动课时：正为充值，负为扣减 */
    @NotNull(message = "调整课时不能为空")
    private BigDecimal changeHours;

    private String reason;
}
