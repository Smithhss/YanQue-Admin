package cn.yanque.integration.yeepay.handle;

import com.yeepay.yop.sdk.service.common.callback.YopCallback;
import com.yeepay.yop.sdk.service.common.callback.handler.YopCallbackHandler;
import com.yeepay.yop.sdk.service.common.callback.handler.YopCallbackHandlerFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "payment", name = "provider", havingValue = "yeepay")
public class YeepaySettleSuccessHandle implements YopCallbackHandler {

    @PostConstruct
    public void init() {
        YopCallbackHandlerFactory.register(getType(), this);
    }

    @Override
    public String getType() {
        return "settle";
    }

    @Override
    public void handle(YopCallback yopCallback) {
        // 处理结算成功通知
    }

}
