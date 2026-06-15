package cn.yanque.studentFront.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.models.upload.pojo.vo.req.UploadPresignReq;
import cn.yanque.models.upload.pojo.vo.res.UploadPresignRes;
import cn.yanque.models.upload.service.UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 学生端上传预签名接口，复用通用upload模块的TOS签名能力。
 */
@RestController
@RequestMapping("/student/upload")
@Tag(name = "StudentUploadController", description = "学生端上传预签名")
public class StudentUploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("presign-upload")
    @Operation(description = "获取学生端上传预签名")
    public ApiResponse<UploadPresignRes> createUploadPresign(@Valid @RequestBody UploadPresignReq req) {
        // 学生端走/student前缀是为了复用学生JWT和签名校验；TOS签名能力仍由通用upload模块提供。
        return ApiResponse.success(uploadService.createUploadPresign(req));
    }
}
