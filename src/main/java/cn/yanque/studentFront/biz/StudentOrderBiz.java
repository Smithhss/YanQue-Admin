package cn.yanque.studentFront.biz;

import cn.yanque.studentFront.pojo.req.CreatePaymentOrderReq;
import cn.yanque.studentFront.pojo.res.CreateOrderNoRes;
import cn.yanque.studentFront.pojo.res.CreatePaymentOrderRes;
import cn.yanque.studentFront.pojo.res.PaymentReturnInfoRes;

public interface StudentOrderBiz {
    CreateOrderNoRes createOrderNo();

    CreatePaymentOrderRes createPaymentOrder(CreatePaymentOrderReq req);

    PaymentReturnInfoRes paymentReturnInfo(String orderNo);
}
