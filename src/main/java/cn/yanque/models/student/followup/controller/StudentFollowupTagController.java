package cn.yanque.models.student.followup.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.api.PageResult;
import cn.yanque.models.student.followup.pojo.vo.req.StudentFollowupTagCreateReq;
import cn.yanque.models.student.followup.pojo.vo.req.StudentFollowupTagPageReq;
import cn.yanque.models.student.followup.pojo.vo.req.StudentFollowupTagUpdateReq;
import cn.yanque.models.student.followup.pojo.vo.res.StudentFollowupTagCreateRes;
import cn.yanque.models.student.followup.pojo.vo.res.StudentFollowupTagDeleteRes;
import cn.yanque.models.student.followup.pojo.vo.res.StudentFollowupTagDetailRes;
import cn.yanque.models.student.followup.pojo.vo.res.StudentFollowupTagPageRes;
import cn.yanque.models.student.followup.pojo.vo.res.StudentFollowupTagUpdateRes;
import cn.yanque.models.student.followup.service.StudentFollowupTagService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/studentFollowupTags")
@Tag(name = "StudentFollowupTagController", description = "学生回访标签管理")
public class StudentFollowupTagController {

    @Autowired
    private StudentFollowupTagService studentFollowupTagService;

    @PostMapping
    @Operation(description = "新增学生回访标签配置")
    public ApiResponse<StudentFollowupTagCreateRes> add(@Valid @RequestBody StudentFollowupTagCreateReq req) {
        return ApiResponse.success(studentFollowupTagService.add(req));
    }

    @PutMapping("{id}")
    @Operation(description = "修改学生回访标签配置")
    public ApiResponse<StudentFollowupTagUpdateRes> update(@Parameter(description = "配置ID") @PathVariable Long id,
                                                           @Valid @RequestBody StudentFollowupTagUpdateReq req) {
        req.setId(id);
        return ApiResponse.success(studentFollowupTagService.update(req));
    }

    @DeleteMapping("{id}")
    @Operation(description = "删除学生回访标签配置")
    public ApiResponse<StudentFollowupTagDeleteRes> delete(@Parameter(description = "配置ID") @PathVariable Long id) {
        return ApiResponse.success(studentFollowupTagService.delete(id));
    }

    @GetMapping("{id}")
    @Operation(description = "查询学生回访标签配置详情")
    public ApiResponse<StudentFollowupTagDetailRes> detail(@Parameter(description = "配置ID") @PathVariable Long id) {
        return ApiResponse.success(studentFollowupTagService.detail(id));
    }

    @GetMapping
    @Operation(description = "分页查询学生回访标签配置")
    public ApiResponse<PageResult<StudentFollowupTagPageRes>> page(@Valid @ModelAttribute StudentFollowupTagPageReq req) {
        return ApiResponse.success(studentFollowupTagService.page(req));
    }
}
