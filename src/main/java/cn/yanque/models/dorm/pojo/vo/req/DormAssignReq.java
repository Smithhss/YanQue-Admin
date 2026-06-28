package cn.yanque.models.dorm.pojo.vo.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 学生入住分配请求对象。
 */
@Data
@Schema(description = "入住分配请求")
public class DormAssignReq {

    /** 学生ID */
    @NotNull(message = "学生不能为空")
    @Schema(description = "学生ID")
    private Long studentId;

    /** 床位ID */
    @NotNull(message = "床位不能为空")
    @Schema(description = "床位ID")
    private Long bedId;

    /** 入住日期,不填默认今天 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "入住日期(yyyy-MM-dd),不填默认今天")
    private Date checkInDate;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
}
