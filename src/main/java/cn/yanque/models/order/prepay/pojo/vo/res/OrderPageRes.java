package cn.yanque.models.order.prepay.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付订单分页项响应。
 */
@Data
@Schema(description = "支付订单分页项")
public class OrderPageRes {

    /** 主键ID */
    @Schema(description = "主键ID")
    private Long id;

    /** 支付订单号 */
    @Schema(description = "支付订单号")
    private String orderNo;

    /** 学生手机号 */
    @Schema(description = "学生手机号")
    private String studentPhone;

    /** 学生姓名 */
    @Schema(description = "学生姓名")
    private String studentName;

    /** 产品ID */
    @Schema(description = "产品ID")
    private String productId;

    /** 产品内容 */
    @Schema(description = "产品内容")
    private String productContent;

    /** 订单支付金额 */
    @Schema(description = "订单支付金额")
    private BigDecimal orderAmount;

    /** 预支付订单号 */
    @Schema(description = "预支付订单号")
    private String prepayOrderNo;

    /** 支付订单状态 */
    @Schema(description = "订单状态：INIT初始化，FAIL失败，PROCESSING支付中，SUCCESS支付成功，TIMEOUT超时")
    private String status;

    /** 支付渠道唯一订单号 */
    @Schema(description = "支付渠道唯一订单号")
    private String uniqueOrderNo;

    /** 支付成功时间 */
    @Schema(description = "支付成功时间")
    private Date paySuccessTime;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private Date createdAt;

    /** 更新时间 */
    @Schema(description = "更新时间")
    private Date updatedAt;
}
