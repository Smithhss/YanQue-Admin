package cn.yanque.studentFront.pojo.res;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 学生考试试卷题目响应,不包含答案和解析。
 */
@Data
public class StudentExamPaperQuestionRes {

    /** 试卷题目关联ID */
    private Long id;

    /** 题目ID */
    private Long questionId;

    /** 题干 */
    private String questionContent;

    /** 题型 */
    private String questionType;

    /** 难度 */
    private String difficulty;

    /** 题目分数 */
    private BigDecimal questionScore;

    /** 选项列表,非选择题为空 */
    private List<StudentExamPaperOptionRes> options;
}
