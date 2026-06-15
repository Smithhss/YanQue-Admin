package cn.yanque.studentFront.pojo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 学生提交作业请求。
 */
@Data
@Schema(description = "学生提交作业请求")
public class StudentHomeworkSubmitReq {

    @NotBlank(message = "提交文件对象Key不能为空")
    private String objectKey;

    @NotBlank(message = "提交文件名不能为空")
    private String fileName;
}
