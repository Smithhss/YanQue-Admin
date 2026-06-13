package cn.yanque.studentFront.biz.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.yanque.common.enums.OrderStatusEnum;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.config.ThreadPoolConfig;
import cn.yanque.integration.yeepay.pojo.req.YeepayUnifiedOrderReq;
import cn.yanque.integration.yeepay.pojo.res.YeepayUnifiedOrderRes;
import cn.yanque.integration.yeepay.service.YeepayCashierService;
import cn.yanque.models.order.prepay.pojo.entity.OrderEntity;
import cn.yanque.models.order.prepay.pojo.info.UpdateOrderStatusInfo;
import cn.yanque.models.order.prepay.service.OrderService;
import cn.yanque.studentFront.biz.StudentOrderBiz;
import cn.yanque.studentFront.pojo.req.CreatePaymentOrderReq;
import cn.yanque.studentFront.pojo.res.CreateOrderNoRes;
import cn.yanque.studentFront.pojo.res.CreatePaymentOrderRes;
import cn.yanque.studentFront.pojo.res.StudentLoginRes;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class StudentOrderBizImpl implements StudentOrderBiz {

    @Autowired
    private OrderService orderService;

    @Autowired
    private YeepayCashierService yeepayCashierService;

    @Override
    public CreateOrderNoRes createOrderNo() {
        CreateOrderNoRes createOrderNoRes = new CreateOrderNoRes();
        createOrderNoRes.setOrderNo("1010"+ DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN) + "0101" + RandomUtil.randomNumbers(6));
        return createOrderNoRes;
    }

    @Override
    public CreatePaymentOrderRes createPaymentOrder(CreatePaymentOrderReq req) {

        // 组装参数，入库
        OrderEntity orderEntity = new OrderEntity();
        BeanUtils.copyProperties(req, orderEntity);
        orderEntity.setStatus(OrderStatusEnum.INIT.name());
        orderEntity.setCreatedAt(new Date());
        orderEntity.setUpdatedAt(new Date());

        orderService.saveOrder(orderEntity);


        YeepayUnifiedOrderReq yeepayUnifiedOrderReq = new YeepayUnifiedOrderReq();
        yeepayUnifiedOrderReq.setOrderNo(orderEntity.getOrderNo());
        yeepayUnifiedOrderReq.setOrderAmount(orderEntity.getOrderAmount());
        YeepayUnifiedOrderRes res;
        try {
            res = yeepayCashierService.unifiedOrder(yeepayUnifiedOrderReq);
        } catch (Exception e) {
            // 更新订单状态信息为失败
            orderService.updateOrderStatus(new UpdateOrderStatusInfo(orderEntity.getOrderNo(), OrderStatusEnum.FAIL.name(), OrderStatusEnum.INIT.name(), null, null));
            throw BusinessException.DateError.newInstance("创建支付订单失败");
        }

        // 更新订单状态为支付中
        orderService.updateOrderStatus(new UpdateOrderStatusInfo(orderEntity.getOrderNo(), OrderStatusEnum.PROCESSING.name(), OrderStatusEnum.INIT.name(), res.getUniqueOrderNo(), null));


        ThreadPoolConfig.getScheduledPool().schedule(new Runnable() {
            @Override
            public void run() {
                // 判断订单是否超时，如果超时，订单状态改为 订单超时



            }
        }, 15, TimeUnit.MINUTES);
        CreatePaymentOrderRes createPaymentOrderRes = new CreatePaymentOrderRes();
        BeanUtils.copyProperties(res, createPaymentOrderRes);
        return createPaymentOrderRes;
    }


}
