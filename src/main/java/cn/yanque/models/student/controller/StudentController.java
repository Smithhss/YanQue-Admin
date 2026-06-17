package cn.yanque.models.student.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.api.PageResult;
import cn.yanque.models.student.pojo.vo.req.StudentAssignClassReq;
import cn.yanque.models.student.pojo.vo.req.StudentPageReq;
import cn.yanque.models.student.pojo.vo.req.StudentSopAssignReq;
import cn.yanque.models.student.pojo.vo.req.StudentTagUpdateReq;
import cn.yanque.models.student.pojo.vo.res.StudentAssignClassRes;
import cn.yanque.models.student.pojo.vo.res.StudentPageRes;
import cn.yanque.models.student.pojo.vo.res.StudentSopAssignRes;
import cn.yanque.models.student.pojo.vo.res.StudentTagUpdateRes;
import cn.yanque.models.student.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("tag-options")
    @Operation(description = "查询学生标签选项")
    public ApiResponse<List<String>> listStudentTagOptions() {
        return ApiResponse.success(studentService.listStudentTagOptions());
    }

    @PutMapping("{id}/class")
    @Operation(description = "给线下学生分配班级")
    public ApiResponse<StudentAssignClassRes> assignClass(@Parameter(description = "学生ID") @PathVariable Long id,
                                                          @Valid @RequestBody StudentAssignClassReq req) {
        return ApiResponse.success(studentService.assignClass(id, req));
    }

    @PostMapping("{id}/sop")
    @Operation(description = "给线上学生分配入学SOP")
    public ApiResponse<StudentSopAssignRes> assignSop(@Parameter(description = "学生ID") @PathVariable Long id,
                                                      @Valid @RequestBody StudentSopAssignReq req) {
        return ApiResponse.success(studentService.assignSop(id, req));
    }

    @PutMapping("{id}/tag")
    @Operation(description = "修改学生标签")
    public ApiResponse<StudentTagUpdateRes> updateStudentTag(@Parameter(description = "学生ID") @PathVariable Long id,
                                                             @Valid @RequestBody StudentTagUpdateReq req) {
        return ApiResponse.success(studentService.updateStudentTag(id, req));
    }
}
