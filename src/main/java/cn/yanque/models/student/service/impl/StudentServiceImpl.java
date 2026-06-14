package cn.yanque.models.student.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.yanque.common.api.PageResult;
import cn.yanque.common.enums.ActiveEnum;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.student.mapper.StudentMapper;
import cn.yanque.models.student.pojo.bo.QueryStudentBo;
import cn.yanque.models.student.pojo.entity.StudentEntity;
import cn.yanque.models.student.pojo.vo.req.StudentPageReq;
import cn.yanque.models.student.pojo.vo.res.StudentPageRes;
import cn.yanque.models.student.service.StudentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentEntity createStudent(StudentEntity student) {
        student.setStudentNo(createStudentNo());
        student.setStatus(ActiveEnum.ACTIVE.name());
        student.setCreatedAt(new Date());
        student.setUpdatedAt(new Date());

        try {
            studentMapper.insert(student);
        } catch (DuplicateKeyException e) {
            throw BusinessException.DateExist.newInstance("学生资料已存在，请勿重复提交");
        }

        return student;
    }

    @Override
    public PageResult<StudentPageRes> pageStudent(StudentPageReq req) {
        int pageNum = req.getPageNum() == null ? 1 : req.getPageNum();
        int pageSize = req.getPageSize() == null ? 10 : req.getPageSize();
        QueryStudentBo queryStudentBo = new QueryStudentBo();
        BeanUtils.copyProperties(req, queryStudentBo);
        PageHelper.startPage(pageNum, pageSize);
        List<StudentEntity> list = studentMapper.selectPage(queryStudentBo);
        PageInfo<StudentEntity> pageInfo = new PageInfo<>(list);
        List<StudentPageRes> records = list.stream().map(this::buildStudentPageRes).toList();
        return new PageResult<>(pageInfo.getTotal(), pageNum, pageSize, records);
    }

    private StudentPageRes buildStudentPageRes(StudentEntity student) {
        StudentPageRes res = new StudentPageRes();
        BeanUtils.copyProperties(student, res);
        return res;
    }

    private String createStudentNo() {
        return "STU" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN) + RandomUtil.randomNumbers(6);
    }
}
