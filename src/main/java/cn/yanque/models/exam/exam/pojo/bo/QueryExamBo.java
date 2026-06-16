package cn.yanque.models.exam.exam.pojo.bo;

import lombok.Data;

/**
 * 考试列表查询条件。
 */
@Data
public class QueryExamBo {

    private Long paperId;

    private Long classId;

    private Long invigilatorUserId;
}
