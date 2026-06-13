package cn.yanque.models.order.prepay.mapper;

import cn.yanque.models.order.prepay.pojo.entity.OrderEntity;
import cn.yanque.models.order.prepay.pojo.info.UpdateOrderStatusInfo;

public interface OrderMapper {
    int insert(OrderEntity entity);

    int updateOrderStatus(UpdateOrderStatusInfo updateOrderStatusInfo);

    OrderEntity selectByOrderNo(String orderNo);
}
