package cn.yanque.models.student.followup.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "完成学生回访请求")
public class StudentFollowupRecordCompleteReq {

    @NotBlank(message = "回访内容不能为空")
    @Schema(description = "回访内容")
    private String followupContent;

    @NotBlank(message = "回访会议视频不能为空")
    @Schema(description = "回访会议视频对象Key")
    private String followupVideoObjectKey;

    @NotBlank(message = "回访会议视频文件名不能为空")
    @Schema(description = "回访会议视频文件名")
    private String followupVideoFileName;

    @Schema(description = "备注")
    private String remark;
}
