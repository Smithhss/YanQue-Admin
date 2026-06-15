package cn.yanque.models.order.refund.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.order.refund.pojo.entity.RefundOrderEntity;
import cn.yanque.models.order.refund.pojo.vo.req.RefundOrderPageReq;
import cn.yanque.models.order.refund.pojo.vo.res.RefundOrderPageRes;

import java.util.Date;

public interface RefundOrderService {

    void saveRefundOrder(RefundOrderEntity entity);

    RefundOrderEntity selectByRefundOrderNo(String refundOrderNo);

    PageResult<RefundOrderPageRes> pageRefundOrder(RefundOrderPageReq req);

    void updateRefundProcessing(String refundOrderNo, String oldStatus, String uniqueRefundNo);

    void updateRefundSuccess(String refundOrderNo, String oldStatus,  String uniqueRefundNo, Date refundSuccessTime);

    void updateRefundFail(String refundOrderNo, String oldStatus, String failReason);
}
