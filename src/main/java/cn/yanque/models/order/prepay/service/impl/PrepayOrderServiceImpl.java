package cn.yanque.models.order.prepay.service.impl;

import cn.yanque.common.api.PageResult;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.order.prepay.mapper.PrepayOrderMapper;
import cn.yanque.models.order.prepay.pojo.entity.PrepayOrderEntity;
import cn.yanque.models.order.prepay.pojo.vo.req.PrepayOrderCreateReq;
import cn.yanque.models.order.prepay.pojo.vo.req.PrepayOrderPageReq;
import cn.yanque.models.order.prepay.pojo.vo.req.PrepayOrderUpdateReq;
import cn.yanque.models.order.prepay.pojo.vo.res.PrepayOrderCreateRes;
import cn.yanque.models.order.prepay.pojo.vo.res.PrepayOrderDeleteRes;
import cn.yanque.models.order.prepay.pojo.vo.res.PrepayOrderDetailRes;
import cn.yanque.models.order.prepay.pojo.vo.res.PrepayOrderPageRes;
import cn.yanque.models.order.prepay.pojo.vo.res.PrepayOrderUpdateRes;
import cn.yanque.models.order.prepay.service.PrepayOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class PrepayOrderServiceImpl implements PrepayOrderService {

    private static final String DEFAULT_ORDER_STATUS = "PENDING_PAYMENT";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Autowired
    private PrepayOrderMapper prepayOrderMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PrepayOrderCreateRes addPrepayOrder(PrepayOrderCreateReq req) {
        if (req.getDiscountAmount().compareTo(req.getProductAmount()) > 0) {
            throw BusinessException.ParamsError.newInstance("优惠金额不能大于产品金额");
        }

        PrepayOrderEntity order = new PrepayOrderEntity();
        order.setOrderNo(createOrderNo());
        order.setStudentName(req.getStudentName());
        order.setStudentPhone(req.getStudentPhone());
        order.setProductId(req.getProductId());
        order.setProductAmount(req.getProductAmount());
        order.setDiscountAmount(req.getDiscountAmount());
        order.setOrderStatus(req.getOrderStatus() == null || req.getOrderStatus().isBlank() ? DEFAULT_ORDER_STATUS : req.getOrderStatus());
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());
        prepayOrderMapper.insert(order);

        PrepayOrderCreateRes res = new PrepayOrderCreateRes();
        res.setId(order.getId());
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PrepayOrderUpdateRes updatePrepayOrder(PrepayOrderUpdateReq req) {
        PrepayOrderEntity order = new PrepayOrderEntity();
        order.setId(req.getId());
        order.setStudentName(req.getStudentName());
        order.setStudentPhone(req.getStudentPhone());
        order.setProductId(req.getProductId());
        order.setProductAmount(req.getProductAmount());
        order.setDiscountAmount(req.getDiscountAmount());
        order.setOrderStatus(req.getOrderStatus());
        order.setUpdatedAt(new Date());

        int rows = prepayOrderMapper.updateById(order);
        if (rows == 0) {
            throw BusinessException.PrepayOrderNotExist;
        }

        PrepayOrderUpdateRes res = new PrepayOrderUpdateRes();
        res.setId(req.getId());
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PrepayOrderDeleteRes deletePrepayOrder(Long id) {
        int rows = prepayOrderMapper.deleteById(id);
        if (rows == 0) {
            throw BusinessException.PrepayOrderNotExist;
        }

        PrepayOrderDeleteRes res = new PrepayOrderDeleteRes();
        res.setId(id);
        return res;
    }

    @Override
    public PrepayOrderDetailRes getPrepayOrderById(Long id) {
        PrepayOrderEntity order = prepayOrderMapper.selectById(id);
        if (order == null) {
            throw BusinessException.PrepayOrderNotExist;
        }
        PrepayOrderDetailRes res = new PrepayOrderDetailRes();
        BeanUtils.copyProperties(order, res);
        return res;
    }

    @Override
    public PageResult<PrepayOrderPageRes> pagePrepayOrder(PrepayOrderPageReq req) {
        int pageNum = req.getPageNum() == null ? 1 : req.getPageNum();
        int pageSize = req.getPageSize() == null ? 10 : req.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<PrepayOrderEntity> list = prepayOrderMapper.selectPage(req.getKeyword(), req.getOrderStatus());
        PageInfo<PrepayOrderEntity> pageInfo = new PageInfo<>(list);
        List<PrepayOrderPageRes> records = list.stream().map(this::buildPrepayOrderPageRes).toList();
        return new PageResult<>(pageInfo.getTotal(), pageNum, pageSize, records);
    }

    @Override
    public void updatePrepayOrderSuccess(String prepayOrderNo) {
        int rows = prepayOrderMapper.updatePrepayOrderSuccess(prepayOrderNo);
        if (rows == 0) {
            throw BusinessException.PrepayOrderNotExist;
        }
    }

    private PrepayOrderPageRes buildPrepayOrderPageRes(PrepayOrderEntity order) {
        PrepayOrderPageRes res = new PrepayOrderPageRes();
        BeanUtils.copyProperties(order, res);
        return res;
    }

    private String createOrderNo() {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        int random = SECURE_RANDOM.nextInt(900000) + 100000;
        return "YQ" + timestamp + random;
    }
}
