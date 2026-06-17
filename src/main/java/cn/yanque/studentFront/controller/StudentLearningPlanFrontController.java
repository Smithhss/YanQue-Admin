package cn.yanque.studentFront.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.studentFront.pojo.res.StudentLearningCalendarFrontRes;
import cn.yanque.studentFront.pojo.res.StudentLearningPlanCurrentRes;
import cn.yanque.studentFront.service.StudentLearningPlanFrontService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 学生端学习计划接口。
 */
@RestController
@RequestMapping("/student/learning-plan")
@Tag(name = "StudentLearningPlanFrontController", description = "学生端学习计划")
public class StudentLearningPlanFrontController {

    @Autowired
    private StudentLearningPlanFrontService learningPlanFrontService;

    @GetMapping("current")
    @Operation(description = "查询我的当前学习计划")
    public ApiResponse<StudentLearningPlanCurrentRes> current() {
        return ApiResponse.success(learningPlanFrontService.current());
    }

    @GetMapping("current/calendar")
    @Operation(description = "查询我的当前学习日历")
    public ApiResponse<List<StudentLearningCalendarFrontRes>> calendar() {
        return ApiResponse.success(learningPlanFrontService.calendar());
    }
}
