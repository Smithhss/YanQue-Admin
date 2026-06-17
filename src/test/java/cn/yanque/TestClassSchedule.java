package cn.yanque;

import cn.hutool.core.date.DateUtil;
import cn.yanque.models.student.followup.job.StudentFollowupRecordJob;
import cn.yanque.models.teaching.schedule.pojo.vo.req.AddClassSchuleReq;
import cn.yanque.models.teaching.schedule.pojo.vo.req.TeacherDetailReq;
import cn.yanque.models.teaching.schedule.pojo.vo.res.TeacherDetailRes;
import cn.yanque.models.teaching.schedule.service.ClassScheduleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestClassSchedule {

    @Autowired
    private StudentFollowupRecordJob studentFollowupRecordJob;

    @Test
    public void test1() {
        studentFollowupRecordJob.studentFollowupGenerateJob();
    }

}
