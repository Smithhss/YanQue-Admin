package cn.yanque.models.exam.question.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.api.PageResult;
import cn.yanque.models.exam.question.biz.ExamQuestionBiz;
import cn.yanque.models.exam.question.pojo.vo.req.ExamQuestionCreateReq;
import cn.yanque.models.exam.question.pojo.vo.req.ExamQuestionPageReq;
import cn.yanque.models.exam.question.pojo.vo.req.ExamQuestionStatusReq;
import cn.yanque.models.exam.question.pojo.vo.req.ExamQuestionUpdateReq;
import cn.yanque.models.exam.question.pojo.vo.res.ExamQuestionCreateRes;
import cn.yanque.models.exam.question.pojo.vo.res.ExamQuestionDeleteRes;
import cn.yanque.models.exam.question.pojo.vo.res.ExamQuestionDetailRes;
import cn.yanque.models.exam.question.pojo.vo.res.ExamQuestionPageRes;
import cn.yanque.models.exam.question.pojo.vo.res.ExamQuestionStatusRes;
import cn.yanque.models.exam.question.pojo.vo.res.ExamQuestionUpdateRes;
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
 * 题库管理接口。
 */
@RestController
@RequestMapping("/api/examQuestions")
@Tag(name = "ExamQuestionController", description = "题库管理")
public class ExamQuestionController {

    @Autowired
    private ExamQuestionBiz examQuestionBiz;

    @PostMapping
    @Operation(description = "新增题目")
    public ApiResponse<ExamQuestionCreateRes> addQuestion(@Valid @RequestBody ExamQuestionCreateReq req) {
        return ApiResponse.success(examQuestionBiz.addQuestion(req));
    }

    @PutMapping("{id}")
    @Operation(description = "修改题目")
    public ApiResponse<ExamQuestionUpdateRes> updateQuestion(@Parameter(description = "题目ID") @PathVariable Long id,
                                                             @Valid @RequestBody ExamQuestionUpdateReq req) {
        req.setId(id);
        return ApiResponse.success(examQuestionBiz.updateQuestion(req));
    }

    @DeleteMapping("{id}")
    @Operation(description = "删除题目")
    public ApiResponse<ExamQuestionDeleteRes> deleteQuestion(@Parameter(description = "题目ID") @PathVariable Long id) {
        return ApiResponse.success(examQuestionBiz.deleteQuestion(id));
    }

    @GetMapping("{id}")
    @Operation(description = "根据ID查询题目")
    public ApiResponse<ExamQuestionDetailRes> getQuestionById(@Parameter(description = "题目ID") @PathVariable Long id) {
        return ApiResponse.success(examQuestionBiz.getQuestionById(id));
    }

    @GetMapping
    @Operation(description = "分页查询题库")
    public ApiResponse<PageResult<ExamQuestionPageRes>> pageQuestion(@Valid @ModelAttribute ExamQuestionPageReq req) {
        return ApiResponse.success(examQuestionBiz.pageQuestion(req));
    }

    @PutMapping("{id}/status")
    @Operation(description = "修改题目状态")
    public ApiResponse<ExamQuestionStatusRes> updateStatus(@Parameter(description = "题目ID") @PathVariable Long id,
                                                           @Valid @RequestBody ExamQuestionStatusReq req) {
        return ApiResponse.success(examQuestionBiz.updateStatus(id, req));
    }
}
