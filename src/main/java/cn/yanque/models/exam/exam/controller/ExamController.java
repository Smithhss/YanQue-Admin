package cn.yanque.models.exam.exam.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.api.PageResult;
import cn.yanque.models.exam.exam.biz.ExamBiz;
import cn.yanque.models.exam.exam.pojo.vo.req.ExamAnswerVisibleReq;
import cn.yanque.models.exam.exam.pojo.vo.req.ExamPageReq;
import cn.yanque.models.exam.exam.pojo.vo.req.ExamSaveReq;
import cn.yanque.models.exam.exam.pojo.vo.req.ExamSubmissionGradeReq;
import cn.yanque.models.exam.exam.pojo.vo.req.ExamSubmissionPageReq;
import cn.yanque.models.exam.exam.pojo.vo.res.ExamDeleteRes;
import cn.yanque.models.exam.exam.pojo.vo.res.ExamDetailRes;
import cn.yanque.models.exam.exam.pojo.vo.res.ExamPageRes;
import cn.yanque.models.exam.exam.pojo.vo.res.ExamSaveRes;
import cn.yanque.models.exam.exam.pojo.vo.res.ExamSubmissionDetailRes;
import cn.yanque.models.exam.exam.pojo.vo.res.ExamSubmissionGradeRes;
import cn.yanque.models.exam.exam.pojo.vo.res.ExamSubmissionPageRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 考试列表接口。
 */
@RestController
@RequestMapping("/api/exams")
@Tag(name = "ExamController", description = "考试列表")
public class ExamController {

    @Autowired
    private ExamBiz examBiz;

    @PostMapping
    @Operation(description = "新增考试")
    public ApiResponse<ExamSaveRes> createExam(@Valid @RequestBody ExamSaveReq req) {
        return ApiResponse.success(examBiz.createExam(req));
    }

    @PutMapping("{id}")
    @Operation(description = "修改考试")
    public ApiResponse<ExamSaveRes> updateExam(@Parameter(description = "考试ID") @PathVariable Long id,
                                               @Valid @RequestBody ExamSaveReq req) {
        req.setId(id);
        return ApiResponse.success(examBiz.updateExam(req));
    }

    @PutMapping("{id}/answer-visible")
    @Operation(description = "修改考试答案/成绩公布状态")
    public ApiResponse<ExamSaveRes> updateAnswerVisible(@Parameter(description = "考试ID") @PathVariable Long id,
                                                        @Valid @RequestBody ExamAnswerVisibleReq req) {
        return ApiResponse.success(examBiz.updateAnswerVisible(id, req.getAnswerVisible()));
    }

    @GetMapping("{id}/submissions")
    @Operation(description = "分页查询考试提交记录")
    public ApiResponse<PageResult<ExamSubmissionPageRes>> pageSubmissions(@Parameter(description = "考试ID") @PathVariable Long id,
                                                                          @Valid @ModelAttribute ExamSubmissionPageReq req) {
        return ApiResponse.success(examBiz.pageSubmissions(id, req));
    }

    @GetMapping("submissions/{recordId}")
    @Operation(description = "查询考试答卷详情")
    public ApiResponse<ExamSubmissionDetailRes> getSubmissionDetail(@Parameter(description = "考试记录ID") @PathVariable Long recordId) {
        return ApiResponse.success(examBiz.getSubmissionDetail(recordId));
    }

    @PutMapping("submissions/{recordId}/grade")
    @Operation(description = "批改考试答卷")
    public ApiResponse<ExamSubmissionGradeRes> gradeSubmission(@Parameter(description = "考试记录ID") @PathVariable Long recordId,
                                                               @Valid @RequestBody ExamSubmissionGradeReq req) {
        return ApiResponse.success(examBiz.gradeSubmission(recordId, req));
    }

    @GetMapping
    @Operation(description = "分页查询考试")
    public ApiResponse<PageResult<ExamPageRes>> pageExam(@Valid @ModelAttribute ExamPageReq req) {
        return ApiResponse.success(examBiz.pageExam(req));
    }

    @GetMapping("{id}")
    @Operation(description = "根据ID查询考试")
    public ApiResponse<ExamDetailRes> getExamById(@Parameter(description = "考试ID") @PathVariable Long id) {
        return ApiResponse.success(examBiz.getExamById(id));
    }

    @DeleteMapping("{id}")
    @Operation(description = "删除考试")
    public ApiResponse<ExamDeleteRes> deleteExam(@Parameter(description = "考试ID") @PathVariable Long id) {
        return ApiResponse.success(examBiz.deleteExam(id));
    }
}
