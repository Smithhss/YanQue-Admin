package cn.yanque.models.exam.exam.pojo.vo.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

/**
 * 新增/修改考试安排入参。
 */
@Data
public class ExamSaveReq {

    private Long id;

    @NotNull(message = "试卷不能为空")
    private Long paperId;

    @NotNull(message = "班级不能为空")
    private Long classId;

    @NotNull(message = "可进入开始时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date startTime;

    @NotNull(message = "可进入截止时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date endTime;

    @NotNull(message = "答题时长不能为空")
    @Min(value = 1, message = "答题时长必须大于0")
    private Integer durationMinutes;

    @NotNull(message = "监考老师不能为空")
    private Long invigilatorUserId;
}
