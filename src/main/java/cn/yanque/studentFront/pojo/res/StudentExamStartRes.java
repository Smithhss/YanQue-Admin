package cn.yanque.studentFront.pojo.res;

import lombok.Data;

import java.util.Date;

/**
 * 学生开始考试响应。
 */
@Data
public class StudentExamStartRes {

    private Long recordId;

    private Long examId;

    private Long paperId;

    private Date startTime;

    private Date deadlineTime;

    private String status;
}
