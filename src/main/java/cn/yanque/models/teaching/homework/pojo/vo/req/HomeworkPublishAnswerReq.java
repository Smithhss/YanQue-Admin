package cn.yanque.models.teaching.homework.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 发布作业答案请求。
 */
@Data
@Schema(description = "发布作业答案请求")
public class HomeworkPublishAnswerReq {

    @NotBlank(message = "答案对象Key不能为空")
    @Schema(description = "答案对象存储Key")
    private String answerObjectKey;

    @NotBlank(message = "答案文件名不能为空")
    @Schema(description = "答案文件名")
    private String answerFileName;

    @NotNull(message = "学生是否可见不能为空")
    @Schema(description = "学生是否可见")
    private Boolean answerStudentVisible;
}
