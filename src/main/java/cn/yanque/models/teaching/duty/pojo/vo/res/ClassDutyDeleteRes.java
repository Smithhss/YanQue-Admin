package cn.yanque.models.teaching.duty.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "删除值班响应")
public class ClassDutyDeleteRes {

    @Schema(description = "值班ID")
    private Long id;
}
