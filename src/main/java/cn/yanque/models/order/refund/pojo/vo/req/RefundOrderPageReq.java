package cn.yanque.models.order.refund.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Schema(description = "退款订单分页查询请求")
public class RefundOrderPageReq {

    @Schema(description = "退款订单号")
    private String refundOrderNo;

    @Schema(description = "原支付订单号")
    private String paymentOrderNo;

    @Schema(description = "退款状态：INIT初始化，PROCESSING退款处理中，SUCCESS退款成功，FAIL退款失败，CLOSED已关闭")
    private String status;

    @Min(value = 1, message = "页码不能小于1")
    @Schema(description = "页码", defaultValue = "1")
    private Integer pageNum = 1;

    @Min(value = 1, message = "每页条数不能小于1")
    @Schema(description = "每页条数", defaultValue = "10")
    private Integer pageSize = 10;
}
