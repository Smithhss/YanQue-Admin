package cn.yanque.integration.yeepay.handle;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.yanque.integration.yeepay.pojo.Info.YeepayRefundInfo;
import cn.yanque.models.order.refund.biz.RefundOrderBiz;
import com.alibaba.fastjson2.JSONObject;
import com.yeepay.yop.sdk.service.common.callback.YopCallback;
import com.yeepay.yop.sdk.service.common.callback.handler.YopCallbackHandler;
import com.yeepay.yop.sdk.service.common.callback.handler.YopCallbackHandlerFactory;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

@Component
@Slf4j
public class YeepayRefundHandle implements YopCallbackHandler {


    @Autowired
    private RefundOrderBiz refundOrderBiz;
    

    @PostConstruct
    public void init() {
        YopCallbackHandlerFactory.register(getType(), this);
    }

    @Override
    public String getType() {
        return "/yq-admin/yop-callback/refund";
    }

    @Override
    public void handle(YopCallback yopCallback) {
        log.info("yeepay 接收退款通知 yopCallback:{}", yopCallback.getBizData());
        JSONObject bizData = JSONObject.parseObject(yopCallback.getBizData());
        YeepayRefundInfo refundInfo = bizData.toJavaObject(YeepayRefundInfo.class);
        String uniqueRefundNo = refundInfo.getUniqueRefundNo();
        String refundRequestId = refundInfo.getRefundRequestId();
        if (StrUtil.isBlank(refundRequestId)) {
            log.error("yeepay 接收退款通知缺少退款订单号, bizData={}", yopCallback.getBizData());
            return;
        }

        String status = refundInfo.getStatus();
        if (status.equals("SUCCESS")) {
            Date refundSuccessTime = DateUtil.parse(refundInfo.getRefundSuccessDate(), DatePattern.NORM_DATETIME_PATTERN);
            refundOrderBiz.handleRefundSuccess(refundRequestId, uniqueRefundNo, refundSuccessTime);
            return;
        }
        if (status.equals("FAILED")) {
            refundOrderBiz.handleRefundFail(refundRequestId, refundInfo.getErrorMessage());
            return;
        }
        log.info("yeepay 退款通知状态未完成, refundRequestId={}, status={}", refundRequestId, status);
    }



}
