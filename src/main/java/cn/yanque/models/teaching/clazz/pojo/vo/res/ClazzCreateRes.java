package cn.yanque.models.teaching.clazz.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 新增班级响应对象。
 */
@Data
@Schema(description = "创建班级响应")
public class ClazzCreateRes {

    /** 班级ID */
    @Schema(description = "班级ID")
    private Long id;
}
