package cn.yanque.models.exam.exam.pojo.vo.req;

import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 后台考试提交记录分页请求。
 */
@Data
public class ExamSubmissionPageReq {

    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNum = 1;

    @Min(value = 1, message = "每页条数不能小于1")
    private Integer pageSize = 10;
}
