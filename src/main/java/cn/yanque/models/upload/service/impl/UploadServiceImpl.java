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

/**
 * 文件上传服务实现 - 基于火山引擎 TOS 对象存储。
 *
 * <p>核心功能:
 * <ul>
 *   <li>生成预签名上传 URL(客户端直传 TOS,服务端不经手文件)
 *   <li>生成预签名下载 URL(临时授权访问私有文件)
 *   <li>路径白名单校验(防止前端签发任意路径)
 *   <li>文件类型校验(作业文件仅支持 .md,学生视频支持 .mp4/.mov/.m4v/.webm)
 * </ul>
 *
 * <p>安全设计:
 * <ul>
 *   <li>路径穿越防护:禁止 "/" 开头和 ".." 路径
 *   <li>业务目录白名单:只允许 homework/,course/,student/ 前缀
 *   <li>签名有效期:上传和下载 URL 均有时效限制(系统配置)
 * </ul>
 */
@Service
public class UploadServiceImpl implements UploadService {

    // 作业文件路径前缀(教师发布作业时上传)
    private static final String HOMEWORK_CONTENT_PREFIX = "homework/content/";

    // 作业答案路径前缀(教师发布答案时上传)
    private static final String HOMEWORK_ANSWER_PREFIX = "homework/answer/";

    // 作业提交路径前缀(学生提交作业时上传)
    private static final String HOMEWORK_SUBMISSION_PREFIX = "homework/submission/";

    // 课程作业模板路径前缀(课程资料)
    private static final String COURSE_HOMEWORK_TEMPLATE_PREFIX = "course/homework-template/";

    // 学生 SOP 视频路径前缀(学生上传的标准操作流程视频)
    private static final String STUDENT_SOP_PREFIX = "student/sop/";

    // 学生回访视频路径前缀(学生上传的回访记录视频)
    private static final String STUDENT_FOLLOWUP_PREFIX = "student/followup/";

