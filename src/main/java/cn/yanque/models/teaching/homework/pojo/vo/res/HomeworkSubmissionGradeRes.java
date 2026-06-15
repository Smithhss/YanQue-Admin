package cn.yanque.models.teaching.homework.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 作业提交批改响应。
 */
@Data
@Schema(description = "作业提交批改响应")
public class HomeworkSubmissionGradeRes {

    private Long id;

    private Integer score;

    private String teacherRemark;
}
