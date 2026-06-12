package cn.yanque.models.teaching.schedule.pojo.vo.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class AddClassSchuleReq {

    private String courseContent;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date scheduleDate;

    private Long teacherId;
}
