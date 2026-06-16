package cn.yanque.models.exam.paper.pojo.bo;

import lombok.Data;

/**
 * 试卷分页查询条件。
 */
@Data
public class QueryExamPaperBo {

    /** 课程ID */
    private Long courseId;

    /** 阶段名称 */
    private String stageName;

    /** 试卷名称关键字 */
    private String paperName;
}
