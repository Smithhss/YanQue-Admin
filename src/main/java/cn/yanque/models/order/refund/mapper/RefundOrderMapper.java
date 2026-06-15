package cn.yanque.models.order.refund.mapper;

import cn.yanque.models.order.refund.pojo.entity.RefundOrderEntity;
import cn.yanque.models.order.refund.pojo.bo.QueryRefundOrderBo;
import cn.yanque.models.order.refund.pojo.info.UpdateRefundStatusInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RefundOrderMapper {

    int insert(RefundOrderEntity entity);

    RefundOrderEntity selectByRefundOrderNo(@Param("refundOrderNo") String refundOrderNo);

    List<RefundOrderEntity> selectPage(QueryRefundOrderBo queryRefundOrderBo);

    int updateRefundStatus(UpdateRefundStatusInfo updateRefundStatusInfo);

}
