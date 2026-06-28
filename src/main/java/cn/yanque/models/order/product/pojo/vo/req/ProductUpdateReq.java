package cn.yanque.models.order.product.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 修改产品请求对象。
 */
@Data
@Schema(description = "修改产品请求")
public class ProductUpdateReq {

    /** 产品ID */
    @Schema(description = "产品ID")
    private Long id;

    /** 课程内容 */
    @NotBlank(message = "课程内容不能为空")
    @Schema(description = "课程内容")
    private String courseContent;

    /** 上课方式 */
    @NotBlank(message = "上课方式不能为空")
    @Schema(description = "上课方式:ONLINE线上,OFFLINE线下")
    private String teachingMode;

    /** 产品价格 */
    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.00", message = "价格不能小于0")
    @Schema(description = "价格")
    private BigDecimal price;
}
