package cn.yanque.models.teaching.course.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.models.teaching.course.pojo.vo.req.CourseDetailUpdateReq;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseDetailDeleteRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseDetailItemRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseDetailUpdateRes;
import cn.yanque.models.teaching.course.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 课程详情管理接口。
 * 从 CourseController 拆分, 避免 {id} 路径变量与 details 固定路径冲突。
 */
@RestController
@RequestMapping("/api/course-details")
@Tag(name = "CourseDetailController", description = "课程详情管理")
public class CourseDetailController {

    @Autowired
    private CourseService courseService;

    @PutMapping("{id}")
    @Operation(description = "修改课程详情")
    public ApiResponse<CourseDetailUpdateRes> updateCourseDetail(@Parameter(description = "课程详情ID") @PathVariable Long id,
                                                                 @Valid @RequestBody CourseDetailUpdateReq req) {
        req.setId(id);
        return ApiResponse.success(courseService.updateCourseDetail(req));
    }

    @DeleteMapping("{id}")
    @Operation(description = "删除课程详情")
    public ApiResponse<CourseDetailDeleteRes> deleteCourseDetail(@Parameter(description = "课程详情ID") @PathVariable Long id) {
        return ApiResponse.success(courseService.deleteCourseDetail(id));
    }

    @GetMapping("{id}")
    @Operation(description = "根据ID查询课程详情")
    public ApiResponse<CourseDetailItemRes> getCourseDetailById(@Parameter(description = "课程详情ID") @PathVariable Long id) {
        return ApiResponse.success(courseService.getCourseDetailById(id));
    }
}
