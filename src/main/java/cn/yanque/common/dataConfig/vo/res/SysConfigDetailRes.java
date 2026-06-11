package cn.yanque.common.dataConfig.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 系统配置详情响应对象。
 */
@Data
@Schema(description = "系统配置详情响应")
public class SysConfigDetailRes {

    /** 配置ID */
    @Schema(description = "配置ID")
    private Long id;

    /** 配置key */
    @Schema(description = "配置键")
    private String k;

    /** 配置值 */
    @Schema(description = "配置值")
    private String v;
}
