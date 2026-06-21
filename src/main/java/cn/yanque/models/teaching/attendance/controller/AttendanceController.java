package cn.yanque.models.teaching.attendance.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.api.PageResult;
import cn.yanque.models.teaching.attendance.pojo.vo.req.AttendanceCommitReq;
import cn.yanque.models.teaching.attendance.pojo.vo.req.AttendancePageReq;
import cn.yanque.models.teaching.attendance.pojo.vo.res.AttendanceCommitRes;
import cn.yanque.models.teaching.attendance.pojo.vo.res.AttendancePageRes;
import cn.yanque.models.teaching.attendance.pojo.vo.res.AttendanceRosterRes;
import cn.yanque.models.teaching.attendance.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 考勤管理接口。
 */
@RestController
@RequestMapping("/api/attendance")
@Tag(name = "AttendanceController", description = "考勤管理")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("roster")
    @Operation(description = "获取课次考勤名单")
    public ApiResponse<AttendanceRosterRes> roster(@Parameter(description = "课次ID") @RequestParam Long scheduleId) {
        return ApiResponse.success(attendanceService.roster(scheduleId));
    }

    @PostMapping("commit")
    @Operation(description = "提交考勤")
    public ApiResponse<AttendanceCommitRes> commit(@Valid @RequestBody AttendanceCommitReq req, HttpServletRequest request) {
        Long operatorId = (Long) request.getAttribute("userId");
        return ApiResponse.success(attendanceService.commit(req, operatorId));
    }

    @GetMapping
    @Operation(description = "分页查询考勤记录")
    public ApiResponse<PageResult<AttendancePageRes>> page(@Valid @ModelAttribute AttendancePageReq req) {
        return ApiResponse.success(attendanceService.page(req));
    }
}
