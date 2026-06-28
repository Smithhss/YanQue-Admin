package cn.yanque.models.order.prepay.mapper;

import cn.yanque.models.order.prepay.pojo.entity.PrepayOrderEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预支付订单数据访问接口。
 */
public interface PrepayOrderMapper {

    /**
     * 插入预支付订单。
     *
     * @param order 预支付订单实体
     */
    void insert(PrepayOrderEntity order);

    /**
     * 按ID更新预支付订单。
     *
     * @param order 预支付订单实体
     * @return 影响行数
     */
    int updateById(PrepayOrderEntity order);

    /**
     * 按ID查询预支付订单。
     *
     * @param id 订单ID
     * @return 预支付订单实体
     */
    PrepayOrderEntity selectById(@Param("id") Long id);

    /**
     * 按订单号查询预支付订单。
     *
     * @param orderNo 预支付订单号
     * @return 预支付订单实体
     */
    PrepayOrderEntity selectByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 查询预支付订单列表,分页由PageHelper在Service层控制。
     *
     * @param keyword     学生姓名或手机号关键字
     * @param orderStatus 订单状态
     * @return 预支付订单列表
     */
    List<PrepayOrderEntity> selectPage(@Param("keyword") String keyword, @Param("orderStatus") String orderStatus);

    /**
     * 按手机号和状态查询最近一条订单。
     *
     * @param studentPhone 学生手机号
     * @param orderStatus  订单状态
     * @return 最近一条订单
     */
    PrepayOrderEntity selectLatestByPhoneAndStatus(@Param("studentPhone") String studentPhone, @Param("orderStatus") String orderStatus);

    /**
     * 按手机号查询最近一条订单。
     *
     * @param studentPhone 学生手机号
     * @return 最近一条订单
     */
    PrepayOrderEntity selectLatestByPhone(@Param("studentPhone") String studentPhone);

    /**
     * 按ID删除预支付订单。
     *
     * @param id 订单ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 将预支付订单状态更新为SUCCESS。
     * WHERE条件包含order_status = 'PENDING_PAYMENT', 防止已支付/已退款订单被重复覆盖。
     *
     * @param prepayOrderNo 预支付订单号
     * @return 影响行数(0表示订单不存在或状态非PENDING_PAYMENT)
     */
    int updatePrepayOrderSuccess(@Param("prepayOrderNo") String prepayOrderNo);
}
