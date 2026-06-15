package cn.yanque.models.teaching.homework.pojo.bo;

import lombok.Data;

import java.util.Date;

/**
 * 作业查询条件。
 */
@Data
public class QueryHomeworkBo {

    private String title;

    private Long classId;

    private Date homeworkDate;
}
