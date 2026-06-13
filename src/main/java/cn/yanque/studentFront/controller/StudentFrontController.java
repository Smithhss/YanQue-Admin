package cn.yanque.studentFront.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.studentFront.pojo.req.StudentLoginReq;
import cn.yanque.studentFront.pojo.res.StudentLoginRes;
import cn.yanque.studentFront.service.StudentFrontService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 学生前台接口。
 */
@RestController
@RequestMapping("/api/student")
@Tag(name = "StudentFrontController", description = "学生前台")
public class StudentFrontController {

    @Autowired
    private StudentFrontService studentFrontService;

    /**
     * 学生登录。
     *
     * @param req 登录请求
     * @return 登录结果或待支付订单信息
     */
    @PostMapping("/login")
    @Operation(description = "学生登录")
    public ApiResponse<StudentLoginRes> login(@Valid @RequestBody StudentLoginReq req) {
        return ApiResponse.success(studentFrontService.login(req));
    }
}
