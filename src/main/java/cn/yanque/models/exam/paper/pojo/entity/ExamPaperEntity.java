package cn.yanque.models.exam.paper.pojo.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 试卷实体，对应 exam_paper 表。
 */
@Data
public class ExamPaperEntity {

    /** 试卷ID */
    private Long id;

    /** 试卷名称 */
    private String paperName;

    /** 课程ID */
    private Long courseId;

    /** 阶段名称，为空表示整门课程考试 */
    private String stageName;

    /** 总分数 */
    private BigDecimal totalScore;

    /** 创建时间 */
    private Date createdAt;

    /** 更新时间 */
    private Date updatedAt;
}
