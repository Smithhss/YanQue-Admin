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

/**
 * 学生完善资料服务实现。
 *
 * <p>核心功能：学生支付成功后，完善个人资料并完成注册。
 *
 * <p>业务流程：
 * <ol>
 *   <li>校验密码一致性
 *   <li>校验订单状态（必须是已支付成功的订单）
 *   <li>查询产品信息（获取上课方式）
 *   <li>创建学生账号（姓名和手机从订单自动填充）
 *   <li>创建学生-产品关联（记录学生购买的产品）
 *   <li>生成登录态并返回（无需再次登录）
 * </ol>
 *
 * <p>关键设计：
 * <ul>
 *   <li>事务保证：学生账号和产品关联必须同时成功或同时失败
 *   <li>自动填充：姓名和手机号从订单获取，避免数据不一致
 *   <li>直接登录：完成注册后直接返回JWT和签名密钥，提升用户体验
 * </ul>
 */
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

    /**
     * 完善学生资料。
     *
     * <p>学生支付成功后，通过此接口完善个人资料并完成注册。
     * 姓名和手机号从订单自动填充，避免数据不一致。
     * 完成后直接返回登录态，学生无需再次登录。
     *
     * @param req 完善资料请求（包含密码、学历、届数、学校、专业等）
     * @return 完善资料响应（包含学生ID、JWT Token、签名密钥、学生信息）
     * @throws BusinessException 如果密码不一致、订单不存在、订单未支付、产品不存在
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CompleteStudentProfileRes completeProfile(CompleteStudentProfileReq req) {
        // 1. 校验密码一致性
        if (!req.getPassword().equals(req.getConfirmPassword())) {
            throw BusinessException.ParamsError.newInstance("两次输入的密码不一致");
        }

        // 2. 校验订单存在且已支付成功
        OrderEntity order = orderService.selectByOrderNo(req.getOrderNo());
        if (order == null) {
            throw BusinessException.DateError.newInstance("支付订单不存在");
        }
        if (!OrderStatusEnum.SUCCESS.name().equals(order.getStatus())) {
            throw BusinessException.DateError.newInstance("订单未支付成功");
        }

        // 3. 查询产品信息（需要获取上课方式 teachingMode）
        ProductEntity product = productMapper.selectById(Long.valueOf(order.getProductId()));
        if (product == null) {
            throw BusinessException.ProductNotExist;
        }

        // 4. 创建学生账号（姓名和手机号从订单自动填充，不让学生重复填写）
        StudentEntity student = new StudentEntity();
        student.setStudentName(order.getStudentName());      // 从订单自动填充
        student.setStudentPhone(order.getStudentPhone());    // 从订单自动填充
        student.setPassword(req.getPassword());              // 学生填写的密码
        student.setEducation(req.getEducation());            // 学历
        student.setGradeYear(req.getGradeYear());            // 届数
        student.setSchool(req.getSchool());                  // 学校
        student.setMajor(req.getMajor());                    // 专业
        student.setTeachingMode(product.getTeachingMode());  // 从产品获取（线上/线下）
        StudentEntity createdStudent = studentService.createStudent(student);

        // 5. 创建学生-产品关联（记录"哪个学生购买了哪个产品"）
        StudentProductEntity studentProduct = new StudentProductEntity();
        studentProduct.setStudentId(createdStudent.getId());
        studentProduct.setProductId(order.getProductId());
        studentProduct.setSourceOrderNo(order.getOrderNo());  // 记录来源订单号
        studentProductService.createStudentProduct(studentProduct);

        // 6. 生成学生登录态并返回（完成注册后直接登录，无需再次调用登录接口）
        CompleteStudentProfileRes res = new CompleteStudentProfileRes();
        res.setStudentId(createdStudent.getId());
        res.setCompleted(true);
        res.setToken(studentFrontAuthService.createToken(createdStudent));          // JWT Token
        res.setSignSecret(studentFrontAuthService.createSignSecret(createdStudent)); // 签名密钥（存入Redis，TTL 1小时）
        res.setStudent(studentFrontAuthService.buildStudentInfo(createdStudent));    // 学生基本信息
        return res;
    }
}
