package cn.yanque.models.student.pojo.vo.res;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class StudentLearningPlanDetailRes {

    private Long id;

    private Long studentId;

    private String studentName;

    private String studentPhone;

    private Long courseId;

    private String courseName;

    private Long sopId;

    private Date startDate;

    private String status;

    private List<StudentLearningPlanStageRes> stages;
}
