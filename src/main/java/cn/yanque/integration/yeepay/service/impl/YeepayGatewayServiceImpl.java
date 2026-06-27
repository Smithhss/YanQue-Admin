package cn.yanque.integration.yeepay.service.impl;

import cn.yanque.common.exception.BusinessException;
import cn.yanque.config.YeepayProperties;
import cn.yanque.integration.yeepay.service.YeepayGatewayService;
import com.alibaba.fastjson2.JSONObject;
import com.yeepay.yop.sdk.service.common.YopClient;
import com.yeepay.yop.sdk.service.common.YopClientBuilder;
import com.yeepay.yop.sdk.service.common.request.YopRequest;
import com.yeepay.yop.sdk.service.common.response.YopResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@ConditionalOnProperty(prefix = "payment", name = "provider", havingValue = "yeepay")
public class YeepayGatewayServiceImpl implements YeepayGatewayService {


    private static volatile YopClient client = null;

    @Autowired
    private YeepayProperties yeepayProperties;


    @Override
    public JSONObject request(YopRequest request,  String type) {
        try {
            log.info("yeepay 发起调用,请求参数:{}", JSONObject.toJSONString(request));
            YopResponse response = getClient().request(request);
            log.info("yeepay 返回参数:{}", JSONObject.toJSONString(response));
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(response.getResult()));
            String code = jsonObject.getString("code");

            if (type.equals("cashier/order")) {
                if (!code.equals("00000")) {
                    throw BusinessException.RemoteError.newInstance("调用yeepay失败" + jsonObject.getString("message"));
                }
            }
            if (type.equals("refund")) {
                if (!code.equals("OPR00000")) {
                    throw BusinessException.RemoteError.newInstance("调用yeepay失败" + jsonObject.getString("message"));
                }
            }

            return jsonObject;
        } catch (Exception ex) {
            log.error("yeepay 异常, ex:", ex);
            throw BusinessException.PasswordError.newInstance("调用yeepay异常 e" + ex.getMessage());
        }
    }

    private YopClient getClient() {
        if (client == null) {
            synchronized (YopClient.class) {
                if (client == null) {
                    configureSdk();
                    client = YopClientBuilder.builder().build();
                }
            }
        }
        return client;

    }

    private void configureSdk() {
        if ("sandbox".equalsIgnoreCase(yeepayProperties.getMode())) {
            System.setProperty("yop.sdk.config.file", StringUtils.hasText(yeepayProperties.getSdkConfigFile())
                    ? yeepayProperties.getSdkConfigFile()
                    : "config/yeepay/sandbox/yop_sdk_config_default.json");
            return;
        }
        if (StringUtils.hasText(yeepayProperties.getSdkConfigFile())) {
            System.setProperty("yop.sdk.config.file", yeepayProperties.getSdkConfigFile());
        }
    }
}
