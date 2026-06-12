package cn.yanque;

import cn.hutool.core.date.DateUtil;
import cn.yanque.models.teaching.schedule.pojo.vo.req.AddClassSchuleReq;
import cn.yanque.models.teaching.schedule.service.ClassScheduleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestClassSchedule {

    @Autowired
    private ClassScheduleService classScheduleService;

    @Test
    public void test1() {
        AddClassSchuleReq addClassSchuleReq = new AddClassSchuleReq();

        addClassSchuleReq.setScheduleDate(DateUtil.parse("2026-06-25", "yyyy-MM-dd"));
        addClassSchuleReq.setCourseContent("xxxxx");
        addClassSchuleReq.setTeacherId(10L);
        classScheduleService.addClassSchule(1L, addClassSchuleReq);
    }

}
