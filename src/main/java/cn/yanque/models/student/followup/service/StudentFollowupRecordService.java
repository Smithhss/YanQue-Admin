package cn.yanque.models.student.followup.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.student.followup.pojo.vo.req.StudentFollowupRecordCancelReq;
import cn.yanque.models.student.followup.pojo.vo.req.StudentFollowupRecordCompleteReq;
import cn.yanque.models.student.followup.pojo.vo.req.StudentFollowupRecordPageReq;
import cn.yanque.models.student.followup.pojo.vo.res.StudentFollowupRecordDetailRes;
import cn.yanque.models.student.followup.pojo.vo.res.StudentFollowupRecordGenerateRes;
import cn.yanque.models.student.followup.pojo.vo.res.StudentFollowupRecordPageRes;
import cn.yanque.models.student.followup.pojo.vo.res.StudentFollowupRecordStatsRes;
import cn.yanque.models.student.followup.pojo.vo.res.StudentFollowupRecordUpdateRes;

import java.util.Date;

public interface StudentFollowupRecordService {

    PageResult<StudentFollowupRecordPageRes> page(StudentFollowupRecordPageReq req);

    StudentFollowupRecordStatsRes stats();

    StudentFollowupRecordDetailRes detail(Long id);

    StudentFollowupRecordGenerateRes generateDueRecords(Date generateDate);

    StudentFollowupRecordUpdateRes complete(Long id, Long followupUserId, StudentFollowupRecordCompleteReq req);

    StudentFollowupRecordUpdateRes cancel(Long id, StudentFollowupRecordCancelReq req);
}
