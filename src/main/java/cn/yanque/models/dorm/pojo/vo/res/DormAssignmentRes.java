package cn.yanque.models.dorm.pojo.vo.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 入住记录响应对象(联表带出学生与楼栋/房间/床位信息)。
 */
@Data
@Schema(description = "入住记录响应")
public class DormAssignmentRes {

    /** 入住记录ID */
    @Schema(description = "入住记录ID")
    private Long id;

    /** 学生ID */
    @Schema(description = "学生ID")
    private Long studentId;

    /** 学生姓名 */
    @Schema(description = "学生姓名")
    private String studentName;

    /** 床位ID */
    @Schema(description = "床位ID")
    private Long bedId;

    /** 床位号 */
    @Schema(description = "床位号")
    private String bedNo;

    /** 房间ID */
    @Schema(description = "房间ID")
    private Long roomId;

    /** 房间号 */
    @Schema(description = "房间号")
    private String roomNo;

    /** 楼栋ID */
    @Schema(description = "楼栋ID")
    private Long buildingId;

    /** 楼栋名称 */
    @Schema(description = "楼栋名称")
    private String buildingName;

    /** 入住日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "入住日期")
    private Date checkInDate;

    /** 退宿日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "退宿日期,在住时为null")
    private Date checkOutDate;

    /** 状态:LIVING在住/CHECKED_OUT已退宿 */
    @Schema(description = "状态:LIVING在住/CHECKED_OUT已退宿")
    private String status;

    /** 分配操作人ID */
    @Schema(description = "分配操作人ID")
    private Long assignedBy;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private Date createdAt;
}
