package cn.yanque.models.order.refund.service.impl;

import cn.yanque.common.api.PageResult;
import cn.yanque.common.enums.RefundStatusEnum;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.order.refund.mapper.RefundOrderMapper;
import cn.yanque.models.order.refund.pojo.bo.QueryRefundOrderBo;
import cn.yanque.models.order.refund.pojo.entity.RefundOrderEntity;
import cn.yanque.models.order.refund.pojo.info.UpdateRefundStatusInfo;
import cn.yanque.models.order.refund.pojo.vo.req.RefundOrderPageReq;
import cn.yanque.models.order.refund.pojo.vo.res.RefundOrderPageRes;
import cn.yanque.models.order.refund.service.RefundOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RefundOrderServiceImpl implements RefundOrderService {

    @Autowired
    private RefundOrderMapper refundOrderMapper;

    @Override
    public void saveRefundOrder(RefundOrderEntity entity) {
        int rows = refundOrderMapper.insert(entity);
        if (rows != 1) {
            throw BusinessException.DateError.newInstance("创建退款订单失败");
        }
    }

    @Override
    public RefundOrderEntity selectByRefundOrderNo(String refundOrderNo) {
        return refundOrderMapper.selectByRefundOrderNo(refundOrderNo);
    }

    @Override
    public PageResult<RefundOrderPageRes> pageRefundOrder(RefundOrderPageReq req) {
        int pageNum = req.getPageNum() == null ? 1 : req.getPageNum();
        int pageSize = req.getPageSize() == null ? 10 : req.getPageSize();
        QueryRefundOrderBo queryRefundOrderBo = new QueryRefundOrderBo();
        BeanUtils.copyProperties(req, queryRefundOrderBo);
        PageHelper.startPage(pageNum, pageSize);
        List<RefundOrderEntity> list = refundOrderMapper.selectPage(queryRefundOrderBo);
        PageInfo<RefundOrderEntity> pageInfo = new PageInfo<>(list);
        List<RefundOrderPageRes> records = list.stream().map(this::buildRefundOrderPageRes).toList();
        return new PageResult<>(pageInfo.getTotal(), pageNum, pageSize, records);
    }

    @Override
    public void updateRefundProcessing(String refundOrderNo, String oldStatus, String uniqueRefundNo) {
        UpdateRefundStatusInfo update = new UpdateRefundStatusInfo();
        update.setRefundOrderNo(refundOrderNo);
        update.setStatus(RefundStatusEnum.PROCESSING.name());
        update.setOldStatus(oldStatus);
        update.setUniqueRefundNo(uniqueRefundNo);
        int rows = refundOrderMapper.updateRefundStatus(update);
        if (rows != 1) {
            throw BusinessException.DateError.newInstance("更新退款订单状态失败");
        }
    }

    @Override
    public void updateRefundSuccess(String refundOrderNo, String oldStatus,  String uniqueRefundNo,Date refundSuccessTime) {
        UpdateRefundStatusInfo update = new UpdateRefundStatusInfo();
        update.setRefundOrderNo(refundOrderNo);
        update.setStatus(RefundStatusEnum.SUCCESS.name());
        update.setUniqueRefundNo(uniqueRefundNo);
        update.setOldStatus(oldStatus);
        update.setRefundSuccessTime(refundSuccessTime);
        int rows = refundOrderMapper.updateRefundStatus(update);
        if (rows != 1) {
            throw BusinessException.DateError.newInstance("更新退款订单状态失败");
        }
    }

    @Override
    public void updateRefundFail(String refundOrderNo, String oldStatus, String failReason) {
        UpdateRefundStatusInfo update = new UpdateRefundStatusInfo();
        update.setRefundOrderNo(refundOrderNo);
        update.setStatus(RefundStatusEnum.FAIL.name());
        update.setOldStatus(oldStatus);
        update.setFailReason(failReason);
        int rows = refundOrderMapper.updateRefundStatus(update);
        if (rows != 1) {
            throw BusinessException.DateError.newInstance("更新退款订单状态失败");
        }
    }

    private RefundOrderPageRes buildRefundOrderPageRes(RefundOrderEntity refundOrder) {
        RefundOrderPageRes res = new RefundOrderPageRes();
        BeanUtils.copyProperties(refundOrder, res);
        return res;
    }
}
