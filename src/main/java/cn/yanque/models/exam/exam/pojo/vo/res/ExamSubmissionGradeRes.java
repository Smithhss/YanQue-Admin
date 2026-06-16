package cn.yanque.models.exam.exam.pojo.vo.res;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 后台考试答卷批改响应。
 */
@Data
public class ExamSubmissionGradeRes {

    private Long recordId;

    private BigDecimal score;

    private String gradingStatus;
}
