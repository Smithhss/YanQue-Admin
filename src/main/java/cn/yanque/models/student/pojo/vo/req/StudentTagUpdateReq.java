package cn.yanque.models.student.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 学生标签更新请求。
 */
@Data
@Schema(description = "学生标签更新请求")
public class StudentTagUpdateReq {

    @Size(max = 50, message = "学生标签不能超过50个字符")
    @Schema(description = "学生标签")
    private String studentTag;
}
