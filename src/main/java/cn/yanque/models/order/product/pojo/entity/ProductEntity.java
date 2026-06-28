package cn.yanque.models.order.product.pojo.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单产品实体,对应 order_product 表。
 */
@Data
public class ProductEntity {

    /** 产品ID */
    private Long id;

    /** 课程内容 */
    private String courseContent;

    /** 上课方式:ONLINE线上,OFFLINE线下 */
    private String teachingMode;

    /** 产品价格 */
    private BigDecimal price;

    /** 创建时间 */
    private Date createdAt;

    /** 更新时间 */
    private Date updatedAt;
}
