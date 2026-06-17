package cn.yanque.models.student.followup.job;

import cn.yanque.models.student.followup.pojo.vo.res.StudentFollowupRecordGenerateRes;
import cn.yanque.models.student.followup.service.StudentFollowupRecordService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class StudentFollowupRecordJob {

    @Autowired
    private StudentFollowupRecordService studentFollowupRecordService;

    @XxlJob("studentFollowupGenerateJob")
    public void studentFollowupGenerateJob() {
        StudentFollowupRecordGenerateRes res = studentFollowupRecordService.generateDueRecords(new Date());
        log.info("studentFollowupGenerateJob finished, generatedCount={}", res.getGeneratedCount());
    }
}
