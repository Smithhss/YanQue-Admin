package cn.yanque.models.student.coursehour.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.api.PageResult;
import cn.yanque.models.student.coursehour.pojo.vo.req.CourseHourAdjustReq;
import cn.yanque.models.student.coursehour.pojo.vo.req.StudentCourseHourPageReq;
import cn.yanque.models.student.coursehour.pojo.vo.res.StudentCourseHourPageRes;
import cn.yanque.models.student.coursehour.pojo.vo.res.StudentCourseHourRes;
import cn.yanque.models.student.coursehour.service.StudentCourseHourService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 学生课时账户接口。
 */
@RestController
@RequestMapping("/api/studentCourseHour")
@Tag(name = "StudentCourseHourController", description = "学生课时账户")
public class StudentCourseHourController {

    @Autowired
    private StudentCourseHourService studentCourseHourService;

    @GetMapping("{studentId}")
    @Operation(description = "查询学生课时账户")
    public ApiResponse<StudentCourseHourRes> getByStudentId(@Parameter(description = "学生ID") @PathVariable Long studentId) {
        return ApiResponse.success(studentCourseHourService.getByStudentId(studentId));
    }

    @PostMapping("adjust")
    @Operation(description = "调整学生课时")
    public ApiResponse<StudentCourseHourRes> adjust(@Valid @RequestBody CourseHourAdjustReq req, HttpServletRequest request) {
        Long operatorId = (Long) request.getAttribute("userId");
        return ApiResponse.success(studentCourseHourService.adjust(req, operatorId));
    }

    @GetMapping
    @Operation(description = "分页查询课时账户")
    public ApiResponse<PageResult<StudentCourseHourPageRes>> page(@Valid @ModelAttribute StudentCourseHourPageReq req) {
        return ApiResponse.success(studentCourseHourService.page(req));
    }
}
