package cn.yanque.studentFront.pojo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 支付成功回跳页订单信息。
 */
@Data
@Schema(description = "支付成功回跳页订单信息")
public class PaymentReturnInfoRes {

    @Schema(description = "支付订单号")
    private String orderNo;

    @Schema(description = "学生姓名")
    private String studentName;

    @Schema(description = "手机号")
    private String studentPhone;

    @Schema(description = "产品ID")
    private String productId;

    @Schema(description = "产品内容")
    private String productContent;

    @Schema(description = "支付金额")
    private BigDecimal orderAmount;

    @Schema(description = "订单状态")
    private String status;
}
