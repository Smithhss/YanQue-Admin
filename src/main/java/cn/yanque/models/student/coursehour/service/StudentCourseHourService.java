package cn.yanque.models.student.coursehour.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.student.coursehour.pojo.vo.req.CourseHourAdjustReq;
import cn.yanque.models.student.coursehour.pojo.vo.req.StudentCourseHourPageReq;
import cn.yanque.models.student.coursehour.pojo.vo.res.StudentCourseHourPageRes;
import cn.yanque.models.student.coursehour.pojo.vo.res.StudentCourseHourRes;

public interface StudentCourseHourService {

    StudentCourseHourRes getByStudentId(Long studentId);

    StudentCourseHourRes adjust(CourseHourAdjustReq req, Long operatorId);

    PageResult<StudentCourseHourPageRes> page(StudentCourseHourPageReq req);

    /** 消耗课时(考勤扣减),返回扣减后剩余课时。 */
    java.math.BigDecimal consume(Long studentId, java.math.BigDecimal hours, Long scheduleId, Long operatorId);

    /** 回退课时(改点名时退回此前扣减)。 */
    void revert(Long studentId, java.math.BigDecimal hours, Long scheduleId, Long operatorId);
}
