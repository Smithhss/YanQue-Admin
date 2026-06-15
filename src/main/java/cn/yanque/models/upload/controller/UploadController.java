package cn.yanque.models.upload.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.models.upload.pojo.vo.req.UploadPresignReq;
import cn.yanque.models.upload.pojo.vo.res.DownloadPresignRes;
import cn.yanque.models.upload.pojo.vo.res.UploadPresignRes;
import cn.yanque.models.upload.service.UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通用上传下载预签名接口。
 */
@RestController
@RequestMapping("/api/upload")
@Tag(name = "UploadController", description = "上传下载预签名")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("presign-upload")
    @Operation(description = "获取上传预签名")
    public ApiResponse<UploadPresignRes> createUploadPresign(@Valid @RequestBody UploadPresignReq req) {
        return ApiResponse.success(uploadService.createUploadPresign(req));
    }

    @GetMapping("presign-download")
    @Operation(description = "获取下载预签名")
    public ApiResponse<DownloadPresignRes> createDownloadPresign(@Valid @ModelAttribute UploadPresignReq req) {
        return ApiResponse.success(uploadService.createDownloadPresign(req.getObjectKey()));
    }
}
