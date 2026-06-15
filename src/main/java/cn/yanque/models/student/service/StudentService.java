package cn.yanque.models.student.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.student.pojo.entity.StudentEntity;
import cn.yanque.models.student.pojo.vo.req.StudentAssignClassReq;
import cn.yanque.models.student.pojo.vo.req.StudentPageReq;
import cn.yanque.models.student.pojo.vo.res.StudentAssignClassRes;
import cn.yanque.models.student.pojo.vo.res.StudentPageRes;

/**
 * 学生管理服务。
 */
public interface StudentService {

    StudentEntity createStudent(StudentEntity student);

    PageResult<StudentPageRes> pageStudent(StudentPageReq req);

    StudentAssignClassRes assignClass(Long id, StudentAssignClassReq req);
}
