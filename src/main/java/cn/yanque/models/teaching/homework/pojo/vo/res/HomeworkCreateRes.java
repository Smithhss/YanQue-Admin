package cn.yanque.models.teaching.homework.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 新增作业响应。
 */
@Data
@Schema(description = "新增作业响应")
public class HomeworkCreateRes {

    @Schema(description = "作业ID")
    private Long id;
}
