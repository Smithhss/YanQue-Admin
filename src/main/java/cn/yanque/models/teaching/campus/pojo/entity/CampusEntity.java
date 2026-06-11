package cn.yanque.models.teaching.campus.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * 校区实体，对应 sys_campus 表。
 */
@Data
public class CampusEntity {

    /** 校区ID */
    private Long id;

    /** 校区地点 */
    private String campusLocation;

    /** 负责人姓名 */
    private String managerName;

    /** 负责人电话 */
    private String managerPhone;

    /** 创建时间 */
    private Date createdAt;

    /** 更新时间 */
    private Date updatedAt;
}
