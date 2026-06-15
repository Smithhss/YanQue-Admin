package cn.yanque.models.student.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.api.PageResult;
import cn.yanque.models.student.pojo.vo.req.StudentAssignClassReq;
import cn.yanque.models.student.pojo.vo.req.StudentPageReq;
import cn.yanque.models.student.pojo.vo.res.StudentAssignClassRes;
import cn.yanque.models.student.pojo.vo.res.StudentPageRes;
import cn.yanque.models.student.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 学生管理接口。
 */
@RestController
@RequestMapping("/api/students")
@Tag(name = "StudentController", description = "学生管理")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    @Operation(description = "分页查询学生")
    public ApiResponse<PageResult<StudentPageRes>> pageStudent(@Valid @ModelAttribute StudentPageReq req) {
        return ApiResponse.success(studentService.pageStudent(req));
    }

    @PutMapping("{id}/class")
    @Operation(description = "给线下学生分配班级")
    public ApiResponse<StudentAssignClassRes> assignClass(@Parameter(description = "学生ID") @PathVariable Long id,
                                                          @Valid @RequestBody StudentAssignClassReq req) {
        return ApiResponse.success(studentService.assignClass(id, req));
    }
}
