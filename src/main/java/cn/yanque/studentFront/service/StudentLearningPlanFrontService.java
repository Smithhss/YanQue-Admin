package cn.yanque.studentFront.service;

import cn.yanque.studentFront.pojo.res.StudentLearningCalendarFrontRes;
import cn.yanque.studentFront.pojo.res.StudentLearningPlanCurrentRes;

import java.util.List;

public interface StudentLearningPlanFrontService {

    StudentLearningPlanCurrentRes current();

    List<StudentLearningCalendarFrontRes> calendar();
}
