package cn.yanque.models.order.prepay.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.order.prepay.pojo.entity.OrderEntity;
import cn.yanque.models.order.prepay.pojo.info.UpdateOrderStatusInfo;
import cn.yanque.models.order.prepay.pojo.vo.req.OrderPageReq;
import cn.yanque.models.order.prepay.pojo.vo.res.OrderPageRes;

public interface OrderService {

    void saveOrder(OrderEntity entity);

    void updateOrderStatus(UpdateOrderStatusInfo updateOrderStatusInfo);

    OrderEntity selectByOrderNo(String orderNo);

    PageResult<OrderPageRes> pageOrder(OrderPageReq req);
}
