package cn.yanque.models.order.prepay.pojo.bo;

import lombok.Data;

/**
 * 支付订单查询条件。
 */
@Data
public class QueryOrderBo {

    /** 支付订单号 */
    private String orderNo;

    /** 学生姓名 */
    private String studentName;

    /** 学生手机号 */
    private String studentPhone;

    /** 预支付订单号 */
    private String prepayOrderNo;

    /** 支付订单状态 */
    private String status;
}
