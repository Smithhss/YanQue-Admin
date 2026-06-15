package cn.yanque.models.upload.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 上传预签名请求。
 */
@Data
@Schema(description = "上传预签名请求")
public class UploadPresignReq {

    @NotBlank(message = "对象Key不能为空")
    @Schema(description = "对象存储Key")
    private String objectKey;
}
