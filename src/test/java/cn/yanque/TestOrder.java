package cn.yanque;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.order.prepay.pojo.vo.req.PrepayOrderCreateReq;
import cn.yanque.models.order.prepay.pojo.vo.req.PrepayOrderPageReq;
import cn.yanque.models.order.prepay.pojo.vo.res.PrepayOrderCreateRes;
import cn.yanque.models.order.prepay.pojo.vo.res.PrepayOrderPageRes;
import cn.yanque.models.order.prepay.service.PrepayOrderService;
import cn.yanque.studentFront.biz.StudentOrderBiz;
import cn.yanque.studentFront.pojo.req.CreatePaymentOrderReq;
import cn.yanque.studentFront.pojo.res.CreateOrderNoRes;
import cn.yanque.studentFront.pojo.res.CreatePaymentOrderRes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
public class TestOrder {

    @Autowired
    private PrepayOrderService prepayOrderService;

    @Autowired
    private StudentOrderBiz studentOrderBiz;

    /**
     * 测试创建预支付订单
     */
    @Test
    public void testCreatePrepayOrder() {
        PrepayOrderCreateReq req = new PrepayOrderCreateReq();
        req.setStudentName("测试学生");
        req.setStudentPhone("13800138000");
        req.setProductId(1L);
        req.setProductAmount(new BigDecimal("20000.00"));
        req.setDiscountAmount(new BigDecimal("0.00"));

        PrepayOrderCreateRes res = prepayOrderService.addPrepayOrder(req);
        System.out.println("预支付订单创建成功，ID: " + res.getId());
    }

    /**
     * 测试分页查询预支付订单
     */
    @Test
    public void testPagePrepayOrder() {
        PrepayOrderPageReq req = new PrepayOrderPageReq();
        req.setPageNum(1);
        req.setPageSize(10);

        PageResult<PrepayOrderPageRes> result = prepayOrderService.pagePrepayOrder(req);
        System.out.println("总记录数: " + result.getTotal());
        System.out.println("当前页: " + result.getPageNum());
        System.out.println("每页数量: " + result.getPageSize());
        System.out.println("订单列表:");
        for (PrepayOrderPageRes order : result.getRecords()) {
            System.out.println("  订单号: " + order.getOrderNo() + ", 学生: " + order.getStudentName() + ", 状态: " + order.getOrderStatus());
        }
    }

    /**
     * 测试生成订单号
     */
    @Test
    public void testCreateOrderNo() {
        CreateOrderNoRes res = studentOrderBiz.createOrderNo();
        System.out.println("生成的订单号: " + res.getOrderNo());
    }

    /**
     * 测试创建支付订单（需要先有预支付订单）
     */
    @Test
    public void testCreatePaymentOrder() {
        // 先查询一个待支付的预支付订单
        PrepayOrderPageReq pageReq = new PrepayOrderPageReq();
        pageReq.setPageNum(1);
        pageReq.setPageSize(1);
        pageReq.setOrderStatus("PENDING_PAYMENT");

        PageResult<PrepayOrderPageRes> result = prepayOrderService.pagePrepayOrder(pageReq);
        if (result.getRecords().isEmpty()) {
            System.out.println("没有待支付的预支付订单，请先创建预支付订单");
            return;
        }

        PrepayOrderPageRes prepayOrder = result.getRecords().get(0);
        System.out.println("使用预支付订单: " + prepayOrder.getOrderNo());

        // 生成支付订单号
        CreateOrderNoRes orderNoRes = studentOrderBiz.createOrderNo();
        System.out.println("生成的支付订单号: " + orderNoRes.getOrderNo());

        // 创建支付订单
        CreatePaymentOrderReq req = new CreatePaymentOrderReq();
        req.setOrderNo(orderNoRes.getOrderNo());
        req.setStudentName(prepayOrder.getStudentName());
        req.setStudentPhone(prepayOrder.getStudentPhone());
        req.setProductId(String.valueOf(prepayOrder.getProductId()));
        req.setOrderAmount(prepayOrder.getProductAmount().subtract(prepayOrder.getDiscountAmount()));
        req.setPrepayOrderNo(prepayOrder.getOrderNo());

        try {
            CreatePaymentOrderRes res = studentOrderBiz.createPaymentOrder(req);
            System.out.println("支付订单创建成功");
            System.out.println("收银台地址: " + res.getCashierUrl());
        } catch (Exception e) {
            System.out.println("创建支付订单失败: " + e.getMessage());
        }
    }
}
