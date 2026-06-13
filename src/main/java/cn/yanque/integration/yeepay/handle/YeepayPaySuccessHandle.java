package cn.yanque.integration.yeepay.handle;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.yanque.common.enums.OrderStatusEnum;
import cn.yanque.integration.yeepay.pojo.Info.YeepayPaySuccessInfo;
import cn.yanque.models.order.prepay.pojo.entity.OrderEntity;
import cn.yanque.models.order.prepay.pojo.info.UpdateOrderStatusInfo;
import cn.yanque.models.order.prepay.service.OrderService;
import cn.yanque.models.order.prepay.service.PrepayOrderService;
import com.alibaba.fastjson2.JSONObject;
import com.yeepay.yop.sdk.service.common.callback.YopCallback;
import com.yeepay.yop.sdk.service.common.callback.handler.YopCallbackHandler;
import com.yeepay.yop.sdk.service.common.callback.handler.YopCallbackHandlerFactory;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class YeepayPaySuccessHandle implements YopCallbackHandler {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PrepayOrderService prepayOrderService;

    @PostConstruct
    public void init() {
        YopCallbackHandlerFactory.register(getType(), this);
    }

    @Override
    public String getType() {
        return "/yq-admin/yop-callback/paySuccess";
    }

    @Override
    public void handle(YopCallback yopCallback) {


        // 处理支付成功通知
        log.info("yeepay 接收支付成功通知 yopCallback:{}", yopCallback.getBizData());
        YeepayPaySuccessInfo yeepayPaySuccessInfo = JSONObject.parseObject(yopCallback.getBizData(), YeepayPaySuccessInfo.class);

        OrderEntity orderEntity = orderService.selectByOrderNo(yeepayPaySuccessInfo.getOrderId());
        if (orderEntity == null) {
            log.error("yeepay 接收支付成功通知 未查询到订单");
            return;
        }

        Date paySuccessTime = DateUtil.parse(yeepayPaySuccessInfo.getPaySuccessDate(), DatePattern.NORM_DATETIME_PATTERN);

        orderService.updateOrderStatus(new UpdateOrderStatusInfo(yeepayPaySuccessInfo.getOrderId(), OrderStatusEnum.SUCCESS.name(), OrderStatusEnum.PROCESSING.name(), null, paySuccessTime));

        prepayOrderService.updatePrepayOrderSuccess(orderEntity.getPrepayOrderNo());

    }
}
