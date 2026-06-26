package cn.yanque.studentFront.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.models.dorm.pojo.vo.res.DormAssignmentRes;
import cn.yanque.studentFront.service.StudentDormFrontService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 学生端"我的宿舍"接口。
 */
@RestController
@RequestMapping("/student/dorm")
@Tag(name = "StudentDormFrontController", description = "学生端我的宿舍")
public class StudentDormFrontController {

    @Autowired
    private StudentDormFrontService studentDormFrontService;

    @GetMapping("my")
    @Operation(description = "查询我的宿舍")
    public ApiResponse<DormAssignmentRes> myDorm() {
        return ApiResponse.success(studentDormFrontService.myDorm());
    }
}
