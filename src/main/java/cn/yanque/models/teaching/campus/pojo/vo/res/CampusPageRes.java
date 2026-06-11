package cn.yanque.models.teaching.campus.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 校区分页列表响应对象。
 */
@Data
@Schema(description = "校区分页响应")
public class CampusPageRes {

    /** 校区ID */
    @Schema(description = "校区ID")
    private Long id;

    /** 校区地点 */
    @Schema(description = "校区地点")
    private String campusLocation;

    /** 负责人姓名 */
    @Schema(description = "负责人")
    private String managerName;

    /** 负责人电话 */
    @Schema(description = "负责人电话")
    private String managerPhone;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private Date createdAt;
}
