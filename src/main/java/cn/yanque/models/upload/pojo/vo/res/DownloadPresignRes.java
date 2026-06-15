package cn.yanque.models.upload.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 下载预签名响应。
 */
@Data
@Schema(description = "下载预签名响应")
public class DownloadPresignRes {

    @Schema(description = "预签名下载URL")
    private String downloadUrl;

    @Schema(description = "对象存储Key")
    private String objectKey;

    @Schema(description = "过期秒数")
    private Long expires;
}
