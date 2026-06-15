package cn.yanque.studentFront.pojo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 学生登录响应。
 */
@Data
@Schema(description = "学生登录响应")
public class StudentLoginRes {

    /** 是否需要先支付 */
    @Schema(description = "是否需要先支付")
    private Boolean needPay;

    /** JWT token，需要支付时为空 */
    @Schema(description = "JWT token")
    private String token;

    /** 签名密钥，正式登录后用于学生端接口签名 */
    @Schema(description = "签名密钥")
    private String signSecret;

    /** 待支付临时token，仅needPay为true时返回 */
    @Schema(description = "待支付临时token")
    private String pendingPayToken;

    /** 待支付临时签名密钥，仅needPay为true时返回 */
    @Schema(description = "待支付临时签名密钥")
    private String pendingPaySignSecret;

    /** 学生信息 */
    @Schema(description = "学生信息")
    private StudentInfoRes student;

    /** 待支付订单信息 */
    @Schema(description = "待支付订单信息")
    private StudentPendingPayOrderRes pendingOrder;
}
