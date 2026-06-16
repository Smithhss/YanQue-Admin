package cn.yanque.models.exam.paper.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.api.PageResult;
import cn.yanque.models.exam.paper.biz.ExamPaperBiz;
import cn.yanque.models.exam.paper.pojo.vo.req.ExamPaperPageReq;
import cn.yanque.models.exam.paper.pojo.vo.req.ExamPaperSaveReq;
import cn.yanque.models.exam.paper.pojo.vo.res.ExamPaperDeleteRes;
import cn.yanque.models.exam.paper.pojo.vo.res.ExamPaperDetailRes;
import cn.yanque.models.exam.paper.pojo.vo.res.ExamPaperPageRes;
import cn.yanque.models.exam.paper.pojo.vo.res.ExamPaperSaveRes;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 试卷管理接口。
 */
@RestController
@RequestMapping("/api/examPapers")
@Tag(name = "ExamPaperController", description = "试卷管理")
public class ExamPaperController {

    @Autowired
    private ExamPaperBiz examPaperBiz;

    @PostMapping
    @Operation(description = "保存试卷和题目")
    public ApiResponse<ExamPaperSaveRes> savePaperWithQuestions(@Valid @RequestBody ExamPaperSaveReq req) {
        return ApiResponse.success(examPaperBiz.savePaperWithQuestions(req));
    }

    @GetMapping
    @Operation(description = "分页查询试卷")
    public ApiResponse<PageResult<ExamPaperPageRes>> pagePaper(@Valid @ModelAttribute ExamPaperPageReq req) {
        return ApiResponse.success(examPaperBiz.pagePaper(req));
    }

    @GetMapping("{id}")
    @Operation(description = "根据ID查询试卷")
    public ApiResponse<ExamPaperDetailRes> getPaperById(@Parameter(description = "试卷ID") @PathVariable Long id) {
        return ApiResponse.success(examPaperBiz.getPaperById(id));
    }

    @DeleteMapping("{id}")
    @Operation(description = "删除试卷")
    public ApiResponse<ExamPaperDeleteRes> deletePaper(@Parameter(description = "试卷ID") @PathVariable Long id) {
        return ApiResponse.success(examPaperBiz.deletePaper(id));
    }
}
