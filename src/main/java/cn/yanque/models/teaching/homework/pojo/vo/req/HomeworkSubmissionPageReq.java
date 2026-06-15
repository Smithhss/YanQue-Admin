package cn.yanque.models.teaching.homework.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 后台作业提交分页请求。
 */
@Data
@Schema(description = "后台作业提交分页请求")
public class HomeworkSubmissionPageReq {

    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNum = 1;

    @Min(value = 1, message = "每页条数不能小于1")
    private Integer pageSize = 10;
}
