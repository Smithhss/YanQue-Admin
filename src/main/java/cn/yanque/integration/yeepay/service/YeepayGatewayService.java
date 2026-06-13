package cn.yanque.integration.yeepay.service;

import com.alibaba.fastjson2.JSONObject;
import com.yeepay.yop.sdk.service.common.request.YopRequest;

public interface YeepayGatewayService {

    JSONObject request(YopRequest request);
}
