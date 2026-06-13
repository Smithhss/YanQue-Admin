package cn.yanque.models.order.prepay.service.impl;

import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.order.prepay.mapper.OrderMapper;
import cn.yanque.models.order.prepay.pojo.entity.OrderEntity;
import cn.yanque.models.order.prepay.pojo.info.UpdateOrderStatusInfo;
import cn.yanque.models.order.prepay.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;


    @Override
    public void saveOrder(OrderEntity entity) {
        int i;
        try {
            i = orderMapper.insert(entity);
        } catch (DuplicateKeyException e) {
            throw BusinessException.DateExist.newInstance("订单已存在,请勿重复下单");
        }

        if (i != 1){
            throw BusinessException.DateExist.newInstance("下单保存订单失败");
        }
    }

    @Override
    public void updateOrderStatus(UpdateOrderStatusInfo updateOrderStatusInfo) {
        int i = orderMapper.updateOrderStatus(updateOrderStatusInfo);
        if (i != 1) {
            throw BusinessException.DateExist.newInstance("更新订单状态失败,订单状态已经发生变化");
        }
    }

    @Override
    public OrderEntity selectByOrderNo(String orderNo) {
        return orderMapper.selectByOrderNo(orderNo);
    }
}
