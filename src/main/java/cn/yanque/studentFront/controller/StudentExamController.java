package cn.yanque.studentFront.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.api.PageResult;
import cn.yanque.studentFront.pojo.req.StudentExamPageReq;
import cn.yanque.studentFront.pojo.req.StudentExamSubmitReq;
import cn.yanque.studentFront.pojo.res.StudentExamPageRes;
import cn.yanque.studentFront.pojo.res.StudentExamPaperRes;
import cn.yanque.studentFront.pojo.res.StudentExamStartRes;
import cn.yanque.studentFront.pojo.res.StudentExamSubmissionRes;
import cn.yanque.studentFront.pojo.res.StudentExamSubmitRes;
import cn.yanque.studentFront.service.StudentExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 学生考试接口。
 */
@RestController
@RequestMapping("/student/exams")
@Tag(name = "StudentExamController", description = "学生考试")
public class StudentExamController {

    @Autowired
    private StudentExamService studentExamService;

    @GetMapping
    @Operation(description = "分页查询我的考试")
    public ApiResponse<PageResult<StudentExamPageRes>> pageExam(@Valid @ModelAttribute StudentExamPageReq req) {
        return ApiResponse.success(studentExamService.pageExam(req));
    }

    @PostMapping("{id}/start")
    @Operation(description = "开始考试")
    public ApiResponse<StudentExamStartRes> startExam(@PathVariable Long id) {
        return ApiResponse.success(studentExamService.startExam(id));
    }

    @GetMapping("records/{recordId}/paper")
    @Operation(description = "查询我的考试试卷")
    public ApiResponse<StudentExamPaperRes> getExamPaper(@PathVariable Long recordId) {
        return ApiResponse.success(studentExamService.getExamPaper(recordId));
    }

    @PostMapping("records/{recordId}/submit")
    @Operation(description = "提交考试")
    public ApiResponse<StudentExamSubmitRes> submitExam(@PathVariable Long recordId,
                                                        @Valid @RequestBody StudentExamSubmitReq req) {
        return ApiResponse.success(studentExamService.submitExam(recordId, req));
    }

    @GetMapping("records/{recordId}/submission")
    @Operation(description = "查询我的考试提交记录")
    public ApiResponse<StudentExamSubmissionRes> getSubmission(@PathVariable Long recordId) {
        return ApiResponse.success(studentExamService.getSubmission(recordId));
    }
}
