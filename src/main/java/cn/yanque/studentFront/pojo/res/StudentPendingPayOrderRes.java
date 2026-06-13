package cn.yanque.studentFront.pojo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 学生待支付订单信息。
 */
@Data
@Schema(description = "学生待支付订单信息")
public class StudentPendingPayOrderRes {

    /** 订单ID */
    @Schema(description = "订单ID")
    private Long id;

    /** 订单号 */
    @Schema(description = "订单号")
    private String orderNo;

    /** 学生姓名 */
    @Schema(description = "学生姓名")
    private String studentName;

    /** 学生手机号 */
    @Schema(description = "学生手机号")
    private String studentPhone;

    /** 产品ID */
    @Schema(description = "产品ID")
    private Long productId;

    /** 产品内容 */
    @Schema(description = "产品内容")
    private String productContent;

    /** 产品金额 */
    @Schema(description = "产品金额")
    private BigDecimal productAmount;

    /** 优惠金额 */
    @Schema(description = "优惠金额")
    private BigDecimal discountAmount;

    /** 应付金额 */
    @Schema(description = "应付金额")
    private BigDecimal payableAmount;

    /** 订单状态 */
    @Schema(description = "订单状态")
    private String orderStatus;
}
