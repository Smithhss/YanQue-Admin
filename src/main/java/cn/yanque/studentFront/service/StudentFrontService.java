package cn.yanque.studentFront.service;

import cn.yanque.studentFront.pojo.req.StudentLoginReq;
import cn.yanque.studentFront.pojo.res.StudentLoginRes;

/**
 * 学生前台服务。
 */
public interface StudentFrontService {

    /**
     * 学生登录,未支付时返回待支付订单信息。
     *
     * @param req 登录请求
     * @return 登录结果
     */
    StudentLoginRes login(StudentLoginReq req);
}
