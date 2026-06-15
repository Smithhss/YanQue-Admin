package cn.yanque.models.teaching.homework.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.teaching.homework.pojo.vo.req.HomeworkCreateReq;
import cn.yanque.models.teaching.homework.pojo.vo.req.HomeworkPageReq;
import cn.yanque.models.teaching.homework.pojo.vo.req.HomeworkPrepareReq;
import cn.yanque.models.teaching.homework.pojo.vo.req.HomeworkPublishAnswerReq;
import cn.yanque.models.teaching.homework.pojo.vo.req.HomeworkSubmissionGradeReq;
import cn.yanque.models.teaching.homework.pojo.vo.req.HomeworkSubmissionPageReq;
import cn.yanque.models.teaching.homework.pojo.vo.res.HomeworkCreateRes;
import cn.yanque.models.teaching.homework.pojo.vo.res.HomeworkPageRes;
import cn.yanque.models.teaching.homework.pojo.vo.res.HomeworkPrepareRes;
import cn.yanque.models.teaching.homework.pojo.vo.res.HomeworkPublishAnswerRes;
import cn.yanque.models.teaching.homework.pojo.vo.res.HomeworkSubmissionGradeRes;
import cn.yanque.models.teaching.homework.pojo.vo.res.HomeworkSubmissionPageRes;

/**
 * 作业服务。
 */
public interface HomeworkService {

    HomeworkCreateRes addHomework(HomeworkCreateReq req);

    PageResult<HomeworkPageRes> pageHomework(HomeworkPageReq req);

    HomeworkPrepareRes prepareHomework(HomeworkPrepareReq req);

    HomeworkPublishAnswerRes publishAnswer(Long id, HomeworkPublishAnswerReq req);

    PageResult<HomeworkSubmissionPageRes> pageSubmissions(Long homeworkId, HomeworkSubmissionPageReq req);

    HomeworkSubmissionGradeRes gradeSubmission(Long submissionId, HomeworkSubmissionGradeReq req);
}
