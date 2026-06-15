package cn.yanque.studentFront.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.api.PageResult;
import cn.yanque.studentFront.pojo.req.StudentHomeworkPageReq;
import cn.yanque.studentFront.pojo.res.StudentHomeworkDownloadUrlRes;
import cn.yanque.studentFront.pojo.res.StudentHomeworkPageRes;
import cn.yanque.studentFront.service.StudentHomeworkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    private Long getStudentId(HttpServletRequest request) {
        return (Long) request.getAttribute("studentId");
    }
}
