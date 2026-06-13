package cn.yanque.models.teaching.duty.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "修改值班响应")
public class ClassDutyUpdateRes {

    @Schema(description = "值班ID")
    private Long id;
}
