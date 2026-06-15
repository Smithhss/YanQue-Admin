package cn.yanque.studentFront.service;

import cn.yanque.models.student.pojo.entity.StudentEntity;
import cn.yanque.studentFront.pojo.res.StudentInfoRes;

/**
 * 学生前台登录态服务。
 */
public interface StudentFrontAuthService {

    String createToken(StudentEntity student);

    String createSignSecret(StudentEntity student);

    StudentInfoRes buildStudentInfo(StudentEntity student);
}
