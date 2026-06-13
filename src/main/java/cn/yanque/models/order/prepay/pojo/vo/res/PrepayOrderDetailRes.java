package cn.yanque.models.order.prepay.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 预支付订单详情响应。
 */
@Data
@Schema(description = "预支付订单详情")
public class PrepayOrderDetailRes {

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
    @Schema(description = "手机号")
    private String studentPhone;

    /** 产品ID */
    @Schema(description = "产品ID")
    private Long productId;

    /** 产品金额 */
    @Schema(description = "产品金额")
    private BigDecimal productAmount;

    /** 优惠金额 */
    @Schema(description = "优惠金额")
    private BigDecimal discountAmount;

    /** 订单状态 */
    @Schema(description = "订单状态")
    private String orderStatus;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private Date createdAt;

    /** 更新时间 */
    @Schema(description = "更新时间")
    private Date updatedAt;
}
