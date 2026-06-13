package cn.yanque.studentFront.pojo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "学生支付下单响应")
public class CreatePaymentOrderRes {

    /** 收银台地址 */
    @Schema(description = "收银台地址，前端跳转到该地址完成支付", example = "https://cashier.example.com/pay?token=xxx")
    private String cashierUrl;
}
