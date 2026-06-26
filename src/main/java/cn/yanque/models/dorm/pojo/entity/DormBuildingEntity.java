package cn.yanque.models.dorm.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * 宿舍楼栋实体，对应 dorm_building 表。
 */
@Data
public class DormBuildingEntity {

    /** 楼栋ID */
    private Long id;

    /** 所属校区ID（sys_campus.id） */
    private Long campusId;

    /** 楼栋名称 */
    private String buildingName;

    /** 性别类型：MALE男寝/FEMALE女寝 */
    private String genderType;

    /** 宿管姓名 */
    private String managerName;

    /** 宿管电话 */
    private String managerPhone;

    /** 状态：ENABLED启用/DISABLED停用 */
    private String status;

    /** 创建时间 */
    private Date createdAt;

    /** 更新时间 */
    private Date updatedAt;
}
