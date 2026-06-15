package cn.yanque.models.order.refund.pojo.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRefundStatusInfo {

    private String refundOrderNo;

    private String status;

    private String oldStatus;

    private String uniqueRefundNo;

    private String failReason;

    private Date refundSuccessTime;
}
