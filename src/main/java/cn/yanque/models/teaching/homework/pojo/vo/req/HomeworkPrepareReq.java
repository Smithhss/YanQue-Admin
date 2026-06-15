package cn.yanque.models.teaching.homework.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 作业发布预填请求。
 */
@Data
@Schema(description = "作业发布预填请求")
public class HomeworkPrepareReq {

    @NotNull(message = "班级ID不能为空")
    @Schema(description = "班级ID")
    private Long classId;

    @NotNull(message = "作业日期不能为空")
    @Schema(description = "作业日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date homeworkDate;
}
