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
import cn.yanque.models.order.product.mapper.ProductMapper;
import cn.yanque.models.order.product.pojo.entity.ProductEntity;
import cn.yanque.models.order.prepay.pojo.entity.OrderEntity;
import cn.yanque.models.order.prepay.pojo.info.UpdateOrderStatusInfo;
import cn.yanque.models.order.prepay.service.OrderService;
import cn.yanque.studentFront.biz.StudentOrderBiz;
import cn.yanque.studentFront.pojo.req.CreatePaymentOrderReq;
import cn.yanque.studentFront.pojo.res.CreateOrderNoRes;
import cn.yanque.studentFront.pojo.res.CreatePaymentOrderRes;
import cn.yanque.studentFront.pojo.res.PaymentReturnInfoRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class StudentOrderBizImpl implements StudentOrderBiz {

    @Autowired
    private OrderService orderService;

    @Autowired
    private YeepayCashierService yeepayCashierService;

    @Autowired
    private ProductMapper productMapper;

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

        // 调用易宝接口
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

        scheduleOrderTimeoutCheck(orderEntity.getOrderNo());
        CreatePaymentOrderRes createPaymentOrderRes = new CreatePaymentOrderRes();
        BeanUtils.copyProperties(res, createPaymentOrderRes);
        return createPaymentOrderRes;
    }

    @Override
    public PaymentReturnInfoRes paymentReturnInfo(String orderNo) {
        OrderEntity order = orderService.selectByOrderNo(orderNo);
        if (order == null) {
            throw BusinessException.DateError.newInstance("支付订单不存在");
        }
        if (!OrderStatusEnum.SUCCESS.name().equals(order.getStatus())) {
            throw BusinessException.DateError.newInstance("订单未支付成功");
        }

        PaymentReturnInfoRes res = new PaymentReturnInfoRes();
        BeanUtils.copyProperties(order, res);
        ProductEntity product = productMapper.selectById(Long.valueOf(order.getProductId()));
        if (product != null) {
            res.setProductContent(product.getCourseContent());
        }
        return res;
    }

    private void scheduleOrderTimeoutCheck(String orderNo) {
        ThreadPoolConfig.getScheduledPool().schedule(() -> {
            try {
                OrderEntity latestOrder = orderService.selectByOrderNo(orderNo);
                if (latestOrder == null || !OrderStatusEnum.PROCESSING.name().equals(latestOrder.getStatus())) {
                    return;
                }
                orderService.updateOrderStatus(new UpdateOrderStatusInfo(orderNo, OrderStatusEnum.TIMEOUT.name(), OrderStatusEnum.PROCESSING.name(), null, null));
            } catch (Exception e) {
                log.error("支付订单超时检查失败, orderNo={}", orderNo, e);
            }
        }, 15, TimeUnit.MINUTES);
    }


}
