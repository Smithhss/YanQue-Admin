package cn.yanque.studentFront.pojo.res;

import lombok.Data;

/**
 * 学生考试题目选项响应。
 */
@Data
public class StudentExamPaperOptionRes {

    /** 选项ID */
    private Long id;

    /** 题目ID */
    private Long questionId;

    /** 选项标识：A/B/C/D */
    private String optionKey;

    /** 选项内容 */
    private String optionContent;
}
