package cn.yanque.models.teaching.course.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.models.teaching.course.pojo.vo.req.CourseHomeworkTemplateCreateReq;
import cn.yanque.models.teaching.course.pojo.vo.req.CourseHomeworkTemplateUpdateReq;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseHomeworkTemplateCreateRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseHomeworkTemplateDeleteRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseHomeworkTemplateItemRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseHomeworkTemplateUpdateRes;
import cn.yanque.models.teaching.course.service.CourseHomeworkTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课程作业标准/训练集接口。
 */
@RestController
@RequestMapping("/api/course")
@Tag(name = "CourseHomeworkTemplateController", description = "课程作业标准")
public class CourseHomeworkTemplateController {

    @Autowired
    private CourseHomeworkTemplateService templateService;

    @PostMapping("{courseId}/homeworkTemplates")
    @Operation(description = "新增课程作业标准")
    public ApiResponse<CourseHomeworkTemplateCreateRes> addTemplate(@Parameter(description = "课程ID") @PathVariable Long courseId,
                                                                    @Valid @RequestBody CourseHomeworkTemplateCreateReq req) {
        return ApiResponse.success(templateService.addTemplate(courseId, req));
    }

    @PutMapping("homeworkTemplates/{id}")
    @Operation(description = "修改课程作业标准")
    public ApiResponse<CourseHomeworkTemplateUpdateRes> updateTemplate(@Parameter(description = "课程作业标准ID") @PathVariable Long id,
                                                                       @Valid @RequestBody CourseHomeworkTemplateUpdateReq req) {
        req.setId(id);
        return ApiResponse.success(templateService.updateTemplate(req));
    }

    @DeleteMapping("homeworkTemplates/{id}")
    @Operation(description = "删除课程作业标准")
    public ApiResponse<CourseHomeworkTemplateDeleteRes> deleteTemplate(@Parameter(description = "课程作业标准ID") @PathVariable Long id) {
        return ApiResponse.success(templateService.deleteTemplate(id));
    }

    @GetMapping("homeworkTemplates/{id}")
    @Operation(description = "查询课程作业标准详情")
    public ApiResponse<CourseHomeworkTemplateItemRes> getTemplateById(@Parameter(description = "课程作业标准ID") @PathVariable Long id) {
        return ApiResponse.success(templateService.getTemplateById(id));
    }

    @GetMapping("{courseId}/homeworkTemplates")
    @Operation(description = "查询课程作业标准列表")
    public ApiResponse<List<CourseHomeworkTemplateItemRes>> listTemplates(@Parameter(description = "课程ID") @PathVariable Long courseId) {
        return ApiResponse.success(templateService.listTemplates(courseId));
    }
}
