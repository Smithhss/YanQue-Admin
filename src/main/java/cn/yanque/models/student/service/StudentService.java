package cn.yanque.models.student.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.student.pojo.entity.StudentEntity;
import cn.yanque.models.student.pojo.vo.req.StudentPageReq;
import cn.yanque.models.student.pojo.vo.res.StudentPageRes;

/**
 * 学生管理服务。
 */
public interface StudentService {

    StudentEntity createStudent(StudentEntity student);

    PageResult<StudentPageRes> pageStudent(StudentPageReq req);
}
