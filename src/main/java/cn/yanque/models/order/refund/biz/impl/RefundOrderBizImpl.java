package cn.yanque.models.order.refund.biz.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.yanque.common.enums.OrderStatusEnum;
import cn.yanque.common.enums.RefundStatusEnum;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.integration.yeepay.pojo.req.YeepayRefundReq;
import cn.yanque.integration.yeepay.pojo.res.YeepayRefundRes;
import cn.yanque.integration.yeepay.service.YeepayCashierService;
import cn.yanque.models.order.prepay.pojo.entity.OrderEntity;
import cn.yanque.models.order.prepay.service.OrderService;
import cn.yanque.models.order.refund.biz.RefundOrderBiz;
import cn.yanque.models.order.refund.pojo.entity.RefundOrderEntity;
import cn.yanque.models.order.refund.pojo.vo.req.RefundApplyReq;
import cn.yanque.models.order.refund.pojo.vo.res.RefundApplyRes;
import cn.yanque.models.order.refund.pojo.vo.res.RefundCreateRes;
import cn.yanque.models.order.refund.service.RefundOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class RefundOrderBizImpl implements RefundOrderBiz {

    @Autowired
    private RefundOrderService refundOrderService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private YeepayCashierService yeepayCashierService;

    @Override
    public RefundCreateRes createRefundOrder() {
        RefundCreateRes res = new RefundCreateRes();
        res.setRefundOrderNo(createRefundOrderNo());
        return res;
    }

    @Override
    public RefundApplyRes applyRefund(String refundOrderNo, RefundApplyReq req) {

        // 获取原单信息
        OrderEntity order = getRefundablePaymentOrder(req.getPaymentOrderNo());

        // 保存退款单信息
        RefundOrderEntity existedRefundOrder = saveApplyingRefundOrder(refundOrderNo, req, order);
        if (existedRefundOrder != null) {
            return buildRefundApplyRes(existedRefundOrder);
        }

        // 更新原单已退金额
        try {
            orderService.increaseRefundedAmount(order.getOrderNo(), req.getRefundAmount());
        } catch (BusinessException e) {
            refundOrderService.updateRefundFail(refundOrderNo, RefundStatusEnum.INIT.name(), e.getMessage());
            throw e;
        }

        YeepayRefundRes yeepayRefundRes;
        try {
            yeepayRefundRes = requestYeepayRefund(order, refundOrderNo, req);
        } catch (Exception e) {
            orderService.decreaseRefundedAmount(order.getOrderNo(), req.getRefundAmount());
            refundOrderService.updateRefundFail(refundOrderNo, RefundStatusEnum.INIT.name(), e.getMessage());
            throw BusinessException.RemoteError.newInstance("申请退款失败：" + e.getMessage());
        }
        refundOrderService.updateRefundProcessing(refundOrderNo, RefundStatusEnum.INIT.name(), yeepayRefundRes.getUniqueRefundNo());
        return buildRefundApplyRes(refundOrderNo, order.getOrderNo(), req.getRefundAmount(), yeepayRefundRes);
    }

    @Override
    public void handleRefundSuccess(String refundOrderNo, String uniqueRefundNo, Date refundSuccessTime) {
        RefundOrderEntity refundOrder = getRefundOrder(refundOrderNo);
        if (RefundStatusEnum.SUCCESS.name().equals(refundOrder.getStatus())) {
            return;
        }
        if (!RefundStatusEnum.PROCESSING.name().equals(refundOrder.getStatus())) {
            throw BusinessException.DateError.newInstance("退款订单状态不允许更新为成功");
        }
        refundOrderService.updateRefundSuccess(refundOrderNo, RefundStatusEnum.PROCESSING.name(), uniqueRefundNo, refundSuccessTime);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleRefundFail(String refundOrderNo, String failReason) {
        RefundOrderEntity refundOrder = getRefundOrder(refundOrderNo);
        if (RefundStatusEnum.FAIL.name().equals(refundOrder.getStatus())) {
            return;
        }
        if (!RefundStatusEnum.PROCESSING.name().equals(refundOrder.getStatus())) {
            throw BusinessException.DateError.newInstance("退款订单状态不允许更新为失败");
        }
        refundOrderService.updateRefundFail(refundOrderNo, RefundStatusEnum.PROCESSING.name(), failReason);
        orderService.decreaseRefundedAmount(refundOrder.getPaymentOrderNo(), refundOrder.getRefundAmount());
    }

    private OrderEntity getRefundablePaymentOrder(String paymentOrderNo) {
        OrderEntity order = orderService.selectByOrderNo(paymentOrderNo);
        if (order == null) {
            throw BusinessException.DateError.newInstance("支付订单不存在");
        }
        if (!OrderStatusEnum.SUCCESS.name().equals(order.getStatus())) {
            throw BusinessException.DateError.newInstance("只有支付成功订单可以退款");
        }
        return order;
    }

    private RefundOrderEntity getRefundOrder(String refundOrderNo) {
        RefundOrderEntity refundOrder = refundOrderService.selectByRefundOrderNo(refundOrderNo);
        if (refundOrder == null) {
            throw BusinessException.DateError.newInstance("退款订单不存在");
        }
        return refundOrder;
    }

    private RefundOrderEntity saveApplyingRefundOrder(String refundOrderNo, RefundApplyReq req, OrderEntity order) {
        RefundOrderEntity refundOrder = new RefundOrderEntity();
        refundOrder.setRefundOrderNo(refundOrderNo);
        refundOrder.setPaymentOrderNo(order.getOrderNo());
        refundOrder.setPaymentAmount(order.getOrderAmount());
        refundOrder.setRefundAmount(req.getRefundAmount());
        refundOrder.setReason(req.getReason());
        refundOrder.setStatus(RefundStatusEnum.INIT.name());
        refundOrder.setCreatedAt(new Date());
        refundOrder.setUpdatedAt(new Date());
        try {
            refundOrderService.saveRefundOrder(refundOrder);
            return null;
        } catch (DuplicateKeyException e) {
            RefundOrderEntity existed = refundOrderService.selectByRefundOrderNo(refundOrderNo);
            if (existed == null) {
                throw BusinessException.DateError.newInstance("退款订单号已存在");
            }
            if (!order.getOrderNo().equals(existed.getPaymentOrderNo()) || existed.getRefundAmount() == null || existed.getRefundAmount().compareTo(req.getRefundAmount()) != 0) {
                throw BusinessException.DateError.newInstance("退款订单号已被其他退款申请使用");
            }
            return existed;
        }
    }

    private YeepayRefundRes requestYeepayRefund(OrderEntity order, String refundOrderNo, RefundApplyReq req) {
        YeepayRefundReq yeepayRefundReq = new YeepayRefundReq();
        yeepayRefundReq.setOrderNo(order.getOrderNo());
        yeepayRefundReq.setUniqueOrderNo(order.getUniqueOrderNo());
        yeepayRefundReq.setRefundOrderNo(refundOrderNo);
        yeepayRefundReq.setRefundAmount(req.getRefundAmount());
        yeepayRefundReq.setReason(req.getReason());
        return yeepayCashierService.refund(yeepayRefundReq);
    }

    private RefundApplyRes buildRefundApplyRes(String refundOrderNo, String paymentOrderNo, BigDecimal refundAmount, YeepayRefundRes yeepayRefundRes) {
        RefundApplyRes res = new RefundApplyRes();
        res.setRefundOrderNo(refundOrderNo);
        res.setPaymentOrderNo(paymentOrderNo);
        res.setRefundAmount(refundAmount);
        res.setStatus(RefundStatusEnum.PROCESSING.name());
        res.setUniqueRefundNo(yeepayRefundRes.getUniqueRefundNo());
        return res;
    }

    private RefundApplyRes buildRefundApplyRes(RefundOrderEntity refundOrder) {
        RefundApplyRes res = new RefundApplyRes();
        res.setRefundOrderNo(refundOrder.getRefundOrderNo());
        res.setPaymentOrderNo(refundOrder.getPaymentOrderNo());
        res.setRefundAmount(refundOrder.getRefundAmount());
        res.setStatus(refundOrder.getStatus());
        res.setUniqueRefundNo(refundOrder.getUniqueRefundNo());
        return res;
    }

    private String createRefundOrderNo() {
        return "1011" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN) + RandomUtil.randomNumbers(6);
    }
}
