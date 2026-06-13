package cn.yanque.models.order.prepay.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.order.prepay.pojo.vo.req.PrepayOrderCreateReq;
import cn.yanque.models.order.prepay.pojo.vo.req.PrepayOrderPageReq;
import cn.yanque.models.order.prepay.pojo.vo.req.PrepayOrderUpdateReq;
import cn.yanque.models.order.prepay.pojo.vo.res.PrepayOrderCreateRes;
import cn.yanque.models.order.prepay.pojo.vo.res.PrepayOrderDeleteRes;
import cn.yanque.models.order.prepay.pojo.vo.res.PrepayOrderDetailRes;
import cn.yanque.models.order.prepay.pojo.vo.res.PrepayOrderPageRes;
import cn.yanque.models.order.prepay.pojo.vo.res.PrepayOrderUpdateRes;

/**
 * 预支付订单管理服务。
 */
public interface PrepayOrderService {

    PrepayOrderCreateRes addPrepayOrder(PrepayOrderCreateReq req);

    PrepayOrderUpdateRes updatePrepayOrder(PrepayOrderUpdateReq req);

    PrepayOrderDeleteRes deletePrepayOrder(Long id);

    PrepayOrderDetailRes getPrepayOrderById(Long id);

    PageResult<PrepayOrderPageRes> pagePrepayOrder(PrepayOrderPageReq req);

    void updatePrepayOrderSuccess(String prepayOrderNo);
}
