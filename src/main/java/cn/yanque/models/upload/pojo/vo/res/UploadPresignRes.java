package cn.yanque.models.upload.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * 上传预签名响应。
 */
@Data
@Schema(description = "上传预签名响应")
public class UploadPresignRes {

    @Schema(description = "预签名上传URL")
    private String uploadUrl;

    @Schema(description = "对象存储Key")
    private String objectKey;

    @Schema(description = "过期秒数")
    private Long expires;

    @Schema(description = "上传时需要携带的请求头")
    private Map<String, String> headers;
}
