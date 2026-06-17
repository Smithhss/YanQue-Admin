package cn.yanque.models.student.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.student.pojo.vo.req.StudentSopCompleteReq;
import cn.yanque.models.student.pojo.vo.req.StudentSopPageReq;
import cn.yanque.models.student.pojo.vo.res.StudentSopCompleteRes;
import cn.yanque.models.student.pojo.vo.res.StudentSopPageRes;

/**
 * 学生入学SOP管理服务。
 */
public interface StudentSopService {

    PageResult<StudentSopPageRes> pageStudentSop(StudentSopPageReq req);

    StudentSopCompleteRes completeSop(Long id, StudentSopCompleteReq req);
}
