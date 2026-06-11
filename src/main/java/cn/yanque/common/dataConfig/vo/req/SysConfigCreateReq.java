package cn.yanque.common.dataConfig.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 新增系统配置请求对象。
 */
@Data
@Schema(description = "创建系统配置请求")
public class SysConfigCreateReq {

    /** 配置key */
    @NotBlank(message = "配置键不能为空")
    @Schema(description = "配置键")
    private String k;

    /** 配置值 */
    @NotBlank(message = "配置值不能为空")
    @Schema(description = "配置值")
    private String v;
}
