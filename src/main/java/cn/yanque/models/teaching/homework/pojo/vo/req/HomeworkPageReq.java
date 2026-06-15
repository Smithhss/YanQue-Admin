package cn.yanque.models.teaching.homework.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 作业分页查询请求。
 */
@Data
@Schema(description = "作业分页查询请求")
public class HomeworkPageReq {

    @Schema(description = "作业标题")
    private String title;

    @Schema(description = "班级ID")
    private Long classId;

    @Schema(description = "作业日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date homeworkDate;

    @Min(value = 1, message = "页码不能小于1")
    @Schema(description = "页码", defaultValue = "1")
    private Integer pageNum = 1;

    @Min(value = 1, message = "每页条数不能小于1")
    @Schema(description = "每页条数", defaultValue = "10")
    private Integer pageSize = 10;
}
