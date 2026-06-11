package cn.yanque.common.dataConfig.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 修改系统配置响应对象。
 */
@Data
@Schema(description = "修改系统配置响应")
public class SysConfigUpdateRes {

    /** 配置ID */
    @Schema(description = "配置ID")
    private Long id;
}
