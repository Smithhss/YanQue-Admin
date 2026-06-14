package cn.yanque.models.student.service.impl;

import cn.yanque.common.enums.ActiveEnum;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.student.mapper.StudentProductMapper;
import cn.yanque.models.student.pojo.entity.StudentProductEntity;
import cn.yanque.models.student.service.StudentProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class StudentProductServiceImpl implements StudentProductService {

    @Autowired
    private StudentProductMapper studentProductMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentProductEntity createStudentProduct(StudentProductEntity studentProduct) {
        studentProduct.setStatus(ActiveEnum.ACTIVE.name());
        studentProduct.setCreatedAt(new Date());
        studentProduct.setUpdatedAt(new Date());
        try {
            studentProductMapper.insert(studentProduct);
        } catch (DuplicateKeyException e) {
            throw BusinessException.DateExist.newInstance("学生产品关系已存在，请勿重复提交");
        }
        return studentProduct;
    }
}
