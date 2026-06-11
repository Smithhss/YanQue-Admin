package cn.yanque.common.dataConfig.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "系统配置详情响应")
public class SysConfigDetailRes {

    @Schema(description = "配置ID")
    private Long id;

    @Schema(description = "配置键")
    private String k;

    @Schema(description = "配置值")
    private String v;
}
