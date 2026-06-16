package cn.yanque.models.exam.paper.pojo.vo.req;

import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 试卷分页查询入参。
 */
@Data
public class ExamPaperPageReq {

    private Long courseId;

    private String stageName;

    private String paperName;

    @Min(value = 1, message = "页码必须大于0")
    private Integer pageNum = 1;

    @Min(value = 1, message = "每页条数必须大于0")
    private Integer pageSize = 10;
}
