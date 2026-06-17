package cn.yanque.models.teaching.course.pojo.vo.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 新增课程作业标准请求。
 */
@Data
public class CourseHomeworkTemplateCreateReq {

    private String stageName;

    private Integer dayNumber;

    @NotBlank(message = "作业标准文件不能为空")
    private String contentObjectKey;

    @NotBlank(message = "作业标准文件名不能为空")
    private String contentFileName;

    @NotBlank(message = "状态不能为空")
    private String status;

    private String remark;
}
