package cn.yanque.models.student.service.impl;

import cn.yanque.common.api.PageResult;
import cn.yanque.common.enums.SopStatusEnum;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.student.mapper.StudentLearningPlanMapper;
import cn.yanque.models.student.mapper.StudentMapper;
import cn.yanque.models.student.mapper.StudentSopMapper;
import cn.yanque.models.student.pojo.bo.QueryStudentSopBo;
import cn.yanque.models.student.pojo.entity.StudentEntity;
import cn.yanque.models.student.pojo.entity.StudentLearningPlanEntity;
import cn.yanque.models.student.pojo.entity.StudentSopEntity;
import cn.yanque.models.student.pojo.vo.req.StudentSopCompleteReq;
import cn.yanque.models.student.pojo.vo.req.StudentSopPageReq;
import cn.yanque.models.student.pojo.vo.res.StudentSopCompleteRes;
import cn.yanque.models.student.pojo.vo.res.StudentSopPageRes;
import cn.yanque.models.student.service.StudentSopService;
import cn.yanque.models.users.mapper.SysUserMapper;
import cn.yanque.models.users.pojo.entity.SysUserEntity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class StudentSopServiceImpl implements StudentSopService {

    private static final String SOP_STATUS_ASSIGNED = SopStatusEnum.ASSIGNED.name();
    private static final String SOP_STATUS_COMPLETED = SopStatusEnum.COMPLETED.name();

    @Autowired
    private StudentSopMapper studentSopMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private StudentLearningPlanMapper learningPlanMapper;

    @Override
    public PageResult<StudentSopPageRes> pageStudentSop(StudentSopPageReq req) {
        int pageNum = req.getPageNum() == null ? 1 : req.getPageNum();
        int pageSize = req.getPageSize() == null ? 10 : req.getPageSize();
        QueryStudentSopBo query = new QueryStudentSopBo();
        BeanUtils.copyProperties(req, query);
        PageHelper.startPage(pageNum, pageSize);
        List<StudentSopEntity> list = studentSopMapper.selectPage(query);
        PageInfo<StudentSopEntity> pageInfo = new PageInfo<>(list);

        Map<Long, StudentEntity> studentMap = buildStudentMap(list);
        Map<Long, SysUserEntity> mentorMap = buildMentorMap(list);
        Map<Long, StudentLearningPlanEntity> learningPlanMap = buildLearningPlanMap(list);
        List<StudentSopPageRes> records = list.stream()
                .map(item -> buildPageRes(item, studentMap, mentorMap, learningPlanMap))
                .toList();
        return new PageResult<>(pageInfo.getTotal(), pageNum, pageSize, records);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentSopCompleteRes completeSop(Long id, StudentSopCompleteReq req) {
        StudentSopEntity oldSop = studentSopMapper.selectById(id);
        if (oldSop == null) {
            throw BusinessException.DateError.newInstance("SOP记录不存在");
        }
        if (!SOP_STATUS_ASSIGNED.equals(oldSop.getStatus())) {
            throw BusinessException.DateError.newInstance("只有已分配的SOP可以完成");
        }

        StudentSopEntity studentSop = new StudentSopEntity();
        studentSop.setId(id);
        studentSop.setSopVideoObjectKey(req.getSopVideoObjectKey().trim());
        studentSop.setSopVideoFileName(normalizeText(req.getSopVideoFileName()));
        studentSop.setSopTime(req.getSopTime());
        studentSop.setStatus(SOP_STATUS_COMPLETED);
        studentSop.setUpdatedAt(new Date());
        int rows = studentSopMapper.completeSop(studentSop);
        if (rows == 0) {
            throw BusinessException.DateError.newInstance("只有已分配的SOP可以完成");
        }

        StudentSopCompleteRes res = new StudentSopCompleteRes();
        res.setId(id);
        return res;
    }

    private Map<Long, StudentEntity> buildStudentMap(List<StudentSopEntity> list) {
        List<Long> studentIds = list.stream()
                .map(StudentSopEntity::getStudentId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (studentIds.isEmpty()) {
            return Map.of();
        }
        return studentMapper.selectByIds(studentIds).stream()
                .collect(Collectors.toMap(StudentEntity::getId, item -> item));
    }

    private Map<Long, SysUserEntity> buildMentorMap(List<StudentSopEntity> list) {
        List<Long> mentorIds = list.stream()
                .map(StudentSopEntity::getMentorId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (mentorIds.isEmpty()) {
            return Map.of();
        }
        return sysUserMapper.selectByIds(mentorIds).stream()
                .collect(Collectors.toMap(SysUserEntity::getId, item -> item));
    }

    private Map<Long, StudentLearningPlanEntity> buildLearningPlanMap(List<StudentSopEntity> list) {
        List<Long> studentIds = list.stream()
                .map(StudentSopEntity::getStudentId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (studentIds.isEmpty()) {
            return Map.of();
        }
        return learningPlanMapper.selectActiveByStudentIds(studentIds).stream()
                .collect(Collectors.toMap(StudentLearningPlanEntity::getStudentId, item -> item, (first, second) -> first));
    }

    private StudentSopPageRes buildPageRes(StudentSopEntity studentSop,
                                           Map<Long, StudentEntity> studentMap,
                                           Map<Long, SysUserEntity> mentorMap,
                                           Map<Long, StudentLearningPlanEntity> learningPlanMap) {
        StudentSopPageRes res = new StudentSopPageRes();
        BeanUtils.copyProperties(studentSop, res);
        StudentEntity student = studentMap.get(studentSop.getStudentId());
        if (student != null) {
            res.setStudentName(student.getStudentName());
            res.setStudentPhone(student.getStudentPhone());
            res.setTeachingMode(student.getTeachingMode());
        }
        res.setLearningPlanCreated(learningPlanMap.containsKey(studentSop.getStudentId()));
        res.setMentorName(getUserShowName(mentorMap.get(studentSop.getMentorId())));
        return res;
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

    private String normalizeText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
