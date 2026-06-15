package cn.yanque.models.teaching.homework.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 作业内容预览地址响应。
 */
@Data
@Schema(description = "作业内容预览地址响应")
public class HomeworkContentRes {

    @Schema(description = "作业内容文件名")
    private String contentFileName;

    @Schema(description = "预签名预览URL")
    private String previewUrl;

    @Schema(description = "过期秒数")
    private Long expires;
}
