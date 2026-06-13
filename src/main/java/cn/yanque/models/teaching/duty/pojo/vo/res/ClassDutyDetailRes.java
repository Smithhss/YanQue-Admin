package cn.yanque.models.teaching.duty.pojo.vo.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "值班详情响应")
public class ClassDutyDetailRes {

    @Schema(description = "值班ID")
    private Long id;

    @Schema(description = "班级ID")
    private Long classId;

    @Schema(description = "班级期数")
    private String classPeriod;

    @Schema(description = "校区ID")
    private Long campusId;

    @Schema(description = "校区名称")
    private String campusName;

    @Schema(description = "老师ID")
    private Long teacherId;

    @Schema(description = "老师名称")
    private String teacherName;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    @Schema(description = "值班日期")
    private Date dutyDate;

    @Schema(description = "值班类型")
    private String dutyType;

    @Schema(description = "值班类型描述")
    private String dutyTypeDesc;

    @Schema(description = "开始时间")
    private String startTime;

    @Schema(description = "结束时间")
    private String endTime;

    @Schema(description = "备注")
    private String remark;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @Schema(description = "创建时间")
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @Schema(description = "更新时间")
    private Date updatedAt;
}
