package cn.yanque.models.order.refund.biz;

import cn.yanque.models.order.refund.pojo.vo.req.RefundApplyReq;
import cn.yanque.models.order.refund.pojo.vo.res.RefundApplyRes;
import cn.yanque.models.order.refund.pojo.vo.res.RefundCreateRes;

import java.util.Date;

public interface RefundOrderBiz {

    RefundCreateRes createRefundOrder();

    RefundApplyRes applyRefund(String refundOrderNo, RefundApplyReq req);

    void handleRefundSuccess(String refundOrderNo, String uniqueRefundNo, Date refundSuccessTime);

    void handleRefundFail(String refundOrderNo, String failReason);
}
