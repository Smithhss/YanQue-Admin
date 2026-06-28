package cn.yanque.models.exam.exam.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.models.exam.exam.biz.ExamBiz;
import cn.yanque.models.exam.exam.pojo.vo.req.ExamSubmissionGradeReq;
import cn.yanque.models.exam.exam.pojo.vo.res.ExamSubmissionDetailRes;
import cn.yanque.models.exam.exam.pojo.vo.res.ExamSubmissionGradeRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 考试答卷管理接口。
 * 从 ExamController 拆分, 避免 {id} 路径变量与 submissions 固定路径冲突。
 */
@RestController
@RequestMapping("/api/exam-submissions")
@Tag(name = "ExamSubmissionController", description = "考试答卷管理")
public class ExamSubmissionController {

    @Autowired
    private ExamBiz examBiz;

    @GetMapping("{recordId}")
    @Operation(description = "查询考试答卷详情")
    public ApiResponse<ExamSubmissionDetailRes> getSubmissionDetail(@Parameter(description = "考试记录ID") @PathVariable Long recordId) {
        return ApiResponse.success(examBiz.getSubmissionDetail(recordId));
    }

    @PutMapping("{recordId}/grade")
    @Operation(description = "批改考试答卷")
    public ApiResponse<ExamSubmissionGradeRes> gradeSubmission(@Parameter(description = "考试记录ID") @PathVariable Long recordId,
                                                               @Valid @RequestBody ExamSubmissionGradeReq req) {
        return ApiResponse.success(examBiz.gradeSubmission(recordId, req));
    }
}
