package cn.yanque.models.teaching.schedule.pojo.vo.res;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TeacherDetailRes {

    private Long teacherId;

    private String teacherName;

    private List<TeacherDetail> teacherDetailList;

    @Data
    public static class TeacherDetail {
        private Date teacheringDate;

        private Long classId;

        private String classPeriod;
    }

}


