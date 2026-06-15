package cn.yanque.models.teaching.homework.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 作业提交批改请求。
 */
@Data
@Schema(description = "作业提交批改请求")
public class HomeworkSubmissionGradeReq {

    @NotNull(message = "分数不能为空")
    @Min(value = 0, message = "分数不能小于0")
    @Max(value = 100, message = "分数不能大于100")
    private Integer score;

    private String teacherRemark;
}
