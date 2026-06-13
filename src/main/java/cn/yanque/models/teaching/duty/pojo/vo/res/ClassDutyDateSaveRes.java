package cn.yanque.models.teaching.duty.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "按日期保存值班响应")
public class ClassDutyDateSaveRes {

    @Schema(description = "保存数量")
    private Integer saveCount;
}
