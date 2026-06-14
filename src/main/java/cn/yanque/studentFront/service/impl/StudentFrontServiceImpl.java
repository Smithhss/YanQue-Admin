package cn.yanque.studentFront.service.impl;

import cn.hutool.jwt.JWTUtil;
import cn.yanque.common.dataConfig.service.SysConfig;
import cn.yanque.common.dataConfig.service.SysConfigService;
import cn.yanque.common.enums.ActiveEnum;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.order.product.mapper.ProductMapper;
import cn.yanque.models.order.product.pojo.entity.ProductEntity;
import cn.yanque.models.order.prepay.mapper.PrepayOrderMapper;
import cn.yanque.models.order.prepay.pojo.entity.PrepayOrderEntity;
import cn.yanque.models.student.mapper.StudentMapper;
import cn.yanque.models.student.pojo.entity.StudentEntity;
import cn.yanque.studentFront.pojo.req.StudentLoginReq;
import cn.yanque.studentFront.pojo.res.StudentInfoRes;
import cn.yanque.studentFront.pojo.res.StudentLoginRes;
import cn.yanque.studentFront.pojo.res.StudentPendingPayOrderRes;
import cn.yanque.studentFront.service.StudentFrontService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class StudentFrontServiceImpl implements StudentFrontService {

    private static final String STATUS_PENDING_PAYMENT = "PENDING_PAYMENT";
    private static final String STATUS_PAID = "PAID";
    private static final long TOKEN_EXPIRE_MILLIS = 1000 * 60 * 60;

    @Autowired
    private PrepayOrderMapper prepayOrderMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private SysConfigService sysConfigService;

    @Override
    public StudentLoginRes login(StudentLoginReq req) {

        StudentEntity student = studentMapper.selectByPhone(req.getPhone());
        if (student != null) {
            if (!ActiveEnum.ACTIVE.name().equals(student.getStatus())) {
                throw BusinessException.UserNotExist.newInstance("学生账号已停用");
            }
            if (!req.getPassword().equals(student.getPassword())) {
                throw BusinessException.PasswordError;
            }
            StudentLoginRes res = new StudentLoginRes();
            res.setNeedPay(false);
            res.setToken(createToken(student));
            res.setStudent(buildStudentInfo(student));
            return res;
        }

        PrepayOrderEntity pendingOrder = prepayOrderMapper.selectLatestByPhoneAndStatus(req.getPhone(), STATUS_PENDING_PAYMENT);
        if (pendingOrder != null) {
            StudentLoginRes res = new StudentLoginRes();
            res.setNeedPay(true);
            res.setStudent(buildStudentInfo(pendingOrder));
            res.setPendingOrder(buildPendingOrderRes(pendingOrder));
            return res;
        }

        throw BusinessException.PasswordError.newInstance("用户名不存在");
    }

    private StudentInfoRes buildStudentInfo(StudentEntity student) {
        StudentInfoRes res = new StudentInfoRes();
        res.setName(student.getStudentName());
        res.setPhone(student.getStudentPhone());
        return res;
    }

    private StudentInfoRes buildStudentInfo(PrepayOrderEntity order) {
        StudentInfoRes res = new StudentInfoRes();
        res.setName(order.getStudentName());
        res.setPhone(order.getStudentPhone());
        return res;
    }

    private StudentPendingPayOrderRes buildPendingOrderRes(PrepayOrderEntity order) {
        StudentPendingPayOrderRes res = new StudentPendingPayOrderRes();
        BeanUtils.copyProperties(order, res);
        ProductEntity product = productMapper.selectById(order.getProductId());
        if (product != null) {
            res.setProductContent(product.getCourseContent());
        }
        BigDecimal productAmount = order.getProductAmount() == null ? BigDecimal.ZERO : order.getProductAmount();
        BigDecimal discountAmount = order.getDiscountAmount() == null ? BigDecimal.ZERO : order.getDiscountAmount();
        res.setPayableAmount(productAmount.subtract(discountAmount).max(BigDecimal.ZERO));
        return res;
    }

    private String createToken(PrepayOrderEntity order) {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", order.getId());
        map.put("phone", order.getStudentPhone());
        map.put("student", true);
        map.put("expire_time", System.currentTimeMillis() + TOKEN_EXPIRE_MILLIS);
        return JWTUtil.createToken(map, sysConfigService.get(SysConfig.jwtSecret).getBytes());
    }

    private String createToken(StudentEntity student) {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", student.getId());
        map.put("phone", student.getStudentPhone());
        map.put("student", true);
        map.put("expire_time", System.currentTimeMillis() + TOKEN_EXPIRE_MILLIS);
        return JWTUtil.createToken(map, sysConfigService.get(SysConfig.jwtSecret).getBytes());
    }
}
