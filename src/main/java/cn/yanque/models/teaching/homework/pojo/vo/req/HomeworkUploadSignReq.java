package cn.yanque.models.teaching.homework.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 作业内容上传预签名请求。
 */
@Data
@Schema(description = "作业内容上传预签名请求")
public class HomeworkUploadSignReq {

    @NotNull(message = "班级ID不能为空")
    @Schema(description = "班级ID")
    private Long classId;

    @NotBlank(message = "文件名不能为空")
    @Schema(description = "文件名")
    private String fileName;
}
