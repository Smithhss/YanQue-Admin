package cn.yanque.models.exam.exam.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * 考试安排实体,对应 exam 表。
 */
@Data
public class ExamEntity {

    /** 考试ID */
    private Long id;

    /** 试卷ID */
    private Long paperId;

    /** 班级ID */
    private Long classId;

    /** 可进入考试开始时间 */
    private Date startTime;

    /** 可进入考试截止时间 */
    private Date endTime;

    /** 学生个人答题时长,单位分钟 */
    private Integer durationMinutes;

    /** 监考老师用户ID */
    private Long invigilatorUserId;

    /** 是否向学生公布答卷判定结果 */
    private Boolean answerVisible;

    /** 创建时间 */
    private Date createdAt;

    /** 更新时间 */
    private Date updatedAt;
}
