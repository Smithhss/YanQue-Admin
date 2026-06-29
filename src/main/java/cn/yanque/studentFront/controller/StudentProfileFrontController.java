package cn.yanque.studentFront.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.studentFront.pojo.req.StudentProfileUpdateReq;
import cn.yanque.studentFront.pojo.res.StudentProfileRes;
import cn.yanque.studentFront.service.StudentProfileFrontService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student/profile")
@Tag(name = "StudentProfileFrontController", description = "学生端我的资料")
public class StudentProfileFrontController {

    @Autowired
    private StudentProfileFrontService studentProfileFrontService;

    @GetMapping
    @Operation(description = "查询我的资料")
    public ApiResponse<StudentProfileRes> getProfile() {
        return ApiResponse.success(studentProfileFrontService.getProfile());
    }

    @PutMapping
    @Operation(description = "更新我的资料")
    public ApiResponse<StudentProfileRes> updateProfile(@Valid @RequestBody StudentProfileUpdateReq req) {
        return ApiResponse.success(studentProfileFrontService.updateProfile(req));
    }
}
