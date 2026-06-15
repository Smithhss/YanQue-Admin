package cn.yanque.studentFront.service;

import cn.yanque.studentFront.pojo.req.CompleteStudentProfileReq;
import cn.yanque.studentFront.pojo.res.CompleteStudentProfileRes;

/**
 * 学生前台资料服务。
 */
public interface StudentFrontProfileService {

    CompleteStudentProfileRes completeProfile(CompleteStudentProfileReq req);
}
