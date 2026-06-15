package cn.yanque.models.teaching.homework.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.api.PageResult;
import cn.yanque.models.teaching.homework.pojo.vo.req.HomeworkCreateReq;
import cn.yanque.models.teaching.homework.pojo.vo.req.HomeworkPageReq;
import cn.yanque.models.teaching.homework.pojo.vo.req.HomeworkPrepareReq;
import cn.yanque.models.teaching.homework.pojo.vo.req.HomeworkUploadSignReq;
import cn.yanque.models.teaching.homework.pojo.vo.res.HomeworkContentRes;
import cn.yanque.models.teaching.homework.pojo.vo.res.HomeworkCreateRes;
import cn.yanque.models.teaching.homework.pojo.vo.res.HomeworkPageRes;
import cn.yanque.models.teaching.homework.pojo.vo.res.HomeworkPrepareRes;
import cn.yanque.models.teaching.homework.pojo.vo.res.HomeworkUploadSignRes;
import cn.yanque.models.teaching.homework.service.HomeworkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作业管理接口。
 */
@RestController
@RequestMapping("/api/homeworks")
@Tag(name = "HomeworkController", description = "作业管理")
public class HomeworkController {

    @Autowired
    private HomeworkService homeworkService;

    @PostMapping
    @Operation(description = "新增作业")
    public ApiResponse<HomeworkCreateRes> addHomework(@Valid @RequestBody HomeworkCreateReq req) {
        return ApiResponse.success(homeworkService.addHomework(req));
    }

    @GetMapping
    @Operation(description = "分页查询作业")
    public ApiResponse<PageResult<HomeworkPageRes>> pageHomework(@Valid @ModelAttribute HomeworkPageReq req) {
        return ApiResponse.success(homeworkService.pageHomework(req));
    }

    @GetMapping("prepare")
    @Operation(description = "获取作业发布预填信息")
    public ApiResponse<HomeworkPrepareRes> prepareHomework(@Valid @ModelAttribute HomeworkPrepareReq req) {
        return ApiResponse.success(homeworkService.prepareHomework(req));
    }

    @GetMapping("{id}/content")
    @Operation(description = "预览作业内容")
    public ApiResponse<HomeworkContentRes> getHomeworkContent(@PathVariable Long id) {
        return ApiResponse.success(homeworkService.getHomeworkContent(id));
    }

    @PostMapping("content/upload-sign")
    @Operation(description = "生成作业内容上传预签名")
    public ApiResponse<HomeworkUploadSignRes> createContentUploadSign(@Valid @RequestBody HomeworkUploadSignReq req) {
        return ApiResponse.success(homeworkService.createContentUploadSign(req));
    }
}
