package cn.yanque.models.teaching.schedule.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.models.teaching.schedule.pojo.vo.req.ClassScheduleGenerateReq;
import cn.yanque.models.teaching.schedule.pojo.vo.res.ClassScheduleGenerateRes;
import cn.yanque.models.teaching.schedule.pojo.vo.res.ClassScheduleItemRes;
import cn.yanque.models.teaching.schedule.service.ClassScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/classes/schedules")
@Tag(name = "ClassScheduleController", description = "班级课表管理")
public class ClassScheduleController {

    @Autowired
    private ClassScheduleService classScheduleService;

    @PostMapping("generate")
    @Operation(description = "生成班级课表")
    public ApiResponse<ClassScheduleGenerateRes> generateSchedule(@Valid @RequestBody ClassScheduleGenerateReq req) {
        return ApiResponse.success(classScheduleService.generateSchedule(req));
    }

    @GetMapping("{classId}")
    @Operation(description = "查询班级课表")
    public ApiResponse<List<ClassScheduleItemRes>> listSchedule(@Parameter(description = "班级ID") @PathVariable Long classId) {
        return ApiResponse.success(classScheduleService.listSchedule(classId));
    }
}
