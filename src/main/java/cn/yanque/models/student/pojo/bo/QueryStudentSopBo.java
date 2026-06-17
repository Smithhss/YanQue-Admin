package cn.yanque.models.student.pojo.bo;

import lombok.Data;

/**
 * 学生入学SOP查询条件。
 */
@Data
public class QueryStudentSopBo {

    /** 学生姓名 */
    private String studentName;

    /** 手机号 */
    private String studentPhone;

    /** 导师ID */
    private Long mentorId;

    /** 状态：ASSIGNED已分配，CANCELED已取消 */
    private String status;
}
