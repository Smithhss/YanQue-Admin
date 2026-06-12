package cn.yanque.models.users.pojo.vo.res;

import cn.yanque.common.enums.ActiveEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 用户详情响应对象。
 */
@Data
@Schema(description = "用户详情响应")
public class UserDetailRes {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "电话")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "飞书 union_id")
    private String unionId;

    @Schema(description = "状态，ACTIVE启用，INACTIVE禁用")
    private String status;

    /** 状态中文描述 */
    private String statusDesc;

    @Schema(description = "创建时间")
    private Date createdAt;

    @Schema(description = "更新时间")
    private Date updatedAt;

    /**
     * 设置状态时同步生成状态描述。
     *
     * @param status 用户状态
     */
    public void setStatus(String status) {
        this.status = status;
        this.setStatusDesc(ActiveEnum.valueOf(status).getDesc());
    }
}
