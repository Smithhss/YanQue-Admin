package cn.yanque.models.dorm.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 宿舍楼栋响应对象（详情与分页列表共用）。
 */
@Data
@Schema(description = "宿舍楼栋响应")
public class DormBuildingRes {

    /** 楼栋ID */
    @Schema(description = "楼栋ID")
    private Long id;

    /** 所属校区ID */
    @Schema(description = "所属校区ID")
    private Long campusId;

    /** 楼栋名称 */
    @Schema(description = "楼栋名称")
    private String buildingName;

    /** 性别类型：MALE/FEMALE */
    @Schema(description = "性别类型：MALE男寝/FEMALE女寝")
    private String genderType;

    /** 宿管姓名 */
    @Schema(description = "宿管姓名")
    private String managerName;

    /** 宿管电话 */
    @Schema(description = "宿管电话")
    private String managerPhone;

    /** 状态 */
    @Schema(description = "状态：ENABLED启用/DISABLED停用")
    private String status;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private Date createdAt;

    /** 更新时间 */
    @Schema(description = "更新时间")
    private Date updatedAt;
}
