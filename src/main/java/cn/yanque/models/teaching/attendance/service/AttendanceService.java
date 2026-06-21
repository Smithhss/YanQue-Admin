package cn.yanque.models.teaching.attendance.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.teaching.attendance.pojo.vo.req.AttendanceCommitReq;
import cn.yanque.models.teaching.attendance.pojo.vo.req.AttendancePageReq;
import cn.yanque.models.teaching.attendance.pojo.vo.res.AttendanceCommitRes;
import cn.yanque.models.teaching.attendance.pojo.vo.res.AttendancePageRes;
import cn.yanque.models.teaching.attendance.pojo.vo.res.AttendanceRosterRes;

public interface AttendanceService {

    AttendanceRosterRes roster(Long scheduleId);

    AttendanceCommitRes commit(AttendanceCommitReq req, Long operatorId);

    PageResult<AttendancePageRes> page(AttendancePageReq req);
}
