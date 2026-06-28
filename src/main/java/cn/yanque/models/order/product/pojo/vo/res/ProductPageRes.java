package cn.yanque.models.order.product.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品分页项响应。
 */
@Data
@Schema(description = "产品分页项")
public class ProductPageRes {

    /** 产品ID */
    @Schema(description = "产品ID")
    private Long id;

    /** 课程内容 */
    @Schema(description = "课程内容")
    private String courseContent;

    /** 上课方式 */
    @Schema(description = "上课方式:ONLINE线上,OFFLINE线下")
    private String teachingMode;

    /** 产品价格 */
    @Schema(description = "价格")
    private BigDecimal price;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private Date createdAt;

    /** 更新时间 */
    @Schema(description = "更新时间")
    private Date updatedAt;
}
