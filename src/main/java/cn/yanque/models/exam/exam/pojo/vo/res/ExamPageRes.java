package cn.yanque.models.exam.exam.pojo.vo.res;

import lombok.Data;

import java.util.Date;

/**
 * 考试列表响应。
 */
@Data
public class ExamPageRes {

    private Long id;

    private Long paperId;

    private String paperName;

    private Long classId;

    private String classPeriod;

    private Date startTime;

    private Date endTime;

    private Integer durationMinutes;

    private Long invigilatorUserId;

    private String invigilatorName;

    private Boolean answerVisible;

    private Date createdAt;

    private Date updatedAt;
}
