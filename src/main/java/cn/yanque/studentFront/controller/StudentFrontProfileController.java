package cn.yanque.studentFront.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.studentFront.pojo.req.CompleteStudentProfileReq;
import cn.yanque.studentFront.pojo.res.CompleteStudentProfileRes;
import cn.yanque.studentFront.service.StudentFrontProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 学生前台资料接口。
 */
@RestController
@RequestMapping("/api/studentFront/student")
@Tag(name = "StudentFrontProfileController", description = "学生前台资料")
public class StudentFrontProfileController {

    @Autowired
    private StudentFrontProfileService studentFrontProfileService;

    @PostMapping("/completeProfile")
    @Operation(description = "支付成功后完善学生资料")
    public ApiResponse<CompleteStudentProfileRes> completeProfile(@Valid @RequestBody CompleteStudentProfileReq req) {
        return ApiResponse.success(studentFrontProfileService.completeProfile(req));
    }
}
