package cn.yanque.models.student.coursehour.pojo.vo.res;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StudentCourseHourRes {

    private Long studentId;

    private BigDecimal totalHours;

    private BigDecimal usedHours;

    private BigDecimal remainingHours;
}
