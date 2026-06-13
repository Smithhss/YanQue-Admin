package cn.yanque.integration.yeepay.service;

import cn.yanque.integration.yeepay.pojo.req.YeepayUnifiedOrderReq;
import cn.yanque.integration.yeepay.pojo.res.YeepayUnifiedOrderRes;

public interface YeepayCashierService {

    YeepayUnifiedOrderRes unifiedOrder(YeepayUnifiedOrderReq req);
}
