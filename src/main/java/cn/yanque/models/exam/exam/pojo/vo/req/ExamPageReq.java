package cn.yanque.models.exam.exam.pojo.vo.req;

import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 考试列表分页查询入参。
 */
@Data
public class ExamPageReq {

    private Long paperId;

    private Long classId;

    private Long invigilatorUserId;

    @Min(value = 1, message = "页码必须大于0")
    private Integer pageNum = 1;

    @Min(value = 1, message = "每页条数必须大于0")
    private Integer pageSize = 10;
}
