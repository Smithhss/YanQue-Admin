package cn.yanque.studentFront.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.jwt.JWTUtil;
import cn.yanque.common.utils.RedisUtil;
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
import cn.yanque.studentFront.service.StudentFrontAuthService;
import cn.yanque.studentFront.service.StudentFrontService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class StudentFrontServiceImpl implements StudentFrontService {

    private static final String STATUS_PENDING_PAYMENT = "PENDING_PAYMENT";
    private static final long PENDING_PAY_TOKEN_EXPIRE_MILLIS = 1000 * 60 * 30;
    private static final Duration PENDING_PAY_EXPIRE = Duration.ofMinutes(30);
    private static final String PENDING_PAY_KEY_PREFIX = "yanque:student:pending-pay:";
    private static final String TOKEN_TYPE_PENDING_PAY = "PENDING_PAY";

    @Autowired
    private PrepayOrderMapper prepayOrderMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private StudentFrontAuthService studentFrontAuthService;

    @Override
    public StudentLoginRes login(StudentLoginReq req) {

        StudentEntity student = studentMapper.selectByPhone(req.getPhone());

        // 学生有账号
        if (student != null) {
            if (!ActiveEnum.ACTIVE.name().equals(student.getStatus())) {
                throw BusinessException.UserNotExist.newInstance("学生账号已停用");
            }
            if (!req.getPassword().equals(student.getPassword())) {
                throw BusinessException.PasswordError;
            }
            StudentLoginRes res = new StudentLoginRes();
            res.setNeedPay(false);
            res.setToken(studentFrontAuthService.createToken(student));
            res.setSignSecret(studentFrontAuthService.createSignSecret(student));
            res.setStudent(studentFrontAuthService.buildStudentInfo(student));
            return res;
        }

        // 学生没账号 且未支付
        PrepayOrderEntity pendingOrder = prepayOrderMapper.selectLatestByPhoneAndStatus(req.getPhone(), STATUS_PENDING_PAYMENT);
        if (pendingOrder != null) {
            StudentLoginRes res = new StudentLoginRes();
            res.setNeedPay(true);
            fillPendingPayAuth(res, pendingOrder);
            res.setStudent(buildStudentInfo(pendingOrder));
            res.setPendingOrder(buildPendingOrderRes(pendingOrder));
            return res;
        }

        throw BusinessException.PasswordError.newInstance("用户名不存在");
    }

    private void fillPendingPayAuth(StudentLoginRes res, PrepayOrderEntity pendingOrder) {
        String token = createPendingPayToken(pendingOrder);
        String signSecret = createRandomSecret();
        String value = pendingOrder.getStudentPhone() + "|" + pendingOrder.getOrderNo() + "|" + signSecret;
        redisUtil.set(PENDING_PAY_KEY_PREFIX + token, value, PENDING_PAY_EXPIRE);
        res.setPendingPayToken(token);
        res.setPendingPaySignSecret(signSecret);
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

    private String createPendingPayToken(PrepayOrderEntity order) {
        Map<String, Object> map = new HashMap<>();
        map.put("token_type", TOKEN_TYPE_PENDING_PAY);
        map.put("phone", order.getStudentPhone());
        map.put("prepay_order_no", order.getOrderNo());
        map.put("expire_time", System.currentTimeMillis() + PENDING_PAY_TOKEN_EXPIRE_MILLIS);
        return JWTUtil.createToken(map, sysConfigService.get(SysConfig.jwtSecret).getBytes());
    }

    private String createRandomSecret() {
        return RandomUtil.randomString(48);
    }
}
