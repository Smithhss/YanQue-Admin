package cn.yanque.models.student.coursehour.pojo.vo.req;

import lombok.Data;

@Data
public class StudentCourseHourPageReq {

    private Integer pageNum;

    private Integer pageSize;

    /** 可选：按学生ID精确筛选 */
    private Long studentId;
}
