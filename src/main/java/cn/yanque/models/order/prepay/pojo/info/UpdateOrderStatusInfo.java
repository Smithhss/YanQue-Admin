package cn.yanque.models.order.prepay.pojo.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderStatusInfo {

    private String orderNo;

    private String status;

    private String oldStatus;

    private String uniqueOrderNo;

    private Date paySuccessTime;
}
