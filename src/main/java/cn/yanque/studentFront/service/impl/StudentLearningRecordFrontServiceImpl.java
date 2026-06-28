package cn.yanque.studentFront.service.impl;

import cn.yanque.common.api.PageResult;
import cn.yanque.common.threadlocal.StudentThreadLocal;
import cn.yanque.models.student.coursehour.pojo.vo.res.StudentCourseHourRes;
import cn.yanque.models.student.coursehour.service.StudentCourseHourService;
import cn.yanque.models.teaching.attendance.pojo.vo.req.AttendancePageReq;
import cn.yanque.models.teaching.attendance.pojo.vo.res.AttendancePageRes;
import cn.yanque.models.teaching.attendance.service.AttendanceService;
import cn.yanque.studentFront.service.StudentLearningRecordFrontService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentLearningRecordFrontServiceImpl implements StudentLearningRecordFrontService {

    @Autowired
    private StudentCourseHourService studentCourseHourService;

    @Autowired
    private AttendanceService attendanceService;

    @Override
    public StudentCourseHourRes myCourseHour() {
        Long studentId = StudentThreadLocal.get().getId();
        return studentCourseHourService.getByStudentId(studentId);
    }

    @Override
    public PageResult<AttendancePageRes> myAttendance(Integer pageNum, Integer pageSize) {
        // 仅查询当前登录学生本人的考勤,studentId 取自登录态。
        Long studentId = StudentThreadLocal.get().getId();
        AttendancePageReq req = new AttendancePageReq();
        req.setStudentId(studentId);
        req.setPageNum(pageNum);
        req.setPageSize(pageSize);
        return attendanceService.page(req);
    }
}
