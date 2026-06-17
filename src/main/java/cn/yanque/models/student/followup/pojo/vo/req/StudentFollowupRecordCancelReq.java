package cn.yanque.models.student.followup.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "取消学生回访请求")
public class StudentFollowupRecordCancelReq {

    @Schema(description = "备注")
    private String remark;
}
