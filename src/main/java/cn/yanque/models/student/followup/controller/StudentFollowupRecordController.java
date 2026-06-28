package cn.yanque.models.student.followup.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.api.PageResult;
import cn.yanque.models.student.followup.pojo.vo.req.StudentFollowupRecordCancelReq;
import cn.yanque.models.student.followup.pojo.vo.req.StudentFollowupRecordCompleteReq;
import cn.yanque.models.student.followup.pojo.vo.req.StudentFollowupRecordPageReq;
import cn.yanque.models.student.followup.pojo.vo.res.StudentFollowupRecordDetailRes;
import cn.yanque.models.student.followup.pojo.vo.res.StudentFollowupRecordGenerateRes;
import cn.yanque.models.student.followup.pojo.vo.res.StudentFollowupRecordPageRes;
import cn.yanque.models.student.followup.pojo.vo.res.StudentFollowupRecordStatsRes;
import cn.yanque.models.student.followup.pojo.vo.res.StudentFollowupRecordUpdateRes;
import cn.yanque.models.student.followup.service.StudentFollowupRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/studentFollowupRecords")
@Tag(name = "StudentFollowupRecordController", description = "学生回访管理")
public class StudentFollowupRecordController {

    @Autowired
    private StudentFollowupRecordService studentFollowupRecordService;

    @GetMapping
    @Operation(description = "分页查询学生回访记录")
    public ApiResponse<PageResult<StudentFollowupRecordPageRes>> page(@Valid @ModelAttribute StudentFollowupRecordPageReq req) {
        return ApiResponse.success(studentFollowupRecordService.page(req));
    }

    @GetMapping("by-stats")
    @Operation(description = "查询学生回访统计")
    public ApiResponse<StudentFollowupRecordStatsRes> stats() {
        return ApiResponse.success(studentFollowupRecordService.stats());
    }

    @GetMapping("{id}")
    @Operation(description = "查询学生回访记录详情")
    public ApiResponse<StudentFollowupRecordDetailRes> detail(@Parameter(description = "记录ID") @PathVariable Long id) {
        return ApiResponse.success(studentFollowupRecordService.detail(id));
    }

    @PostMapping("generate")
    @Operation(description = "手动生成截至今天的学生回访记录")
    public ApiResponse<StudentFollowupRecordGenerateRes> generate() {
        return ApiResponse.success(studentFollowupRecordService.generateDueRecords(new Date()));
    }

    @PutMapping("{id}/complete")
    @Operation(description = "完成学生回访")
    public ApiResponse<StudentFollowupRecordUpdateRes> complete(@Parameter(description = "记录ID") @PathVariable Long id,
                                                                @RequestAttribute("userId") Long userId,
                                                                @Valid @RequestBody StudentFollowupRecordCompleteReq req) {
        return ApiResponse.success(studentFollowupRecordService.complete(id, userId, req));
    }

    @PutMapping("{id}/cancel")
    @Operation(description = "取消学生回访")
    public ApiResponse<StudentFollowupRecordUpdateRes> cancel(@Parameter(description = "记录ID") @PathVariable Long id,
                                                              @RequestBody StudentFollowupRecordCancelReq req) {
        return ApiResponse.success(studentFollowupRecordService.cancel(id, req));
    }
}
