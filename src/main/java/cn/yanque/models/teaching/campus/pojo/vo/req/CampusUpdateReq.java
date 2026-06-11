package cn.yanque.models.teaching.campus.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "修改校区请求")
public class CampusUpdateReq {

    @Schema(description = "校区ID")
    private Long id;

    @NotBlank(message = "校区地点不能为空")
    @Schema(description = "校区地点")
    private String campusLocation;

    @NotBlank(message = "负责人不能为空")
    @Schema(description = "负责人")
    private String managerName;

    @NotBlank(message = "负责人电话不能为空")
    @Schema(description = "负责人电话")
    private String managerPhone;
}
