package cn.yanque.models.teaching.schedule.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.models.teaching.schedule.pojo.vo.req.AddClassSchuleReq;
import cn.yanque.models.teaching.schedule.pojo.vo.req.ClassScheduleGenerateReq;
import cn.yanque.models.teaching.schedule.pojo.vo.req.ClassScheduleTeacherAssignReq;
import cn.yanque.models.teaching.schedule.pojo.vo.req.TeacherDetailReq;
import cn.yanque.models.teaching.schedule.pojo.vo.res.*;
import cn.yanque.models.teaching.schedule.service.ClassScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
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

    @GetMapping("{classId}/classStageInfo")
    @Operation(description = "查询班级阶段信息")
    public ApiResponse<List<ClassStageInfoRes>> classStageInfo(@Parameter(description = "班级ID") @PathVariable Long classId) {
        return ApiResponse.success(classScheduleService.classStageInfo(classId));
    }

    @GetMapping("{classId}/date-detail")
    @Operation(description = "查询班级当天课程详情")
    public ApiResponse<ClassScheduleDateDetailRes> getDateDetail(@Parameter(description = "班级ID") @PathVariable Long classId,
                                                                 @Parameter(description = "上课日期，格式：yyyy-MM-dd")
                                                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date scheduleDate) {
        return ApiResponse.success(classScheduleService.getDateDetail(classId, scheduleDate));
    }

    @PutMapping("{classId}/teachers")
    @Operation(description = "按阶段分配老师")
    public ApiResponse<ClassScheduleTeacherAssignRes> assignTeachers(@Parameter(description = "班级ID") @PathVariable Long classId,
                                                                      @Valid @RequestBody ClassScheduleTeacherAssignReq req) {
        return ApiResponse.success(classScheduleService.assignTeachers(classId, req));
    }


    @PutMapping("{classId}/addClassSchule")
    @Operation(description = "新增临时课程")
    public ApiResponse<Void> addClassSchule(@Parameter(description = "班级ID") @PathVariable Long classId,
                                                                     @Valid @RequestBody AddClassSchuleReq req) {
        classScheduleService.addClassSchule(classId, req);
        return ApiResponse.success();
    }

    @GetMapping("/teacher-detail")
    @Operation(description = "查询老师上课详情")
    public ApiResponse<List<TeacherDetailRes>> teacherDetail(@RequestBody TeacherDetailReq req) {
        return ApiResponse.success(classScheduleService.teacherDetail(req));
    }

}
