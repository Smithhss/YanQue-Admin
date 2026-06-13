package cn.yanque.models.order.product.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 修改产品响应。
 */
@Data
@Schema(description = "修改产品响应")
public class ProductUpdateRes {

    /** 产品ID */
    @Schema(description = "产品ID")
    private Long id;
}
