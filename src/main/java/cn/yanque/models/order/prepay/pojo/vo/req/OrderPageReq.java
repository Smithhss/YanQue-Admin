package cn.yanque.models.order.prepay.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 支付订单分页查询请求。
 */
@Data
@Schema(description = "支付订单分页查询请求")
public class OrderPageReq {

    /** 支付订单号 */
    @Schema(description = "支付订单号")
    private String orderNo;

    /** 学生姓名 */
    @Schema(description = "学生姓名")
    private String studentName;

    /** 学生手机号 */
    @Schema(description = "学生手机号")
    private String studentPhone;

    /** 预支付订单号 */
    @Schema(description = "预支付订单号")
    private String prepayOrderNo;

    /** 支付订单状态 */
    @Schema(description = "订单状态:INIT初始化,FAIL失败,PROCESSING支付中,SUCCESS支付成功,TIMEOUT超时")
    private String status;

    /** 当前页码 */
    @Min(value = 1, message = "页码不能小于1")
    @Schema(description = "页码", defaultValue = "1")
    private Integer pageNum = 1;

    /** 每页条数 */
    @Min(value = 1, message = "每页条数不能小于1")
    @Schema(description = "每页条数", defaultValue = "10")
    private Integer pageSize = 10;
}
