package cn.yanque.models.order.product.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 删除产品响应。
 */
@Data
@Schema(description = "删除产品响应")
public class ProductDeleteRes {

    /** 产品ID */
    @Schema(description = "产品ID")
    private Long id;
}
