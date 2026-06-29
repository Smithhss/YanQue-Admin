package cn.yanque.studentFront.service;

import cn.yanque.studentFront.pojo.req.StudentProfileUpdateReq;
import cn.yanque.studentFront.pojo.res.StudentProfileRes;

public interface StudentProfileFrontService {

    StudentProfileRes getProfile();

    StudentProfileRes updateProfile(StudentProfileUpdateReq req);
}
