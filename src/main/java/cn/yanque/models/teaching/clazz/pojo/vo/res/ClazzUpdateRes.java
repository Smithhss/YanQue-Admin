package cn.yanque.models.teaching.clazz.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "修改班级响应")
public class ClazzUpdateRes {

    @Schema(description = "班级ID")
    private Long id;
}
