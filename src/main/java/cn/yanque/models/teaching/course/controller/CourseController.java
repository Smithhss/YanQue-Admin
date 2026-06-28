package cn.yanque.models.teaching.course.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.api.PageResult;
import cn.yanque.models.teaching.course.pojo.vo.req.CourseCreateReq;
import cn.yanque.models.teaching.course.pojo.vo.req.CourseDetailCreateReq;
import cn.yanque.models.teaching.course.pojo.vo.req.CourseDetailUpdateReq;
import cn.yanque.models.teaching.course.pojo.vo.req.CoursePageReq;
import cn.yanque.models.teaching.course.pojo.vo.req.CourseUpdateReq;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseCreateRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseDetailCreateRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseDetailDeleteRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseDetailItemRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseDetailUpdateRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseDeleteRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseDetailRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CoursePageRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseUpdateRes;
import cn.yanque.models.teaching.course.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 课程管理接口。
 * <p>
 * 包含课程主表的增删改查,以及课程详情的维护接口。
 */
@RestController
@RequestMapping("/api/course")
@Tag(name = "CourseController", description = "课程管理")
public class CourseController {

    @Autowired
    private CourseService courseService;

    /**
     * 新增课程。
     *
     * @param req 课程新增请求
     * @return 新增后的课程ID
     */
    @PostMapping
    @Operation(description = "添加课程")
    public ApiResponse<CourseCreateRes> addCourse(@Valid @RequestBody CourseCreateReq req) {
        return ApiResponse.success(courseService.addCourse(req));
    }

    /**
     * 修改课程。
     *
     * @param id  课程ID
     * @param req 课程修改请求
     * @return 被修改的课程ID
     */
    @PutMapping("{id}")
    @Operation(description = "修改课程")
    public ApiResponse<CourseUpdateRes> updateCourse(@Parameter(description = "课程ID") @PathVariable Long id,
                                                     @Valid @RequestBody CourseUpdateReq req) {
        req.setId(id);
        return ApiResponse.success(courseService.updateCourse(req));
    }

    /**
     * 删除课程。
     *
     * @param id 课程ID
     * @return 被删除的课程ID
     */
    @DeleteMapping("{id}")
    @Operation(description = "删除课程")
    public ApiResponse<CourseDeleteRes> deleteCourse(@Parameter(description = "课程ID") @PathVariable Long id) {
        return ApiResponse.success(courseService.deleteCourse(id));
    }

    /**
     * 根据ID查询课程。
     *
     * @param id 课程ID
     * @return 课程详情
     */
    @GetMapping("{id}")
    @Operation(description = "根据ID查询课程")
    public ApiResponse<CourseDetailRes> getCourseById(@Parameter(description = "课程ID") @PathVariable Long id) {
        return ApiResponse.success(courseService.getCourseById(id));
    }

    /**
     * 分页查询课程。
     *
     * @param req 分页和搜索条件
     * @return 课程分页结果
     */
    @GetMapping
    @Operation(description = "分页查询课程")
    public ApiResponse<PageResult<CoursePageRes>> pageCourse(@Valid @ModelAttribute CoursePageReq req) {
        return ApiResponse.success(courseService.pageCourse(req));
    }

    /**
     * 给指定课程新增一条课程详情。
     *
     * @param courseId 课程ID
     * @param req      课程详情新增请求
     * @return 新增后的课程详情ID
     */
    @PostMapping("{courseId}/details")
    @Operation(description = "添加课程详情")
    public ApiResponse<CourseDetailCreateRes> addCourseDetail(@Parameter(description = "课程ID") @PathVariable Long courseId,
                                                              @Valid @RequestBody CourseDetailCreateReq req) {
        return ApiResponse.success(courseService.addCourseDetail(courseId, req));
    }

    /**
     * 修改课程详情。
     *
     * @param id  课程详情ID
     * @param req 课程详情修改请求
     * @return 被修改的课程详情ID
     */
    @PutMapping("details/{id}")
    @Operation(description = "修改课程详情")
    public ApiResponse<CourseDetailUpdateRes> updateCourseDetail(@Parameter(description = "课程详情ID") @PathVariable Long id,
                                                                 @Valid @RequestBody CourseDetailUpdateReq req) {
        req.setId(id);
        return ApiResponse.success(courseService.updateCourseDetail(req));
    }

    /**
     * 删除课程详情。
     *
     * @param id 课程详情ID
     * @return 被删除的课程详情ID
     */
    @DeleteMapping("details/{id}")
    @Operation(description = "删除课程详情")
    public ApiResponse<CourseDetailDeleteRes> deleteCourseDetail(@Parameter(description = "课程详情ID") @PathVariable Long id) {
        return ApiResponse.success(courseService.deleteCourseDetail(id));
    }

    /**
     * 根据ID查询课程详情。
     *
     * @param id 课程详情ID
     * @return 课程详情
     */
    @GetMapping("details/{id}")
    @Operation(description = "根据ID查询课程详情")
    public ApiResponse<CourseDetailItemRes> getCourseDetailById(@Parameter(description = "课程详情ID") @PathVariable Long id) {
        return ApiResponse.success(courseService.getCourseDetailById(id));
    }

    /**
     * 查询指定课程下的全部课程详情。
     *
     * @param courseId 课程ID
     * @return 课程详情列表
     */
    @GetMapping("{courseId}/details")
    @Operation(description = "查询课程详情列表")
    public ApiResponse<List<CourseDetailItemRes>> listCourseDetails(@Parameter(description = "课程ID") @PathVariable Long courseId) {
        return ApiResponse.success(courseService.listCourseDetails(courseId));
    }


    /**
     * 上传Excel导入课程详情。
     * <p>
     * 导入时会先删除该课程原有详情,再写入本次Excel中的阶段,天数和上课内容。
     *
     * @param courseId 课程ID
     * @param file     课程详情Excel文件
     * @return 空响应
     */
    @PostMapping("{courseId}/details/import")
    @Operation(description = "上传课程详情")
    public ApiResponse<Void> importClazzDetail(@Parameter(description = "课程ID") @PathVariable Long courseId,
                                               @RequestPart("file") MultipartFile file) throws IOException {
        courseService.importClazzDetail(courseId, file);
        return ApiResponse.success();
    }



}
