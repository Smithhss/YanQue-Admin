package cn.yanque.models.student.pojo.vo.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class StudentLearningPlanCreateReq {

    @NotNull(message = "SOP记录不能为空")
    private Long sopId;

    @NotNull(message = "课程不能为空")
    private Long courseId;

    @NotNull(message = "开始学习日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date startDate;

    @Valid
    @NotEmpty(message = "阶段学习天数不能为空")
    private List<StudentLearningPlanStageReq> stages;
}
