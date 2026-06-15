package cn.yanque.models.student.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * 学生产品关联实体。
 */
@Data
public class StudentProductEntity {

    /** 主键ID */
    private Long id;

    /** 学生ID */
    private Long studentId;

    /** 产品ID */
    private String productId;

    /** 来源支付订单号 */
    private String sourceOrderNo;

    /** 状态 */
    private String status;

    /** 创建时间 */
    private Date createdAt;

    /** 更新时间 */
    private Date updatedAt;
}
