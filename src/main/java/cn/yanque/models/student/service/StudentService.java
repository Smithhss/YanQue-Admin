package cn.yanque.models.student.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.student.pojo.entity.StudentEntity;
import cn.yanque.models.student.pojo.vo.req.StudentAssignClassReq;
import cn.yanque.models.student.pojo.vo.req.StudentPageReq;
import cn.yanque.models.student.pojo.vo.req.StudentSopAssignReq;
import cn.yanque.models.student.pojo.vo.req.StudentTagUpdateReq;
import cn.yanque.models.student.pojo.vo.res.StudentAssignClassRes;
import cn.yanque.models.student.pojo.vo.res.StudentPageRes;
import cn.yanque.models.student.pojo.vo.res.StudentSopAssignRes;
import cn.yanque.models.student.pojo.vo.res.StudentTagUpdateRes;

import java.util.List;

/**
 * 学生管理服务。
 */
public interface StudentService {

    StudentEntity createStudent(StudentEntity student);

    PageResult<StudentPageRes> pageStudent(StudentPageReq req);

    List<String> listStudentTagOptions();

    StudentAssignClassRes assignClass(Long id, StudentAssignClassReq req);

    StudentSopAssignRes assignSop(Long id, StudentSopAssignReq req);

    StudentTagUpdateRes updateStudentTag(Long id, StudentTagUpdateReq req);

    StudentEntity selectByStudentId(Long id);
}
