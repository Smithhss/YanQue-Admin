package cn.yanque.studentFront.biz;

import cn.yanque.studentFront.pojo.req.CreatePaymentOrderReq;
import cn.yanque.studentFront.pojo.res.CreateOrderNoRes;
import cn.yanque.studentFront.pojo.res.CreatePaymentOrderRes;

public interface StudentOrderBiz {
    CreateOrderNoRes createOrderNo();

    CreatePaymentOrderRes createPaymentOrder(CreatePaymentOrderReq req);
}
