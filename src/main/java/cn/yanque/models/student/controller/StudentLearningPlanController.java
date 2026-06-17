package cn.yanque.models.student.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.api.PageResult;
import cn.yanque.models.student.pojo.vo.req.StudentLearningPlanCreateReq;
import cn.yanque.models.student.pojo.vo.req.StudentLearningPlanPageReq;
import cn.yanque.models.student.pojo.vo.res.StudentLearningCalendarRes;
import cn.yanque.models.student.pojo.vo.res.StudentLearningPlanCreateRes;
import cn.yanque.models.student.pojo.vo.res.StudentLearningPlanDetailRes;
import cn.yanque.models.student.pojo.vo.res.StudentLearningPlanPageRes;
import cn.yanque.models.student.service.StudentLearningPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 线上学员学习计划接口。
 */
@RestController
@RequestMapping("/api/studentLearningPlans")
@Tag(name = "StudentLearningPlanController", description = "线上学员学习计划")
public class StudentLearningPlanController {

    @Autowired
    private StudentLearningPlanService learningPlanService;

    @PostMapping
    @Operation(description = "创建线上学习计划并生成每日学习日历")
    public ApiResponse<StudentLearningPlanCreateRes> createPlan(@Valid @RequestBody StudentLearningPlanCreateReq req) {
        return ApiResponse.success(learningPlanService.createPlan(req));
    }

    @GetMapping
    @Operation(description = "分页查询线上学习计划")
    public ApiResponse<PageResult<StudentLearningPlanPageRes>> pagePlan(@Valid @ModelAttribute StudentLearningPlanPageReq req) {
        return ApiResponse.success(learningPlanService.pagePlan(req));
    }

    @GetMapping("{id}")
    @Operation(description = "查询线上学习计划详情")
    public ApiResponse<StudentLearningPlanDetailRes> detail(@PathVariable Long id) {
        return ApiResponse.success(learningPlanService.detail(id));
    }

    @GetMapping("{id}/calendar")
    @Operation(description = "查询线上学习计划每日学习日历")
    public ApiResponse<List<StudentLearningCalendarRes>> calendar(@PathVariable Long id) {
        return ApiResponse.success(learningPlanService.calendar(id));
    }
}
