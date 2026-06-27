package cn.yanque.models.order.refund.biz.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.yanque.common.enums.OrderStatusEnum;
import cn.yanque.common.enums.RefundStatusEnum;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.integration.payment.pojo.req.PaymentRefundReq;
import cn.yanque.integration.payment.pojo.res.PaymentRefundRes;
import cn.yanque.integration.payment.service.PaymentCashierService;
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

/**
 * 退款业务核心实现。
 *
 * 退款流程（两步式）：
 *   1. createRefundOrder() → 生成退款单号返回前端
 *   2. applyRefund()       → 用户确认后提交退款申请
 *
 * 状态流转：
 *   INIT → PROCESSING → SUCCESS
 *                    → FAIL（需回退已退金额）
 *
 * 设计要点：
 *   - 幂等：DuplicateKeyException 处理前端重复点击
 *   - 乐观锁：refunded_amount + 退款金额 <= order_amount，防止超额退款
 *   - 失败回退：退款失败时 decreaseRefundedAmount 恢复额度
 */
@Service
public class RefundOrderBizImpl implements RefundOrderBiz {

    @Autowired
    private RefundOrderService refundOrderService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentCashierService paymentCashierService;

    /**
     * 第一步：创建退款单号。
     * 只生成单号，不做任何业务操作。
     * 前端拿到单号后展示给用户确认，确认后再调 applyRefund()。
     */
    @Override
    public RefundCreateRes createRefundOrder() {
        RefundCreateRes res = new RefundCreateRes();
        res.setRefundOrderNo(createRefundOrderNo());
        return res;
    }

    /**
     * 第二步：提交退款申请（核心方法）。
     *
     * 流程：校验原单 → 保存退款单 → 更新已退金额 → 调易宝退款 → 更新状态为处理中
     *
     * @param refundOrderNo 退款单号（第一步生成的）
     * @param req           退款请求（原支付单号、退款金额、原因）
     */
    @Override
    public RefundApplyRes applyRefund(String refundOrderNo, RefundApplyReq req) {

        // 1. 校验原支付单：必须存在且状态为 SUCCESS
        OrderEntity order = getRefundablePaymentOrder(req.getPaymentOrderNo());

        // 2. 保存退款单（INIT 状态），DuplicateKeyException 实现幂等
        //    如果同一退款单号+同一订单+同一金额重复提交，直接返回已有结果
        RefundOrderEntity existedRefundOrder = saveApplyingRefundOrder(refundOrderNo, req, order);
        if (existedRefundOrder != null) {
            return buildRefundApplyRes(existedRefundOrder);
        }

        // 3. 更新原单已退金额（乐观锁：refunded_amount + 退款金额 <= order_amount）
        //    失败则标记退款单为 FAIL 并抛出异常
        try {
            orderService.increaseRefundedAmount(order.getOrderNo(), req.getRefundAmount());
        } catch (BusinessException e) {
            refundOrderService.updateRefundFail(refundOrderNo, RefundStatusEnum.INIT.name(), e.getMessage());
            throw e;
        }

        // 4. 调用易宝退款接口
        //    失败则：回退已退金额 + 标记退款单为 FAIL
        PaymentRefundRes paymentRefundRes;
        try {
            paymentRefundRes = requestRefund(order, refundOrderNo, req);
        } catch (Exception e) {
            orderService.decreaseRefundedAmount(order.getOrderNo(), req.getRefundAmount());
            refundOrderService.updateRefundFail(refundOrderNo, RefundStatusEnum.INIT.name(), e.getMessage());
            throw BusinessException.RemoteError.newInstance("申请退款失败：" + e.getMessage());
        }

        // 5. 更新退款单状态为 PROCESSING，记录易宝退款流水号
        refundOrderService.updateRefundProcessing(refundOrderNo, RefundStatusEnum.INIT.name(), paymentRefundRes.getUniqueRefundNo());
        return buildRefundApplyRes(refundOrderNo, order.getOrderNo(), req.getRefundAmount(), paymentRefundRes);
    }

    /**
     * 退款成功回调（由 YeepayRefundHandle 调用）。
     *
     * 幂等设计：
     *   - 已是 SUCCESS → 直接返回，不重复处理
     *   - 非 PROCESSING → 抛异常，防止非法状态跳转
     */
    @Override
    public void handleRefundSuccess(String refundOrderNo, String uniqueRefundNo, Date refundSuccessTime) {
        RefundOrderEntity refundOrder = getRefundOrder(refundOrderNo);
        // 幂等：已成功则忽略
        if (RefundStatusEnum.SUCCESS.name().equals(refundOrder.getStatus())) {
            return;
        }
        // 状态校验：只有 PROCESSING 才能更新为 SUCCESS
        if (!RefundStatusEnum.PROCESSING.name().equals(refundOrder.getStatus())) {
            throw BusinessException.DateError.newInstance("退款订单状态不允许更新为成功");
        }
        refundOrderService.updateRefundSuccess(refundOrderNo, RefundStatusEnum.PROCESSING.name(), uniqueRefundNo, refundSuccessTime);
    }

