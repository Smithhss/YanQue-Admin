package cn.yanque.models.teaching.attendance.pojo.vo.res;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AttendanceWarningRes {

    private Long studentId;

    private String studentName;

    /** 扣减后剩余课时（负数表示欠课时） */
    private BigDecimal remainingHours;
}
