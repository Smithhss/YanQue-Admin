package cn.yanque.models.teaching.course.service;

import cn.yanque.models.teaching.course.pojo.vo.req.CourseHomeworkTemplateCreateReq;
import cn.yanque.models.teaching.course.pojo.vo.req.CourseHomeworkTemplateUpdateReq;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseHomeworkTemplateCreateRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseHomeworkTemplateDeleteRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseHomeworkTemplateItemRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseHomeworkTemplateUpdateRes;

import java.util.List;

/**
 * 课程作业标准业务接口。
 */
public interface CourseHomeworkTemplateService {

    CourseHomeworkTemplateCreateRes addTemplate(Long courseId, CourseHomeworkTemplateCreateReq req);

    CourseHomeworkTemplateUpdateRes updateTemplate(CourseHomeworkTemplateUpdateReq req);

    CourseHomeworkTemplateDeleteRes deleteTemplate(Long id);

    CourseHomeworkTemplateItemRes getTemplateById(Long id);

    List<CourseHomeworkTemplateItemRes> listTemplates(Long courseId);
}
