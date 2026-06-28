package cn.yanque.models.users.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 权限详情响应对象。
 */
@Data
@Schema(description = "权限详情响应")
public class PermissionDetailRes {

    @Schema(description = "权限ID")
    private Long id;

    @Schema(description = "父权限ID,根节点为0")
    private Long parentId;

    @Schema(description = "权限编码")
    private String permissionCode;

    @Schema(description = "权限名称")
    private String permissionName;

    @Schema(description = "权限类型,例如API,MENU,BUTTON")
    private String permissionType;

    @Schema(description = "API路径匹配规则,仅API权限使用")
    private String apiPath;

    @Schema(description = "排序值,越小越靠前")
    private Integer sortNum;

    @Schema(description = "权限描述")
    private String description;

    @Schema(description = "状态", allowableValues = {"ACTIVE", "INACTIVE"})
    private String status;

    @Schema(description = "创建时间")
    private Date createdAt;

    @Schema(description = "更新时间")
    private Date updatedAt;
}
