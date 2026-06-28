package cn.yanque.models.student.pojo.bo;

import lombok.Data;

/**
 * 学生查询条件。
 */
@Data
public class QueryStudentBo {

    /** 学生姓名 */
    private String studentName;

    /** 手机号 */
    private String studentPhone;

    /** 学历 */
    private String education;

    /** 学校 */
    private String school;

    /** 上课方式:ONLINE线上,OFFLINE线下 */
    private String teachingMode;

    /** 学生标签 */
    private String studentTag;

    /** 状态 */
    private String status;
}
