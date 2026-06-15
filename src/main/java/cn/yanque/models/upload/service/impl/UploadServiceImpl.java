package cn.yanque.models.upload.service.impl;

import cn.yanque.common.dataConfig.service.SysConfig;
import cn.yanque.common.dataConfig.service.SysConfigService;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.config.TosProperties;
import cn.yanque.models.upload.pojo.vo.req.UploadPresignReq;
import cn.yanque.models.upload.pojo.vo.res.DownloadPresignRes;
import cn.yanque.models.upload.pojo.vo.res.UploadPresignRes;
import cn.yanque.models.upload.service.UploadService;
import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.comm.HttpMethod;
import com.volcengine.tos.model.object.PreSignedURLInput;
import com.volcengine.tos.model.object.PreSignedURLOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UploadServiceImpl implements UploadService {

    private static final String HOMEWORK_CONTENT_PREFIX = "homework/content/";

    private static final String HOMEWORK_ANSWER_PREFIX = "homework/answer/";

    private static final String HOMEWORK_SUBMISSION_PREFIX = "homework/submission/";

    @Autowired
    private TosProperties tosProperties;

    @Autowired
    private SysConfigService sysConfigService;

    @Override
    public UploadPresignRes createUploadPresign(UploadPresignReq req) {
        String objectKey = normalizeAndValidateObjectKey(req.getObjectKey());
        if (!objectKey.toLowerCase().endsWith(".md")) {
            throw BusinessException.DateError.newInstance("当前仅支持md文件上传");
        }
        validateTosConfig();
        Long expires = getUploadExpireSeconds();

        PreSignedURLInput input = PreSignedURLInput.builder()
                .bucket(tosProperties.getBucket())
                .key(objectKey)
                .httpMethod(HttpMethod.PUT)
                .expires(expires)
                .build();
        PreSignedURLOutput output = createTosClient().preSignedURL(input);

        UploadPresignRes res = new UploadPresignRes();
        res.setUploadUrl(output.getSignedUrl());
        res.setObjectKey(objectKey);
        res.setExpires(expires);
        res.setHeaders(Map.of());
        return res;
    }

    @Override
    public DownloadPresignRes createDownloadPresign(String objectKey) {
        String normalizedObjectKey = normalizeAndValidateObjectKey(objectKey);
        validateTosConfig();
        Long expires = getPreviewExpireSeconds();

        PreSignedURLInput input = PreSignedURLInput.builder()
                .bucket(tosProperties.getBucket())
                .key(normalizedObjectKey)
                .httpMethod(HttpMethod.GET)
                .expires(expires)
                .build();
        PreSignedURLOutput output = createTosClient().preSignedURL(input);

        DownloadPresignRes res = new DownloadPresignRes();
        res.setDownloadUrl(output.getSignedUrl());
        res.setObjectKey(normalizedObjectKey);
        res.setExpires(expires);
        return res;
    }

    private String normalizeAndValidateObjectKey(String objectKey) {
        if (isBlank(objectKey)) {
            throw BusinessException.DateError.newInstance("对象Key不能为空");
        }
        String normalizedObjectKey = objectKey.trim();
        // 通用签名接口必须限制业务目录，避免前端签发任意对象路径。
        if (normalizedObjectKey.startsWith("/") || normalizedObjectKey.contains("..")
                || (!normalizedObjectKey.startsWith(HOMEWORK_CONTENT_PREFIX)
                && !normalizedObjectKey.startsWith(HOMEWORK_ANSWER_PREFIX)
                && !normalizedObjectKey.startsWith(HOMEWORK_SUBMISSION_PREFIX))) {
            throw BusinessException.DateError.newInstance("对象Key不允许访问");
        }
        return normalizedObjectKey;
    }

    private Long getUploadExpireSeconds() {
        Long uploadExpireSeconds = sysConfigService.get(SysConfig.tosUploadExpireSeconds);
        if (uploadExpireSeconds == null || uploadExpireSeconds <= 0) {
            throw BusinessException.DateError.newInstance("TOS上传签名过期时间配置错误");
        }
        return uploadExpireSeconds;
    }

    private Long getPreviewExpireSeconds() {
        Long previewExpireSeconds = sysConfigService.get(SysConfig.tosPreviewExpireSeconds);
        if (previewExpireSeconds == null || previewExpireSeconds <= 0) {
            throw BusinessException.DateError.newInstance("TOS下载签名过期时间配置错误");
        }
        return previewExpireSeconds;
    }

    private void validateTosConfig() {
        if (isBlank(tosProperties.getEndpoint()) || isBlank(tosProperties.getRegion()) || isBlank(tosProperties.getBucket())
                || isBlank(tosProperties.getAccessKey()) || isBlank(tosProperties.getSecretKey())) {
            throw BusinessException.DateError.newInstance("TOS配置不完整");
        }
    }

    private TOSV2 createTosClient() {
        return new TOSV2ClientBuilder().build(tosProperties.getRegion(), tosProperties.getEndpoint(),
                tosProperties.getAccessKey(), tosProperties.getSecretKey());
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
