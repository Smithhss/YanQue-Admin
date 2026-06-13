package cn.yanque.models.teaching.duty.pojo.vo.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Date;

@Data
@Schema(description = "按日期保存值班请求")
public class ClassDutyDateSaveReq {

    @NotNull(message = "值班日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    @Schema(description = "值班日期")
    private Date dutyDate;

    @Valid
    @Schema(description = "班级值班列表")
    private List<ClassDutyItem> classDutyList;

    @Valid
    @Schema(description = "校区统一值班列表")
    private List<CampusDutyItem> campusDutyList;

    @Data
    @Schema(description = "班级值班项")
    public static class ClassDutyItem {

        @NotNull(message = "班级不能为空")
        @Schema(description = "班级ID")
        private Long classId;

        @NotNull(message = "老师不能为空")
        @Schema(description = "老师ID")
        private Long teacherId;

        @NotNull(message = "值班类型不能为空")
        @Schema(description = "值班类型")
        private String dutyType;
    }

    @Data
    @Schema(description = "校区统一值班项")
    public static class CampusDutyItem {

        @NotNull(message = "校区不能为空")
        @Schema(description = "校区ID")
        private Long campusId;

        @NotNull(message = "老师不能为空")
        @Schema(description = "老师ID")
        private Long teacherId;

        @NotNull(message = "值班类型不能为空")
        @Schema(description = "值班类型")
        private String dutyType;
    }
}
