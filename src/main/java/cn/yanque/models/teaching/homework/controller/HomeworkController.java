package cn.yanque.models.teaching.homework.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.api.PageResult;
import cn.yanque.models.teaching.homework.pojo.vo.req.HomeworkCreateReq;
import cn.yanque.models.teaching.homework.pojo.vo.req.HomeworkPageReq;
import cn.yanque.models.teaching.homework.pojo.vo.req.HomeworkPrepareReq;
import cn.yanque.models.teaching.homework.pojo.vo.req.HomeworkPublishAnswerReq;
import cn.yanque.models.teaching.homework.pojo.vo.req.HomeworkSubmissionGradeReq;
import cn.yanque.models.teaching.homework.pojo.vo.req.HomeworkSubmissionPageReq;
import cn.yanque.models.teaching.homework.pojo.vo.res.HomeworkCreateRes;
import cn.yanque.models.teaching.homework.pojo.vo.res.HomeworkPageRes;
import cn.yanque.models.teaching.homework.pojo.vo.res.HomeworkPrepareRes;
import cn.yanque.models.teaching.homework.pojo.vo.res.HomeworkPublishAnswerRes;
import cn.yanque.models.teaching.homework.pojo.vo.res.HomeworkSubmissionGradeRes;
import cn.yanque.models.teaching.homework.pojo.vo.res.HomeworkSubmissionPageRes;
import cn.yanque.models.teaching.homework.service.HomeworkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作业管理接口。
 */
@RestController
@RequestMapping("/api/homeworks")
@Tag(name = "HomeworkController", description = "作业管理")
public class HomeworkController {

    @Autowired
    private HomeworkService homeworkService;

    @PostMapping
    @Operation(description = "新增作业")
    public ApiResponse<HomeworkCreateRes> addHomework(@Valid @RequestBody HomeworkCreateReq req) {
        return ApiResponse.success(homeworkService.addHomework(req));
    }

    @GetMapping
    @Operation(description = "分页查询作业")
    public ApiResponse<PageResult<HomeworkPageRes>> pageHomework(@Valid @ModelAttribute HomeworkPageReq req) {
        return ApiResponse.success(homeworkService.pageHomework(req));
    }

    @GetMapping("prepare")
    @Operation(description = "获取作业发布预填信息")
    public ApiResponse<HomeworkPrepareRes> prepareHomework(@Valid @ModelAttribute HomeworkPrepareReq req) {
        return ApiResponse.success(homeworkService.prepareHomework(req));
    }

    @PutMapping("{id}/answer")
    @Operation(description = "发布作业答案")
    public ApiResponse<HomeworkPublishAnswerRes> publishAnswer(@PathVariable Long id,
                                                               @Valid @RequestBody HomeworkPublishAnswerReq req) {
        return ApiResponse.success(homeworkService.publishAnswer(id, req));
    }

    @GetMapping("{id}/submissions")
    @Operation(description = "分页查询作业提交记录")
    public ApiResponse<PageResult<HomeworkSubmissionPageRes>> pageSubmissions(@PathVariable Long id,
                                                                              @Valid @ModelAttribute HomeworkSubmissionPageReq req) {
        return ApiResponse.success(homeworkService.pageSubmissions(id, req));
    }

    @PutMapping("submissions/{submissionId}/grade")
    @Operation(description = "批改作业提交")
    public ApiResponse<HomeworkSubmissionGradeRes> gradeSubmission(@PathVariable Long submissionId,
                                                                   @Valid @RequestBody HomeworkSubmissionGradeReq req) {
        return ApiResponse.success(homeworkService.gradeSubmission(submissionId, req));
    }
}
