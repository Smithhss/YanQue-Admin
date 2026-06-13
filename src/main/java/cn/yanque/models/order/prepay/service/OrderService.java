package cn.yanque.models.order.prepay.service;

import cn.yanque.models.order.prepay.pojo.entity.OrderEntity;
import cn.yanque.models.order.prepay.pojo.info.UpdateOrderStatusInfo;

public interface OrderService {

    void saveOrder(OrderEntity entity);

    void updateOrderStatus(UpdateOrderStatusInfo updateOrderStatusInfo);

    OrderEntity selectByOrderNo(String orderNo);
}
