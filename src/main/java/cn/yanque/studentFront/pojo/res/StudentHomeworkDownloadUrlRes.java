package cn.yanque.studentFront.pojo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 学生作业下载预签名响应。
 */
@Data
@Schema(description = "学生作业下载预签名响应")
public class StudentHomeworkDownloadUrlRes {

    private String fileName;

    private String downloadUrl;

    private Long expires;
}
