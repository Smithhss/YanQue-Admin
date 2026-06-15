package cn.yanque.studentFront.service.impl;

import cn.yanque.common.enums.OrderStatusEnum;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.order.product.mapper.ProductMapper;
import cn.yanque.models.order.product.pojo.entity.ProductEntity;
import cn.yanque.models.order.prepay.pojo.entity.OrderEntity;
import cn.yanque.models.order.prepay.service.OrderService;
import cn.yanque.models.student.pojo.entity.StudentEntity;
import cn.yanque.models.student.pojo.entity.StudentProductEntity;
import cn.yanque.models.student.service.StudentProductService;
import cn.yanque.models.student.service.StudentService;
import cn.yanque.studentFront.pojo.req.CompleteStudentProfileReq;
import cn.yanque.studentFront.pojo.res.CompleteStudentProfileRes;
import cn.yanque.studentFront.service.StudentFrontAuthService;
import cn.yanque.studentFront.service.StudentFrontProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentFrontProfileServiceImpl implements StudentFrontProfileService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private StudentProductService studentProductService;

    @Autowired
    private StudentFrontAuthService studentFrontAuthService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CompleteStudentProfileRes completeProfile(CompleteStudentProfileReq req) {
        if (!req.getPassword().equals(req.getConfirmPassword())) {
            throw BusinessException.ParamsError.newInstance("两次输入的密码不一致");
        }

        OrderEntity order = orderService.selectByOrderNo(req.getOrderNo());
        if (order == null) {
            throw BusinessException.DateError.newInstance("支付订单不存在");
        }
        if (!OrderStatusEnum.SUCCESS.name().equals(order.getStatus())) {
            throw BusinessException.DateError.newInstance("订单未支付成功");
        }
        ProductEntity product = productMapper.selectById(Long.valueOf(order.getProductId()));
        if (product == null) {
            throw BusinessException.ProductNotExist;
        }

        StudentEntity student = new StudentEntity();
        student.setStudentName(order.getStudentName());
        student.setStudentPhone(order.getStudentPhone());
        student.setPassword(req.getPassword());
        student.setEducation(req.getEducation());
        student.setGradeYear(req.getGradeYear());
        student.setSchool(req.getSchool());
        student.setMajor(req.getMajor());
        student.setTeachingMode(product.getTeachingMode());
        StudentEntity createdStudent = studentService.createStudent(student);

        StudentProductEntity studentProduct = new StudentProductEntity();
        studentProduct.setStudentId(createdStudent.getId());
        studentProduct.setProductId(order.getProductId());
        studentProduct.setSourceOrderNo(order.getOrderNo());
        studentProductService.createStudentProduct(studentProduct);

        CompleteStudentProfileRes res = new CompleteStudentProfileRes();
        res.setStudentId(createdStudent.getId());
        res.setCompleted(true);
        res.setToken(studentFrontAuthService.createToken(createdStudent));
        res.setSignSecret(studentFrontAuthService.createSignSecret(createdStudent));
        res.setStudent(studentFrontAuthService.buildStudentInfo(createdStudent));
        return res;
    }
}
