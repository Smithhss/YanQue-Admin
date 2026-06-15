package cn.yanque.studentFront.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.api.PageResult;
import cn.yanque.studentFront.pojo.req.StudentHomeworkPageReq;
import cn.yanque.studentFront.pojo.req.StudentHomeworkSubmitReq;
import cn.yanque.studentFront.pojo.res.StudentHomeworkDownloadUrlRes;
import cn.yanque.studentFront.pojo.res.StudentHomeworkPageRes;
import cn.yanque.studentFront.pojo.res.StudentHomeworkSubmitRes;
import cn.yanque.studentFront.service.StudentHomeworkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 学生作业接口。
 */
@RestController
@RequestMapping("/student/homeworks")
@Tag(name = "StudentHomeworkController", description = "学生作业")
public class StudentHomeworkController {

    @Autowired
    private StudentHomeworkService studentHomeworkService;

    @GetMapping
    @Operation(description = "分页查询我的作业")
    public ApiResponse<PageResult<StudentHomeworkPageRes>> pageHomework(@Valid @ModelAttribute StudentHomeworkPageReq req) {
        return ApiResponse.success(studentHomeworkService.pageHomework(req));
    }

    @GetMapping("{id}/download-url")
    @Operation(description = "获取作业文件下载预签名")
    public ApiResponse<StudentHomeworkDownloadUrlRes> createDownloadUrl(@PathVariable Long id,
                                                                        @RequestParam String type) {
        return ApiResponse.success(studentHomeworkService.createDownloadUrl(id, type));
    }

    @PostMapping("{id}/submissions")
    @Operation(description = "提交或重新提交作业")
    public ApiResponse<StudentHomeworkSubmitRes> submitHomework(@PathVariable Long id,
                                                                @Valid @RequestBody StudentHomeworkSubmitReq req) {
        return ApiResponse.success(studentHomeworkService.submitHomework(id, req));
    }

    @GetMapping("{id}/submissions/download-url")
    @Operation(description = "获取我的作业提交文件下载预签名")
    public ApiResponse<StudentHomeworkDownloadUrlRes> createSubmissionDownloadUrl(@PathVariable Long id) {
        return ApiResponse.success(studentHomeworkService.createSubmissionDownloadUrl(id));
    }
}
