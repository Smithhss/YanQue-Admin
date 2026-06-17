package cn.yanque.models.student.pojo.vo.req;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class StudentLearningPlanPageReq {

    private String studentName;

    private String studentPhone;

    private Long courseId;

    private String status;

    @Min(value = 1, message = "pageNum不能小于1")
    private Integer pageNum = 1;

    @Min(value = 1, message = "pageSize不能小于1")
    private Integer pageSize = 10;
}
