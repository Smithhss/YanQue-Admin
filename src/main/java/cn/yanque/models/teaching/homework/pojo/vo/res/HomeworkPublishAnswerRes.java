package cn.yanque.models.teaching.homework.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 发布作业答案响应。
 */
@Data
@Schema(description = "发布作业答案响应")
public class HomeworkPublishAnswerRes {

    @Schema(description = "作业ID")
    private Long id;

    @Schema(description = "答案对象存储Key")
    private String answerObjectKey;

    @Schema(description = "答案文件名")
    private String answerFileName;

    @Schema(description = "学生是否可见")
    private Boolean answerStudentVisible;
}
