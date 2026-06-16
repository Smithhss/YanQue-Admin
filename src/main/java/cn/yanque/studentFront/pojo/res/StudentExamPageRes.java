package cn.yanque.studentFront.pojo.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 学生考试列表响应。
 */
@Data
public class StudentExamPageRes {

    /** 考试ID */
    private Long id;

    /** 试卷ID */
    private Long paperId;

    /** 试卷名称 */
    private String paperName;

    /** 班级ID */
    private Long classId;

    /** 班级期数 */
    private String classPeriod;

    /** 课程ID */
    private Long courseId;

    /** 阶段名称，为空表示整门课程考试 */
    private String stageName;

    /** 可进入考试开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date startTime;

    /** 可进入考试截止时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date endTime;

    /** 学生个人答题时长，单位分钟 */
    private Integer durationMinutes;

    /** 监考老师用户ID */
    private Long invigilatorUserId;

    /** 监考老师名称 */
    private String invigilatorName;

    /** 试卷总分 */
    private BigDecimal totalScore;

    /** 学生考试记录ID，未开始时为空 */
    private Long recordId;

    /** 学生考试记录状态：IN_PROGRESS / SUBMITTED / TIMEOUT */
    private String recordStatus;

    /** 批改状态：PENDING / GRADING / COMPLETED */
    private String gradingStatus;

    /** 学生实际开始答题时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date recordStartTime;

    /** 当前学生本次考试截止时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date deadlineTime;

    /** 提交时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date submitTime;

    /** 当前已计算得分，主观题未批改时只包含客观题得分 */
    private BigDecimal score;

    /** 页面展示状态：NOT_STARTED / AVAILABLE / IN_PROGRESS / SUBMITTED / TIMEOUT / ENDED */
    private String examStatus;

    /** 页面展示状态文案 */
    private String examStatusText;

    /** 是否允许点击开始或继续考试 */
    private Boolean canStart;
}
