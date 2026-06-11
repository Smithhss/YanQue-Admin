package cn.yanque.models.teaching.campus.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "校区详情响应")
public class CampusDetailRes {

    @Schema(description = "校区ID")
    private Long id;

    @Schema(description = "校区地点")
    private String campusLocation;

    @Schema(description = "负责人")
    private String managerName;

    @Schema(description = "负责人电话")
    private String managerPhone;

    @Schema(description = "创建时间")
    private Date createdAt;

    @Schema(description = "更新时间")
    private Date updatedAt;
}
