package cn.yanque.models.teaching.homework.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.teaching.homework.pojo.vo.req.HomeworkCreateReq;
import cn.yanque.models.teaching.homework.pojo.vo.req.HomeworkPageReq;
import cn.yanque.models.teaching.homework.pojo.vo.req.HomeworkPrepareReq;
import cn.yanque.models.teaching.homework.pojo.vo.req.HomeworkUploadSignReq;
import cn.yanque.models.teaching.homework.pojo.vo.res.HomeworkContentRes;
import cn.yanque.models.teaching.homework.pojo.vo.res.HomeworkCreateRes;
import cn.yanque.models.teaching.homework.pojo.vo.res.HomeworkPageRes;
import cn.yanque.models.teaching.homework.pojo.vo.res.HomeworkPrepareRes;
import cn.yanque.models.teaching.homework.pojo.vo.res.HomeworkUploadSignRes;

/**
 * 作业服务。
 */
public interface HomeworkService {

    HomeworkCreateRes addHomework(HomeworkCreateReq req);

    PageResult<HomeworkPageRes> pageHomework(HomeworkPageReq req);

    HomeworkPrepareRes prepareHomework(HomeworkPrepareReq req);

    HomeworkUploadSignRes createContentUploadSign(HomeworkUploadSignReq req);

    HomeworkContentRes getHomeworkContent(Long id);
}
