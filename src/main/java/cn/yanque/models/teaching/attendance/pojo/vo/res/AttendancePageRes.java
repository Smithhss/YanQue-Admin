package cn.yanque.models.teaching.attendance.pojo.vo.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AttendancePageRes {

    private Long id;

    private Long scheduleId;

    private Long classId;

    private Long studentId;

    private String studentName;

    private String studentNo;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date scheduleDate;

    private String status;

    private BigDecimal hourDeducted;
}
