package cn.yanque;

import cn.hutool.core.date.DateUtil;
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
    private ClassScheduleService classScheduleService;

    @Test
    public void test1() {
        TeacherDetailReq teacherDetailReq = new TeacherDetailReq();
        teacherDetailReq.setStartTime(DateUtil.parse("2026-05-01", "yyyy-MM-dd"));
        teacherDetailReq.setEndTime(DateUtil.parse("2026-06-30", "yyyy-MM-dd"));
        List<TeacherDetailRes> teacherDetailRes = classScheduleService.teacherDetail(teacherDetailReq);
    }

}
