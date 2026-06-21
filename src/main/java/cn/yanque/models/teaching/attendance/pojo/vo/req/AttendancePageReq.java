package cn.yanque.models.teaching.attendance.pojo.vo.req;

import lombok.Data;

@Data
public class AttendancePageReq {

    private Integer pageNum;

    private Integer pageSize;

    /** 可选：按班级筛选 */
    private Long classId;

    /** 可选：按学生筛选 */
    private Long studentId;

    /** 可选：上课日期起（yyyy-MM-dd） */
    private String dateFrom;

    /** 可选：上课日期止（yyyy-MM-dd） */
    private String dateTo;
}
