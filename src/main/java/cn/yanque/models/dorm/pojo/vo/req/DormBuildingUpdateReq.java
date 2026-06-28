package cn.yanque.models.dorm.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 修改宿舍楼栋请求对象。
 */
@Data
@Schema(description = "修改宿舍楼栋请求")
public class DormBuildingUpdateReq {

    /** 楼栋ID,由Controller从路径参数写入 */
    @Schema(description = "楼栋ID", hidden = true)
    private Long id;

    /** 所属校区ID */
    @NotNull(message = "校区不能为空")
    @Schema(description = "所属校区ID")
    private Long campusId;

    /** 楼栋名称 */
    @NotBlank(message = "楼栋名称不能为空")
    @Schema(description = "楼栋名称")
    private String buildingName;

    /** 性别类型:MALE/FEMALE */
    @NotBlank(message = "性别类型不能为空")
    @Schema(description = "性别类型:MALE男寝/FEMALE女寝")
    private String genderType;

    /** 宿管姓名 */
    @Schema(description = "宿管姓名")
    private String managerName;

    /** 宿管电话 */
    @Schema(description = "宿管电话")
    private String managerPhone;

    /** 状态:ENABLED/DISABLED */
    @Schema(description = "状态:ENABLED启用/DISABLED停用")
    private String status;
}
