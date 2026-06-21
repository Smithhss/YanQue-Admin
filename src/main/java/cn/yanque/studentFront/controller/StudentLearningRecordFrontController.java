package cn.yanque.studentFront.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.api.PageResult;
import cn.yanque.models.student.coursehour.pojo.vo.res.StudentCourseHourRes;
import cn.yanque.models.teaching.attendance.pojo.vo.res.AttendancePageRes;
import cn.yanque.studentFront.service.StudentLearningRecordFrontService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 学生端考勤与课时接口。
 */
@RestController
@RequestMapping("/student/learning-record")
@Tag(name = "StudentLearningRecordFrontController", description = "学生端考勤与课时")
public class StudentLearningRecordFrontController {

    @Autowired
    private StudentLearningRecordFrontService learningRecordFrontService;

    @GetMapping("course-hour")
    @Operation(description = "查询我的剩余课时")
    public ApiResponse<StudentCourseHourRes> courseHour() {
        return ApiResponse.success(learningRecordFrontService.myCourseHour());
    }

    @GetMapping("attendance")
    @Operation(description = "查询我的考勤记录")
    public ApiResponse<PageResult<AttendancePageRes>> attendance(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize) {
        return ApiResponse.success(learningRecordFrontService.myAttendance(pageNum, pageSize));
    }
}
