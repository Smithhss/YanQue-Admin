package cn.yanque.studentFront.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.student.coursehour.pojo.vo.res.StudentCourseHourRes;
import cn.yanque.models.teaching.attendance.pojo.vo.res.AttendancePageRes;

public interface StudentLearningRecordFrontService {

    StudentCourseHourRes myCourseHour();

    PageResult<AttendancePageRes> myAttendance(Integer pageNum, Integer pageSize);
}
