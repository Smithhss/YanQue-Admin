package cn.yanque.models.order.prepay.pojo.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付订单实体。
 *
 * <p>学生在支付页点击下单后生成正式支付订单,用于记录支付订单号,学生信息,
 * 产品信息,关联的预支付订单号以及支付状态。</p>
 */
@Data
public class OrderEntity {

    /** 主键ID */
    private Long id;

    /** 支付订单号 */
    private String orderNo;

    /** 学生手机号 */
    private String studentPhone;

    /** 学生姓名 */
    private String studentName;

    /** 产品ID */
    private String productId;

    /** 产品内容 */
    private String productContent;

    /** 订单支付金额 */
    private BigDecimal orderAmount;

    /** 已申请退款金额,包含退款处理中和退款成功金额 */
    private BigDecimal refundedAmount;

    /** 预支付订单号 */
    private String prepayOrderNo;

    /** 支付订单状态 */
    private String status;

    /** 支付渠道唯一订单号 */
    private String uniqueOrderNo;

    /** 支付成功时间 */
    private Date paySuccessTime;

    /** 创建时间 */
    private Date createdAt;

    /** 更新时间 */
    private Date updatedAt;
}
