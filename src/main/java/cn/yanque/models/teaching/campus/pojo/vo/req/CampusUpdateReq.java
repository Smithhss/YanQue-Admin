package cn.yanque.models.teaching.campus.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 修改校区请求对象。
 */
@Data
@Schema(description = "修改校区请求")
public class CampusUpdateReq {

    /** 校区ID */
    @Schema(description = "校区ID")
    private Long id;

    /** 校区地点 */
    @NotBlank(message = "校区地点不能为空")
    @Schema(description = "校区地点")
    private String campusLocation;

    /** 负责人姓名 */
    @NotBlank(message = "负责人不能为空")
    @Schema(description = "负责人")
    private String managerName;

    /** 负责人电话 */
    @NotBlank(message = "负责人电话不能为空")
    @Schema(description = "负责人电话")
    private String managerPhone;
}
