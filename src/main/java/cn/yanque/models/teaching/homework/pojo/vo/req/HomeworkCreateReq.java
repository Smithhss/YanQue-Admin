package cn.yanque.models.teaching.homework.pojo.vo.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

/**
 * 新增作业请求。
 */
@Data
@Schema(description = "新增作业请求")
public class HomeworkCreateReq {

    @NotBlank(message = "作业标题不能为空")
    @Schema(description = "作业标题")
    private String title;

    @NotBlank(message = "作业内容对象Key不能为空")
    @Schema(description = "作业内容对象存储Key")
    private String contentObjectKey;

    @NotBlank(message = "作业内容文件名不能为空")
    @Schema(description = "作业内容文件名")
    private String contentFileName;

    @NotNull(message = "班级ID不能为空")
    @Schema(description = "班级ID")
    private Long classId;

    @NotNull(message = "作业日期不能为空")
    @Schema(description = "作业日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date homeworkDate;

    @NotBlank(message = "课程内容不能为空")
    @Schema(description = "课程内容")
    private String classContent;

    @NotNull(message = "开始时间不能为空")
    @Schema(description = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @NotNull(message = "截止时间不能为空")
    @Schema(description = "截止时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deadline;

    @Schema(description = "备注")
    private String remark;
}
