package cn.yanque.models.student.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.student.pojo.vo.req.StudentLearningPlanCreateReq;
import cn.yanque.models.student.pojo.vo.req.StudentLearningPlanPageReq;
import cn.yanque.models.student.pojo.vo.res.StudentLearningCalendarRes;
import cn.yanque.models.student.pojo.vo.res.StudentLearningPlanCreateRes;
import cn.yanque.models.student.pojo.vo.res.StudentLearningPlanDetailRes;
import cn.yanque.models.student.pojo.vo.res.StudentLearningPlanPageRes;

import java.util.List;

public interface StudentLearningPlanService {

    StudentLearningPlanCreateRes createPlan(StudentLearningPlanCreateReq req);

    PageResult<StudentLearningPlanPageRes> pagePlan(StudentLearningPlanPageReq req);

    StudentLearningPlanDetailRes detail(Long id);

    List<StudentLearningCalendarRes> calendar(Long id);
}
