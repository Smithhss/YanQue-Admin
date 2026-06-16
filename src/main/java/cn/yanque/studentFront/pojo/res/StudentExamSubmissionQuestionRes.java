package cn.yanque.studentFront.pojo.res;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 学生考试提交记录题目响应。
 */
@Data
public class StudentExamSubmissionQuestionRes {

    /** 试卷题目关联ID */
    private Long id;

    /** 题目ID */
    private Long questionId;

    /** 题干 */
    private String questionContent;

    /** 题型 */
    private String questionType;

    /** 题目分数 */
    private BigDecimal questionScore;

    /** 学生答案内容 */
    private String answerContent;

    /** 是否正确，主观题和编程题为空 */
    private Boolean correct;

    /** 本题得分，主观题未批改时为空 */
    private BigDecimal score;

    /** 选项列表，非选择题为空 */
    private List<StudentExamPaperOptionRes> options;
}
