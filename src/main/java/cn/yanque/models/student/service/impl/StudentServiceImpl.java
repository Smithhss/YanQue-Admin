package cn.yanque.models.student.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.yanque.common.api.PageResult;
import cn.yanque.common.dataConfig.service.SysConfig;
import cn.yanque.common.dataConfig.service.SysConfigService;
import cn.yanque.common.enums.ActiveEnum;
import cn.yanque.common.enums.SopStatusEnum;
import cn.yanque.common.enums.TeachingModeEnum;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.order.product.mapper.ProductMapper;
import cn.yanque.models.order.product.pojo.entity.ProductEntity;
import cn.yanque.models.student.mapper.StudentMapper;
import cn.yanque.models.student.mapper.StudentProductMapper;
import cn.yanque.models.student.mapper.StudentSopMapper;
import cn.yanque.models.student.pojo.bo.QueryStudentBo;
import cn.yanque.models.student.pojo.entity.StudentEntity;
import cn.yanque.models.student.pojo.entity.StudentProductEntity;
import cn.yanque.models.student.pojo.entity.StudentSopEntity;
import cn.yanque.models.student.pojo.vo.req.StudentAssignClassReq;
import cn.yanque.models.student.pojo.vo.req.StudentPageReq;
import cn.yanque.models.student.pojo.vo.req.StudentSopAssignReq;
import cn.yanque.models.student.pojo.vo.req.StudentTagUpdateReq;
import cn.yanque.models.student.pojo.vo.res.StudentAssignClassRes;
import cn.yanque.models.student.pojo.vo.res.StudentPageRes;
import cn.yanque.models.student.pojo.vo.res.StudentSopAssignRes;
import cn.yanque.models.student.pojo.vo.res.StudentTagUpdateRes;
import cn.yanque.models.student.service.StudentService;
import cn.yanque.models.teaching.clazz.mapper.ClazzMapper;
import cn.yanque.models.teaching.clazz.pojo.entity.ClazzEntity;
import cn.yanque.models.users.mapper.SysUserMapper;
import cn.yanque.models.users.pojo.entity.SysUserEntity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private static final String SOP_STATUS_ASSIGNED = SopStatusEnum.ASSIGNED.name();
    private static final String ROLE_CODE_ADVISOR = "ADVISOR";

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private StudentProductMapper studentProductMapper;

    @Autowired
    private StudentSopMapper studentSopMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ClazzMapper clazzMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysConfigService sysConfigService;

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
            throw BusinessException.DateExist.newInstance("学生资料已存在,请勿重复提交");
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
        Map<Long, StudentSopEntity> sopMap = buildStudentSopMap(list);
        Map<Long, String> mentorNameMap = buildMentorNameMap(sopMap);
        List<StudentPageRes> records = list.stream()
                .map(student -> buildStudentPageRes(student, productContentMap, classPeriodMap, sopMap, mentorNameMap))
                .toList();
        return new PageResult<>(pageInfo.getTotal(), pageNum, pageSize, records);
    }

    @Override
    public List<String> listStudentTagOptions() {
        return parseStudentTagOptions();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentAssignClassRes assignClass(Long id, StudentAssignClassReq req) {
        StudentEntity student = studentMapper.selectById(id);
        if (student == null) {
            throw BusinessException.DateError.newInstance("学生不存在");
        }
        if (!TeachingModeEnum.OFFLINE.name().equals(student.getTeachingMode())) {
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentSopAssignRes assignSop(Long id, StudentSopAssignReq req) {
        StudentEntity student = studentMapper.selectById(id);
        if (student == null) {
            throw BusinessException.DateError.newInstance("学生不存在");
        }
        if (!TeachingModeEnum.ONLINE.name().equals(student.getTeachingMode())) {
            throw BusinessException.DateError.newInstance("只有线上学生需要分配入学SOP");
        }
        if (!isActiveAdvisor(req.getMentorId())) {
            throw BusinessException.UserNotExist.newInstance("导师不存在");
        }
        if (studentSopMapper.selectActiveByStudentId(id) != null) {
            throw BusinessException.DateError.newInstance("该学生已分配入学SOP");
        }

        Date now = new Date();
        StudentSopEntity studentSop = new StudentSopEntity();
        studentSop.setStudentId(id);
        studentSop.setMentorId(req.getMentorId());
        studentSop.setStatus(SOP_STATUS_ASSIGNED);
        studentSop.setCreatedAt(now);
        studentSop.setUpdatedAt(now);
        studentSopMapper.insert(studentSop);

        StudentSopAssignRes res = new StudentSopAssignRes();
        res.setId(studentSop.getId());
        res.setStudentId(id);
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentTagUpdateRes updateStudentTag(Long id, StudentTagUpdateReq req) {
        if (studentMapper.selectById(id) == null) {
            throw BusinessException.DateError.newInstance("学生不存在");
        }

        String studentTag = normalizeStudentTag(req.getStudentTag());
        if (studentTag != null && !parseStudentTagOptions().contains(studentTag)) {
            throw BusinessException.DateError.newInstance("学生标签不在配置范围内");
        }

        studentMapper.updateStudentTag(id, studentTag);
        StudentTagUpdateRes res = new StudentTagUpdateRes();
        res.setStudentId(id);
        res.setStudentTag(studentTag);
        return res;
    }

    @Override
    public StudentEntity selectByStudentId(Long id) {
        return studentMapper.selectById(id);
    }

    @Override
    public void updateProfile(Long id, String education, Integer gradeYear, String school, String major) {
        if (studentMapper.selectById(id) == null) {
            throw BusinessException.DateError.newInstance("学生不存在");
        }
        studentMapper.updateProfile(id, education, gradeYear, school, major);
    }

    private void fillAndValidateTeachingMode(StudentEntity student) {
        String teachingMode = student.getTeachingMode();
        if (teachingMode == null || teachingMode.isBlank()) {
            teachingMode = TeachingModeEnum.ONLINE.name();
        }
        if (!TeachingModeEnum.ONLINE.name().equals(teachingMode) && !TeachingModeEnum.OFFLINE.name().equals(teachingMode)) {
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

        //Map<String, String> productMap key 产品id, value 产品内容
        Map<String, String> productMap = productIds.isEmpty() ? Map.of() : productMapper.selectByIds(productIds).stream()
                .collect(Collectors.toMap(product -> String.valueOf(product.getId()), ProductEntity::getCourseContent));
        return studentProducts.stream()
                .collect(Collectors.groupingBy(
                        StudentProductEntity::getStudentId,
                        Collectors.mapping(item -> productMap.get(item.getProductId()),
                                Collectors.filtering(Objects::nonNull, Collectors.joining(",")))));
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

    private StudentPageRes buildStudentPageRes(StudentEntity student,
                                               Map<Long, String> productContentMap,
                                               Map<Long, String> classPeriodMap,
                                               Map<Long, StudentSopEntity> sopMap,
                                               Map<Long, String> mentorNameMap) {
        StudentPageRes res = new StudentPageRes();
        BeanUtils.copyProperties(student, res);
        res.setProductContent(productContentMap.get(student.getId()));
        res.setClassPeriod(student.getClassId() == null ? null : classPeriodMap.get(student.getClassId()));
        StudentSopEntity studentSop = sopMap.get(student.getId());
        res.setSopAssigned(studentSop != null);
        if (studentSop != null) {
            res.setSopId(studentSop.getId());
            res.setSopMentorId(studentSop.getMentorId());
            res.setSopMentorName(mentorNameMap.get(studentSop.getMentorId()));
            res.setSopVideoObjectKey(studentSop.getSopVideoObjectKey());
            res.setSopVideoFileName(studentSop.getSopVideoFileName());
            res.setSopTime(studentSop.getSopTime());
        }
        return res;
    }

    private Map<Long, StudentSopEntity> buildStudentSopMap(List<StudentEntity> students) {
        List<Long> studentIds = students.stream().map(StudentEntity::getId).toList();
        if (studentIds.isEmpty()) {
            return Map.of();
        }
        return studentSopMapper.selectActiveByStudentIds(studentIds).stream()
                .collect(Collectors.toMap(StudentSopEntity::getStudentId, item -> item, (oldItem, newItem) -> oldItem));
    }

    private Map<Long, String> buildMentorNameMap(Map<Long, StudentSopEntity> sopMap) {
        List<Long> mentorIds = sopMap.values().stream()
                .map(StudentSopEntity::getMentorId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (mentorIds.isEmpty()) {
            return Map.of();
        }
        return sysUserMapper.selectByIds(mentorIds).stream()
                .collect(Collectors.toMap(SysUserEntity::getId, this::getUserShowName));
    }

    private String getUserShowName(SysUserEntity user) {
        if (user == null) {
            return null;
        }
        if (user.getRealName() != null && !user.getRealName().isBlank()) {
            return user.getRealName();
        }
        if (user.getNickname() != null && !user.getNickname().isBlank()) {
            return user.getNickname();
        }
        return user.getUsername();
    }

    private boolean isActiveAdvisor(Long mentorId) {
        if (mentorId == null) {
            return false;
        }
        return sysUserMapper.selectPage(null, ActiveEnum.ACTIVE.name(), ROLE_CODE_ADVISOR).stream()
                .anyMatch(user -> mentorId.equals(user.getId()));
    }

    private List<String> parseStudentTagOptions() {
        String value = sysConfigService.get(SysConfig.studentTagOptions);
        if (value == null || value.isBlank()) {
            return List.of();
        }

        // 支持逗号,顿号,分号,换行分隔,便于在系统配置里维护。
        Set<String> options = Arrays.stream(value.split("[,,,;;\\n\\r]+"))
                .map(String::trim)
                .filter(item -> !item.isBlank())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return List.copyOf(options);
    }

    private String normalizeStudentTag(String studentTag) {
        if (studentTag == null || studentTag.isBlank()) {
            return null;
        }
        return studentTag.trim();
    }

    private String createStudentNo() {
        return "STU" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN) + RandomUtil.randomNumbers(6);
    }
}
