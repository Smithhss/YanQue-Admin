package cn.yanque.models.student.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.yanque.common.api.PageResult;
import cn.yanque.common.enums.ActiveEnum;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.order.product.mapper.ProductMapper;
import cn.yanque.models.order.product.pojo.entity.ProductEntity;
import cn.yanque.models.student.mapper.StudentMapper;
import cn.yanque.models.student.mapper.StudentProductMapper;
import cn.yanque.models.student.pojo.bo.QueryStudentBo;
import cn.yanque.models.student.pojo.entity.StudentEntity;
import cn.yanque.models.student.pojo.entity.StudentProductEntity;
import cn.yanque.models.student.pojo.vo.req.StudentAssignClassReq;
import cn.yanque.models.student.pojo.vo.req.StudentPageReq;
import cn.yanque.models.student.pojo.vo.res.StudentAssignClassRes;
import cn.yanque.models.student.pojo.vo.res.StudentPageRes;
import cn.yanque.models.student.service.StudentService;
import cn.yanque.models.teaching.clazz.mapper.ClazzMapper;
import cn.yanque.models.teaching.clazz.pojo.entity.ClazzEntity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private static final String TEACHING_MODE_ONLINE = "ONLINE";
    private static final String TEACHING_MODE_OFFLINE = "OFFLINE";

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private StudentProductMapper studentProductMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ClazzMapper clazzMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentEntity createStudent(StudentEntity student) {
        fillAndValidateTeachingMode(student);
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

        Map<Long, String> productContentMap = buildProductContentMap(list);
        Map<Long, String> classPeriodMap = buildClassPeriodMap(list);
        List<StudentPageRes> records = list.stream().map(student -> buildStudentPageRes(student, productContentMap, classPeriodMap)).toList();
        return new PageResult<>(pageInfo.getTotal(), pageNum, pageSize, records);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentAssignClassRes assignClass(Long id, StudentAssignClassReq req) {
        StudentEntity student = studentMapper.selectById(id);
        if (student == null) {
            throw BusinessException.DateError.newInstance("学生不存在");
        }
        if (!TEACHING_MODE_OFFLINE.equals(student.getTeachingMode())) {
            throw BusinessException.DateError.newInstance("只有线下学生需要分配班级");
        }
        if (clazzMapper.selectById(req.getClassId()) == null) {
            throw BusinessException.DateError.newInstance("班级不存在");
        }
        studentMapper.updateClassId(id, req.getClassId());

        StudentAssignClassRes res = new StudentAssignClassRes();
        res.setStudentId(id);
        res.setClassId(req.getClassId());
        return res;
    }

    private void fillAndValidateTeachingMode(StudentEntity student) {
        String teachingMode = student.getTeachingMode();
        if (teachingMode == null || teachingMode.isBlank()) {
            teachingMode = TEACHING_MODE_ONLINE;
        }
        if (!TEACHING_MODE_ONLINE.equals(teachingMode) && !TEACHING_MODE_OFFLINE.equals(teachingMode)) {
            throw BusinessException.DateError.newInstance("上课方式只能是ONLINE或OFFLINE");
        }
        student.setTeachingMode(teachingMode);
    }

    private Map<Long, String> buildProductContentMap(List<StudentEntity> students) {
        List<Long> studentIds = students.stream().map(StudentEntity::getId).toList();
        if (studentIds.isEmpty()) {
            return Map.of();
        }
        // 所有student对应的产品
        List<StudentProductEntity> studentProducts = studentProductMapper.selectByStudentIds(studentIds);
        if (studentProducts.isEmpty()) {
            return Map.of();
        }
        List<String> productIds = studentProducts.stream()
                .map(StudentProductEntity::getProductId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        //Map<String, String> productMap key 产品id， value 产品内容
        Map<String, String> productMap = productIds.isEmpty() ? Map.of() : productMapper.selectByIds(productIds).stream()
                .collect(Collectors.toMap(product -> String.valueOf(product.getId()), ProductEntity::getCourseContent));
        return studentProducts.stream()
                .collect(Collectors.groupingBy(
                        StudentProductEntity::getStudentId,
                        Collectors.mapping(item -> productMap.get(item.getProductId()),
                                Collectors.filtering(Objects::nonNull, Collectors.joining("、")))));
    }

    private Map<Long, String> buildClassPeriodMap(List<StudentEntity> students) {
        List<Long> classIds = students.stream()
                .map(StudentEntity::getClassId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (classIds.isEmpty()) {
            return Map.of();
        }
        return clazzMapper.selectByIds(classIds).stream()
                .collect(Collectors.toMap(ClazzEntity::getId, ClazzEntity::getClassPeriod));
    }

    private StudentPageRes buildStudentPageRes(StudentEntity student, Map<Long, String> productContentMap, Map<Long, String> classPeriodMap) {
        StudentPageRes res = new StudentPageRes();
        BeanUtils.copyProperties(student, res);
        res.setProductContent(productContentMap.get(student.getId()));
        res.setClassPeriod(student.getClassId() == null ? null : classPeriodMap.get(student.getClassId()));
        return res;
    }

    private String createStudentNo() {
        return "STU" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN) + RandomUtil.randomNumbers(6);
    }
}
