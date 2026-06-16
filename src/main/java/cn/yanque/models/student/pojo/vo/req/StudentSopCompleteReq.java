package cn.yanque.models.student.pojo.vo.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

/**
 * 学生入学SOP完成请求。
 */
@Data
@Schema(description = "学生入学SOP完成请求")
public class StudentSopCompleteReq {

    @NotBlank(message = "SOP视频不能为空")
    @Size(max = 255, message = "SOP视频对象Key不能超过255个字符")
    @Schema(description = "SOP视频对象Key")
    private String sopVideoObjectKey;

    @Size(max = 255, message = "SOP视频文件名不能超过255个字符")
    @Schema(description = "SOP视频文件名")
    private String sopVideoFileName;

    @NotNull(message = "SOP时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @Schema(description = "SOP时间")
    private Date sopTime;
}
