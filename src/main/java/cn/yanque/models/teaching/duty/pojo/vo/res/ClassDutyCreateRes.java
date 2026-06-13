package cn.yanque.models.teaching.duty.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "新增值班响应")
public class ClassDutyCreateRes {

    @Schema(description = "值班ID")
    private Long id;
}
