package cn.yanque.models.student.followup.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.student.followup.pojo.vo.req.StudentFollowupTagCreateReq;
import cn.yanque.models.student.followup.pojo.vo.req.StudentFollowupTagPageReq;
import cn.yanque.models.student.followup.pojo.vo.req.StudentFollowupTagUpdateReq;
import cn.yanque.models.student.followup.pojo.vo.res.StudentFollowupTagCreateRes;
import cn.yanque.models.student.followup.pojo.vo.res.StudentFollowupTagDeleteRes;
import cn.yanque.models.student.followup.pojo.vo.res.StudentFollowupTagDetailRes;
import cn.yanque.models.student.followup.pojo.vo.res.StudentFollowupTagPageRes;
import cn.yanque.models.student.followup.pojo.vo.res.StudentFollowupTagUpdateRes;

public interface StudentFollowupTagService {

    StudentFollowupTagCreateRes add(StudentFollowupTagCreateReq req);

    StudentFollowupTagUpdateRes update(StudentFollowupTagUpdateReq req);

    StudentFollowupTagDeleteRes delete(Long id);

    StudentFollowupTagDetailRes detail(Long id);

    PageResult<StudentFollowupTagPageRes> page(StudentFollowupTagPageReq req);
}
