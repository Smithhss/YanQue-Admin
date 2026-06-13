package cn.yanque.models.teaching.duty.pojo.vo.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Schema(description = "按日期查询值班响应")
public class ClassDutyDateRes {

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    @Schema(description = "值班日期")
    private Date dutyDate;

    @Schema(description = "班级值班列表")
    private List<ClassDutyClassItemRes> classDutyList;

    @Schema(description = "校区统一值班列表")
    private List<ClassDutyCampusItemRes> campusDutyList;
}
