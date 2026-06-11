package cn.yanque.common.dataConfig.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 删除系统配置响应对象。
 */
@Data
@Schema(description = "删除系统配置响应")
public class SysConfigDeleteRes {

    /** 配置ID */
    @Schema(description = "配置ID")
    private Long id;
}
