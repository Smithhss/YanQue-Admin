package cn.yanque.models.order.prepay.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 修改预支付订单响应。
 */
@Data
@Schema(description = "修改预支付订单响应")
public class PrepayOrderUpdateRes {

    /** 订单ID */
    @Schema(description = "订单ID")
    private Long id;
}
