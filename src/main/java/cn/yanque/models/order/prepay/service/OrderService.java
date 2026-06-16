package cn.yanque.models.order.prepay.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.order.prepay.pojo.entity.OrderEntity;
import cn.yanque.models.order.prepay.pojo.info.UpdateOrderStatusInfo;
import cn.yanque.models.order.prepay.pojo.vo.req.OrderPageReq;
import cn.yanque.models.order.prepay.pojo.vo.res.OrderPageRes;

import java.math.BigDecimal;

public interface OrderService {

    void saveOrder(OrderEntity entity);

    void updateOrderStatus(UpdateOrderStatusInfo updateOrderStatusInfo);

    OrderEntity selectByOrderNo(String orderNo);

    OrderEntity selectLatestSuccessByStudentPhone(String studentPhone);

    PageResult<OrderPageRes> pageOrder(OrderPageReq req);

    void increaseRefundedAmount(String orderNo, BigDecimal refundAmount);

    void decreaseRefundedAmount(String orderNo, BigDecimal refundAmount);
}
