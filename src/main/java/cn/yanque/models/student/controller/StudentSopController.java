package cn.yanque.models.student.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.api.PageResult;
import cn.yanque.models.student.pojo.vo.req.StudentSopCompleteReq;
import cn.yanque.models.student.pojo.vo.req.StudentSopPageReq;
import cn.yanque.models.student.pojo.vo.res.StudentSopCompleteRes;
import cn.yanque.models.student.pojo.vo.res.StudentSopPageRes;
import cn.yanque.models.student.service.StudentSopService;
import io.swagger.v3.oas.annotations.Operation;
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
 * 学生入学SOP管理接口。
 */
@RestController
@RequestMapping("/api/studentSops")
@Tag(name = "StudentSopController", description = "学生入学SOP管理")
public class StudentSopController {

    @Autowired
    private StudentSopService studentSopService;

    @GetMapping
    @Operation(description = "分页查询学生入学SOP")
    public ApiResponse<PageResult<StudentSopPageRes>> pageStudentSop(@Valid @ModelAttribute StudentSopPageReq req) {
        return ApiResponse.success(studentSopService.pageStudentSop(req));
    }

    @PutMapping("{id}/complete")
    @Operation(description = "完成学生入学SOP")
    public ApiResponse<StudentSopCompleteRes> completeSop(@PathVariable Long id,
                                                          @Valid @RequestBody StudentSopCompleteReq req) {
        return ApiResponse.success(studentSopService.completeSop(id, req));
    }
}
