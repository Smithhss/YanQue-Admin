package cn.yanque.models.order.prepay.pojo.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 预支付订单实体,对应 prepay_order 表。
 */
@Data
public class PrepayOrderEntity {

    /** 订单ID */
    private Long id;

    /** 订单号 */
    private String orderNo;

    /** 学生姓名 */
    private String studentName;

    /** 学生手机号 */
    private String studentPhone;

    /** 产品ID */
    private Long productId;

    /** 产品金额 */
    private BigDecimal productAmount;

    /** 优惠金额 */
    private BigDecimal discountAmount;

    /** 订单状态:PENDING_PAYMENT待支付,PAID已支付,CANCELED已取消 */
    private String orderStatus;

    /** 创建时间 */
    private Date createdAt;

    /** 更新时间 */
    private Date updatedAt;
}
