package cn.yanque.models.order.prepay.service.impl;

import cn.yanque.common.api.PageResult;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.order.product.mapper.ProductMapper;
import cn.yanque.models.order.product.pojo.entity.ProductEntity;
import cn.yanque.models.order.prepay.mapper.OrderMapper;
import cn.yanque.models.order.prepay.pojo.bo.QueryOrderBo;
import cn.yanque.models.order.prepay.pojo.entity.OrderEntity;
import cn.yanque.models.order.prepay.pojo.info.UpdateOrderStatusInfo;
import cn.yanque.models.order.prepay.pojo.vo.req.OrderPageReq;
import cn.yanque.models.order.prepay.pojo.vo.res.OrderPageRes;
import cn.yanque.models.order.prepay.service.OrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductMapper productMapper;


    @Override
    public void saveOrder(OrderEntity entity) {
        int i;
        try {
            i = orderMapper.insert(entity);
        } catch (DuplicateKeyException e) {
            throw BusinessException.DateExist.newInstance("订单已存在,请勿重复下单");
        }

        if (i != 1){
            throw BusinessException.DateExist.newInstance("下单保存订单失败");
        }
    }

    @Override
    public void updateOrderStatus(UpdateOrderStatusInfo updateOrderStatusInfo) {
        int i = orderMapper.updateOrderStatus(updateOrderStatusInfo);
        if (i != 1) {
            throw BusinessException.DateExist.newInstance("更新订单状态失败,订单状态已经发生变化");
        }
    }

    @Override
    public OrderEntity selectByOrderNo(String orderNo) {
        return orderMapper.selectByOrderNo(orderNo);
    }

    @Override
    public PageResult<OrderPageRes> pageOrder(OrderPageReq req) {
        int pageNum = req.getPageNum() == null ? 1 : req.getPageNum();
        int pageSize = req.getPageSize() == null ? 10 : req.getPageSize();
        QueryOrderBo queryOrderBo = buildQueryOrderBo(req);
        PageHelper.startPage(pageNum, pageSize);
        List<OrderEntity> list = orderMapper.selectPage(queryOrderBo);
        PageInfo<OrderEntity> pageInfo = new PageInfo<>(list);
        Map<String, ProductEntity> productMap = getProductMap(list);
        List<OrderPageRes> records = list.stream().map(order -> buildOrderPageRes(order, productMap)).toList();
        return new PageResult<>(pageInfo.getTotal(), pageNum, pageSize, records);
    }

    private QueryOrderBo buildQueryOrderBo(OrderPageReq req) {
        QueryOrderBo queryOrderBo = new QueryOrderBo();
        BeanUtils.copyProperties(req, queryOrderBo);
        return queryOrderBo;
    }

    private Map<String, ProductEntity> getProductMap(List<OrderEntity> orders) {
        List<String> productIds = orders.stream()
                .map(OrderEntity::getProductId)
                .filter(Objects::nonNull)
                .filter(productId -> !productId.isBlank())
                .distinct()
                .toList();
        if (productIds.isEmpty()) {
            return Map.of();
        }
        return productMapper.selectByIds(productIds)
                .stream()
                .collect(Collectors.toMap(product -> String.valueOf(product.getId()), Function.identity(), (left, right) -> left));
    }

    private OrderPageRes buildOrderPageRes(OrderEntity order, Map<String, ProductEntity> productMap) {
        OrderPageRes res = new OrderPageRes();
        BeanUtils.copyProperties(order, res);
        ProductEntity product = productMap.get(order.getProductId());
        if (product != null) {
            res.setProductContent(product.getCourseContent());
        }
        return res;
    }
}
