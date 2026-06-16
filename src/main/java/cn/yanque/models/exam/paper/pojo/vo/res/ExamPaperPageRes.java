package cn.yanque.models.exam.paper.pojo.vo.res;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 试卷分页响应。
 */
@Data
public class ExamPaperPageRes {

    private Long id;

    private String paperName;

    private Long courseId;

    private String courseName;

    private String stageName;

    private BigDecimal totalScore;

    private Integer questionCount;

    private Date createdAt;

    private Date updatedAt;
}
