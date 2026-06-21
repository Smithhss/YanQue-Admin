package cn.yanque.models.student.coursehour.pojo.vo.res;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class StudentCourseHourPageRes {

    private Long id;

    private Long studentId;

    private String studentName;

    private String studentNo;

    private BigDecimal totalHours;

    private BigDecimal usedHours;

    private BigDecimal remainingHours;

    private Date updatedAt;
}