    /**
     * 退款失败回调（由 YeepayRefundHandle 调用）。
     *
     * 关键：失败时必须回退原单已退金额，否则这笔额度就"丢了"。
     * 使用 @Transactional 保证「标记失败」和「回退金额」的原子性。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleRefundFail(String refundOrderNo, String failReason) {
        RefundOrderEntity refundOrder = getRefundOrder(refundOrderNo);
        // 幂等：已失败则忽略
        if (RefundStatusEnum.FAIL.name().equals(refundOrder.getStatus())) {
            return;
        }
        // 状态校验：只有 PROCESSING 才能更新为 FAIL
        if (!RefundStatusEnum.PROCESSING.name().equals(refundOrder.getStatus())) {
            throw BusinessException.DateError.newInstance("退款订单状态不允许更新为失败");
        }
        // 标记退款单为 FAIL
        refundOrderService.updateRefundFail(refundOrderNo, RefundStatusEnum.PROCESSING.name(), failReason);
        // 回退原单已退金额（事务内，任一失败整体回滚）
        orderService.decreaseRefundedAmount(refundOrder.getPaymentOrderNo(), refundOrder.getRefundAmount());
    }

    /**
     * 获取可退款的原支付单。
     * 校验：订单存在 + 状态为 SUCCESS（只有支付成功的订单才能退款）。
     */
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

    /**
     * 获取退款单（用于回调场景）。
     */
    private RefundOrderEntity getRefundOrder(String refundOrderNo) {
        RefundOrderEntity refundOrder = refundOrderService.selectByRefundOrderNo(refundOrderNo);
        if (refundOrder == null) {
            throw BusinessException.DateError.newInstance("退款订单不存在");
        }
        return refundOrder;
    }

    /**
     * 保存退款申请单（INIT 状态）。
     *
     * 幂等处理：
     *   - 正常插入 → 返回 null（新申请）
     *   - DuplicateKeyException（主键冲突）→ 查询已有记录
     *     - 相同订单 + 相同金额 → 返回已有记录（幂等，防重复点击）
     *     - 不同订单或不同金额 → 报错（单号被占用）
     */
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
            return null; // 新申请，返回 null 表示需要继续后续流程
        } catch (DuplicateKeyException e) {
            // 主键冲突 → 可能是前端重复点击
            RefundOrderEntity existed = refundOrderService.selectByRefundOrderNo(refundOrderNo);
            if (existed == null) {
                throw BusinessException.DateError.newInstance("退款订单号已存在");
            }
            // 校验是否为同一笔退款（相同订单 + 相同金额）
            if (!order.getOrderNo().equals(existed.getPaymentOrderNo()) || existed.getRefundAmount() == null || existed.getRefundAmount().compareTo(req.getRefundAmount()) != 0) {
                throw BusinessException.DateError.newInstance("退款订单号已被其他退款申请使用");
            }
            return existed; // 幂等：返回已有记录
        }
    }

    /**
     * 组装易宝退款请求参数并调用退款接口。
     *
     * @param order         原支付单（提供 orderNo、uniqueOrderNo）
     * @param refundOrderNo 退款单号（作为易宝侧的退款请求标识）
     * @param req           退款请求（金额、原因）
     */
    private PaymentRefundRes requestRefund(OrderEntity order, String refundOrderNo, RefundApplyReq req) {
        PaymentRefundReq paymentRefundReq = new PaymentRefundReq();
        paymentRefundReq.setOrderNo(order.getOrderNo());
        paymentRefundReq.setUniqueOrderNo(order.getUniqueOrderNo());
        paymentRefundReq.setRefundOrderNo(refundOrderNo);
        paymentRefundReq.setRefundAmount(req.getRefundAmount());
        paymentRefundReq.setReason(req.getReason());
        return paymentCashierService.refund(paymentRefundReq);
    }

    /**
     * 构建退款申请响应（新申请场景）。
     * 状态固定为 PROCESSING，因为已成功调用易宝退款接口。
     */
    private RefundApplyRes buildRefundApplyRes(String refundOrderNo, String paymentOrderNo, BigDecimal refundAmount, PaymentRefundRes paymentRefundRes) {
        RefundApplyRes res = new RefundApplyRes();
        res.setRefundOrderNo(refundOrderNo);
        res.setPaymentOrderNo(paymentOrderNo);
        res.setRefundAmount(refundAmount);
        res.setStatus(RefundStatusEnum.PROCESSING.name());
        res.setUniqueRefundNo(paymentRefundRes.getUniqueRefundNo());
        return res;
    }

    /**
     * 构建退款申请响应（幂等场景：重复提交时返回已有记录）。
     * 状态取数据库实际值（可能是 INIT / PROCESSING / SUCCESS / FAIL）。
     */
    private RefundApplyRes buildRefundApplyRes(RefundOrderEntity refundOrder) {
        RefundApplyRes res = new RefundApplyRes();
        res.setRefundOrderNo(refundOrder.getRefundOrderNo());
        res.setPaymentOrderNo(refundOrder.getPaymentOrderNo());
        res.setRefundAmount(refundOrder.getRefundAmount());
        res.setStatus(refundOrder.getStatus());
        res.setUniqueRefundNo(refundOrder.getUniqueRefundNo());
        return res;
    }

    /**
     * 生成退款单号。
     * 格式：1011（退款标识）+ yyyyMMddHHmmss（时间）+ 6位随机数
     * 示例：101120260620143025123456
     */
    private String createRefundOrderNo() {
        return "1011" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN) + RandomUtil.randomNumbers(6);
    }
}
