package cn.yanque.models.teaching.campus.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "校区分页响应")
public class CampusPageRes {

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
}
