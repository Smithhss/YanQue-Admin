package cn.yanque.models.teaching.homework.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * 作业内容上传预签名响应。
 */
@Data
@Schema(description = "作业内容上传预签名响应")
public class HomeworkUploadSignRes {

    @Schema(description = "预签名上传URL")
    private String uploadUrl;

    @Schema(description = "对象存储Key")
    private String contentObjectKey;

    @Schema(description = "作业内容文件名")
    private String contentFileName;

    @Schema(description = "过期秒数")
    private Long expires;

    @Schema(description = "上传时需要携带的请求头")
    private Map<String, String> headers;
}