    @Autowired
    private TosProperties tosProperties;

    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 生成预签名上传 URL。
     *
     * <p>客户端获取预签名 URL 后,使用 PUT 方法直接上传文件到 TOS,服务端不经手文件流。
     *
     * <p>业务流程:
     * <ol>
     *   <li>校验并规范化 objectKey(路径白名单 + 路径穿越防护)
     *   <li>校验文件类型(作业 .md,学生视频 .mp4/.mov/.m4v/.webm)
     *   <li>校验 TOS 配置完整性
     *   <li>调用 TOS SDK 生成预签名 PUT URL
     * </ol>
     *
     * @param req 上传请求(包含 objectKey)
     * @return 预签名上传 URL,objectKey,过期时间
     * @throws BusinessException 如果路径非法,文件类型不支持,TOS 配置错误
     */
    @Override
    public UploadPresignRes createUploadPresign(UploadPresignReq req) {
        // 1. 校验并规范化路径
        String objectKey = normalizeAndValidateObjectKey(req.getObjectKey());

        // 2. 校验文件类型
        validateUploadFileType(objectKey);

        // 3. 校验 TOS 配置
        validateTosConfig();

        // 4. 获取签名有效期
        Long expires = getUploadExpireSeconds();

        // 5. 生成预签名 PUT URL
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

    /**
     * 生成预签名下载 URL。
     *
     * <p>用于临时授权访问私有文件(作业文件,答案,学生提交等),签名 URL 有效期内可直接下载。
     *
     * <p>业务流程:
     * <ol>
     *   <li>校验并规范化 objectKey(路径白名单 + 路径穿越防护)
     *   <li>校验 TOS 配置完整性
     *   <li>调用 TOS SDK 生成预签名 GET URL
     * </ol>
     *
     * @param objectKey 对象路径
     * @return 预签名下载 URL,objectKey,过期时间
     * @throws BusinessException 如果路径非法,TOS 配置错误
     */
    @Override
    public DownloadPresignRes createDownloadPresign(String objectKey) {
        // 1. 校验并规范化路径
        String normalizedObjectKey = normalizeAndValidateObjectKey(objectKey);

        // 2. 校验 TOS 配置
        validateTosConfig();

        // 3. 获取签名有效期
        Long expires = getPreviewExpireSeconds();

        // 4. 生成预签名 GET URL
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

    /**
     * 规范化并校验 objectKey。
     *
     * <p>安全检查:
     * <ul>
     *   <li>禁止以 "/" 开头(绝对路径)
     *   <li>禁止包含 ".."(路径穿越攻击)
     *   <li>白名单校验:只允许 homework/,course/,student/ 前缀
     * </ul>
     *
     * @param objectKey 原始路径
     * @return 规范化后的路径
     * @throws BusinessException 如果路径为空或不符合安全规则
     */
    private String normalizeAndValidateObjectKey(String objectKey) {
        if (isBlank(objectKey)) {
            throw BusinessException.DateError.newInstance("对象Key不能为空");
        }
        String normalizedObjectKey = objectKey.trim();

        // 通用签名接口必须限制业务目录,避免前端签发任意对象路径
        if (normalizedObjectKey.startsWith("/") || normalizedObjectKey.contains("..")
                || (!normalizedObjectKey.startsWith(HOMEWORK_CONTENT_PREFIX)
                && !normalizedObjectKey.startsWith(HOMEWORK_ANSWER_PREFIX)
                && !normalizedObjectKey.startsWith(HOMEWORK_SUBMISSION_PREFIX)
                && !normalizedObjectKey.startsWith(COURSE_HOMEWORK_TEMPLATE_PREFIX)
                && !normalizedObjectKey.startsWith(STUDENT_SOP_PREFIX)
                && !normalizedObjectKey.startsWith(STUDENT_FOLLOWUP_PREFIX))) {
            throw BusinessException.DateError.newInstance("对象Key不允许访问");
        }
        return normalizedObjectKey;
    }

    /**
     * 校验上传文件类型。
     *
     * <p>文件类型规则:
     * <ul>
     *   <li>学生视频(sop/,followup/):.mp4,.mov,.m4v,.webm
     *   <li>其他业务(作业,答案,课程):.md
     * </ul>
     *
     * @param objectKey 文件路径
     * @throws BusinessException 如果文件类型不支持
     */
    private void validateUploadFileType(String objectKey) {
        String lowerObjectKey = objectKey.toLowerCase();

        // 学生视频文件单独校验
        if (lowerObjectKey.startsWith(STUDENT_SOP_PREFIX) || lowerObjectKey.startsWith(STUDENT_FOLLOWUP_PREFIX)) {
            if (!lowerObjectKey.endsWith(".mp4")
                    && !lowerObjectKey.endsWith(".mov")
                    && !lowerObjectKey.endsWith(".m4v")
                    && !lowerObjectKey.endsWith(".webm")) {
                throw BusinessException.DateError.newInstance("只支持视频文件上传");
            }
            return;
        }

        // 其他业务文件只支持 .md
        if (!lowerObjectKey.endsWith(".md")) {
            throw BusinessException.DateError.newInstance("当前仅支持md文件上传");
        }
    }

    /**
     * 获取上传签名有效期(秒)。
     */
    private Long getUploadExpireSeconds() {
        Long uploadExpireSeconds = sysConfigService.get(SysConfig.tosUploadExpireSeconds);
        if (uploadExpireSeconds == null || uploadExpireSeconds <= 0) {
            throw BusinessException.DateError.newInstance("TOS上传签名过期时间配置错误");
        }
        return uploadExpireSeconds;
    }

    /**
     * 获取下载签名有效期(秒)。
     */
    private Long getPreviewExpireSeconds() {
        Long previewExpireSeconds = sysConfigService.get(SysConfig.tosPreviewExpireSeconds);
        if (previewExpireSeconds == null || previewExpireSeconds <= 0) {
            throw BusinessException.DateError.newInstance("TOS下载签名过期时间配置错误");
        }
        return previewExpireSeconds;
    }

    /**
     * 校验 TOS 配置完整性。
     */
    private void validateTosConfig() {
        if (isBlank(tosProperties.getEndpoint()) || isBlank(tosProperties.getRegion()) || isBlank(tosProperties.getBucket())
                || isBlank(tosProperties.getAccessKey()) || isBlank(tosProperties.getSecretKey())) {
            throw BusinessException.DateError.newInstance("TOS配置不完整");
        }
    }

    /**
     * 创建 TOS 客户端。
     */
    private TOSV2 createTosClient() {
        return new TOSV2ClientBuilder().build(tosProperties.getRegion(), tosProperties.getEndpoint(),
                tosProperties.getAccessKey(), tosProperties.getSecretKey());
    }

    /**
     * 判断字符串是否为空。
     */
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
