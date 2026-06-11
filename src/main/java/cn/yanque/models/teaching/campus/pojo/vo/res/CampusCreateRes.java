package cn.yanque.models.teaching.campus.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 新增校区响应对象。
 */
@Data
@Schema(description = "创建校区响应")
public class CampusCreateRes {

    /** 校区ID */
    @Schema(description = "校区ID")
    private Long id;
}
