package cn.yanque.studentFront.service.impl;

import cn.yanque.common.exception.BusinessException;
import cn.yanque.common.threadlocal.StudentThreadLocal;
import cn.yanque.models.student.pojo.entity.StudentEntity;
import cn.yanque.models.student.service.StudentService;
import cn.yanque.studentFront.pojo.req.StudentProfileUpdateReq;
import cn.yanque.studentFront.pojo.res.StudentProfileRes;
import cn.yanque.studentFront.service.StudentProfileFrontService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentProfileFrontServiceImpl implements StudentProfileFrontService {

    @Autowired
    private StudentService studentService;

    @Override
    public StudentProfileRes getProfile() {
        StudentEntity student = StudentThreadLocal.get();
        if (student == null) {
            throw new BusinessException(401, "学生未登录");
        }
        return toRes(student);
    }

    @Override
    public StudentProfileRes updateProfile(StudentProfileUpdateReq req) {
        StudentEntity student = StudentThreadLocal.get();
        if (student == null) {
            throw new BusinessException(401, "学生未登录");
        }
        studentService.updateProfile(student.getId(), req.getEducation(), req.getGradeYear(), req.getSchool(), req.getMajor());
        StudentEntity updated = studentService.selectByStudentId(student.getId());
        return toRes(updated);
    }

    private StudentProfileRes toRes(StudentEntity e) {
        StudentProfileRes res = new StudentProfileRes();
        res.setId(e.getId());
        res.setName(e.getStudentName());
        res.setPhone(e.getStudentPhone());
        res.setGender(e.getGender());
        res.setEducation(e.getEducation());
        res.setGradeYear(e.getGradeYear());
        res.setSchool(e.getSchool());
        res.setMajor(e.getMajor());
        res.setTeachingMode(e.getTeachingMode());
        res.setStatus(e.getStatus());
        return res;
    }
}
