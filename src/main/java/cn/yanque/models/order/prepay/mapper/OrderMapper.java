package cn.yanque.models.order.prepay.mapper;

import cn.yanque.models.order.prepay.pojo.bo.QueryOrderBo;
import cn.yanque.models.order.prepay.pojo.entity.OrderEntity;
import cn.yanque.models.order.prepay.pojo.info.UpdateOrderStatusInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface OrderMapper {
    int insert(OrderEntity entity);

    int updateOrderStatus(UpdateOrderStatusInfo updateOrderStatusInfo);

    OrderEntity selectByOrderNo(@Param("orderNo") String orderNo);

    List<OrderEntity> selectPage(QueryOrderBo queryOrderBo);

    int increaseRefundedAmount(@Param("orderNo") String orderNo, @Param("refundAmount") BigDecimal refundAmount);

    int decreaseRefundedAmount(@Param("orderNo") String orderNo, @Param("refundAmount") BigDecimal refundAmount);
}
