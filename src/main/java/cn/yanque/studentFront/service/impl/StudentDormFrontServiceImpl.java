package cn.yanque.studentFront.service.impl;

import cn.yanque.common.threadlocal.StudentThreadLocal;
import cn.yanque.models.dorm.pojo.vo.res.DormAssignmentRes;
import cn.yanque.models.dorm.service.DormAssignmentService;
import cn.yanque.studentFront.service.StudentDormFrontService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentDormFrontServiceImpl implements StudentDormFrontService {

    @Autowired
    private DormAssignmentService dormAssignmentService;

    @Override
    public DormAssignmentRes myDorm() {
        // 仅查询当前登录学生本人的在住宿舍,studentId 取自登录态。
        Long studentId = StudentThreadLocal.get().getId();
        return dormAssignmentService.getLivingByStudentId(studentId);
    }
}
