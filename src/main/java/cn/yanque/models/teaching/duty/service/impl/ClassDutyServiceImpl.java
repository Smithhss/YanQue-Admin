package cn.yanque.models.teaching.duty.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.yanque.common.api.PageResult;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.teaching.campus.mapper.CampusMapper;
import cn.yanque.models.teaching.campus.pojo.entity.CampusEntity;
import cn.yanque.models.teaching.clazz.mapper.ClazzMapper;
import cn.yanque.models.teaching.clazz.pojo.entity.ClazzEntity;
import cn.yanque.models.teaching.duty.enums.DutyTypeEnum;
import cn.yanque.models.teaching.duty.mapper.ClassDutyMapper;
import cn.yanque.models.teaching.duty.pojo.entity.ClassDutyEntity;
import cn.yanque.models.teaching.duty.pojo.vo.req.ClassDutyCreateReq;
import cn.yanque.models.teaching.duty.pojo.vo.req.ClassDutyDateSaveReq;
import cn.yanque.models.teaching.duty.pojo.vo.req.ClassDutyPageReq;
import cn.yanque.models.teaching.duty.pojo.vo.req.ClassDutyUpdateReq;
import cn.yanque.models.teaching.duty.pojo.vo.res.ClassDutyCampusItemRes;
import cn.yanque.models.teaching.duty.pojo.vo.res.ClassDutyClassItemRes;
import cn.yanque.models.teaching.duty.pojo.vo.res.ClassDutyCreateRes;
import cn.yanque.models.teaching.duty.pojo.vo.res.ClassDutyDateRes;
import cn.yanque.models.teaching.duty.pojo.vo.res.ClassDutyDateSaveRes;
import cn.yanque.models.teaching.duty.pojo.vo.res.ClassDutyDeleteRes;
import cn.yanque.models.teaching.duty.pojo.vo.res.ClassDutyDetailRes;
import cn.yanque.models.teaching.duty.pojo.vo.res.ClassDutyPageRes;
import cn.yanque.models.teaching.duty.pojo.vo.res.ClassDutyUpdateRes;
import cn.yanque.models.teaching.duty.service.ClassDutyService;
import cn.yanque.models.teaching.schedule.enums.ClassScheduleTypeEnum;
import cn.yanque.models.teaching.schedule.mapper.ClassScheduleMapper;
import cn.yanque.models.teaching.schedule.pojo.entity.ClassScheduleEntity;
import cn.yanque.models.users.mapper.SysUserMapper;
import cn.yanque.models.users.pojo.entity.SysUserEntity;
import cn.yanque.models.users.service.SysUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 值班管理业务实现。
 */
@Service
public class ClassDutyServiceImpl implements ClassDutyService {

    @Autowired
    private ClassDutyMapper classDutyMapper;

    @Autowired
    private ClazzMapper clazzMapper;

    @Autowired
    private CampusMapper campusMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ClassScheduleMapper classScheduleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClassDutyCreateRes addDuty(ClassDutyCreateReq req) {
        ClassDutyEntity duty = buildDutyEntity(null, req.getClassId(), req.getCampusId(), req.getTeacherId(),
                req.getDutyDate(), req.getDutyType(), req.getRemark());
        classDutyMapper.insert(duty);

        ClassDutyCreateRes res = new ClassDutyCreateRes();
        res.setId(duty.getId());
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClassDutyUpdateRes updateDuty(ClassDutyUpdateReq req) {
        if (classDutyMapper.selectById(req.getId()) == null) {
            throw BusinessException.DateError.newInstance("值班不存在");
        }

        ClassDutyEntity duty = buildDutyEntity(req.getId(), req.getClassId(), req.getCampusId(), req.getTeacherId(),
                req.getDutyDate(), req.getDutyType(), req.getRemark());
        int rows = classDutyMapper.updateById(duty);
        if (rows == 0) {
            throw BusinessException.DateError.newInstance("值班不存在");
        }

        ClassDutyUpdateRes res = new ClassDutyUpdateRes();
        res.setId(req.getId());
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClassDutyDeleteRes deleteDuty(Long id) {
        int rows = classDutyMapper.deleteById(id);
        if (rows == 0) {
            throw BusinessException.DateError.newInstance("值班不存在");
        }

        ClassDutyDeleteRes res = new ClassDutyDeleteRes();
        res.setId(id);
        return res;
    }

    @Override
    public ClassDutyDetailRes getDutyById(Long id) {
        ClassDutyEntity duty = classDutyMapper.selectById(id);
        if (duty == null) {
            throw BusinessException.DateError.newInstance("值班不存在");
        }
        ClassDutyDetailRes res = new ClassDutyDetailRes();
        BeanUtils.copyProperties(duty, res);
        fillDutyDetailNames(res);
        fillDutyTypeDesc(res);
        return res;
    }

    @Override
    public PageResult<ClassDutyPageRes> pageDuty(ClassDutyPageReq req) {
        int pageNum = req.getPageNum() == null ? 1 : req.getPageNum();
        int pageSize = req.getPageSize() == null ? 10 : req.getPageSize();
        Date startDate = req.getStartDate() == null ? null : DateUtil.beginOfDay(req.getStartDate());
        Date endDate = req.getEndDate() == null ? null : DateUtil.endOfDay(req.getEndDate());

        PageHelper.startPage(pageNum, pageSize);
        List<ClassDutyEntity> list = classDutyMapper.selectPage(req.getClassId(), req.getCampusId(), req.getTeacherId(),
                req.getDutyType(), startDate, endDate);
        PageInfo<ClassDutyEntity> pageInfo = new PageInfo<>(list);
        List<ClassDutyPageRes> records = list.stream().map(this::buildDutyPageRes).toList();
        fillDutyPageNames(records);
        return new PageResult<>(pageInfo.getTotal(), pageNum, pageSize, records);
    }

    @Override
    public ClassDutyDateRes getDateDuty(Date dutyDate) {
        Date day = DateUtil.beginOfDay(dutyDate);
        List<ClassScheduleEntity> schedules = classScheduleMapper.selectByClassIdAndDate(null, day, DateUtil.endOfDay(day))
                .stream()
                .filter(item -> ClassScheduleTypeEnum.CLASS.name().equals(item.getClassType())
                        || ClassScheduleTypeEnum.SELF_STUDY.name().equals(item.getClassType()))
                .toList();
        List<ClassDutyEntity> duties = classDutyMapper.selectByDutyDate(day);

        Map<String, ClassDutyEntity> classDutyMap = duties.stream()
                .filter(item -> item.getClassId() != null)
                .collect(Collectors.toMap(item -> buildClassDutyKey(item.getClassId(), item.getDutyType()), Function.identity(), (a, b) -> b));
        Map<Long, ClassDutyEntity> campusDutyMap = duties.stream()
                .filter(item -> DutyTypeEnum.EVENING_STUDY_CAMPUS.name().equals(item.getDutyType()))
                .collect(Collectors.toMap(ClassDutyEntity::getCampusId, Function.identity(), (a, b) -> b));

        List<Long> classIds = schedules.stream().map(ClassScheduleEntity::getClassId).distinct().toList();
        Map<Long, ClazzEntity> clazzMap = classIds.isEmpty() ? Collections.emptyMap() : clazzMapper.selectByIds(classIds).stream()
                .collect(Collectors.toMap(ClazzEntity::getId, Function.identity(), (a, b) -> a));

        Set<Long> campusIds = new LinkedHashSet<>();
        clazzMap.values().forEach(clazz -> campusIds.add(clazz.getCampusId()));
        campusIds.addAll(campusDutyMap.keySet());

        Map<Long, CampusEntity> campusMap = campusIds.isEmpty() ? Collections.emptyMap() : campusMapper.selectByIds(new ArrayList<>(campusIds)).stream()
                .collect(Collectors.toMap(CampusEntity::getId, Function.identity(), (a, b) -> a));

        Set<Long> teacherIds = duties.stream()
                .map(ClassDutyEntity::getTeacherId)
                .filter(item -> item != null)
                .collect(Collectors.toSet());
        Map<Long, SysUserEntity> teacherMap = teacherIds.isEmpty() ? Collections.emptyMap() : sysUserMapper.selectByIds(new ArrayList<>(teacherIds)).stream()
                .collect(Collectors.toMap(SysUserEntity::getId, Function.identity(), (a, b) -> a));

        List<ClassDutyClassItemRes> classDutyList = schedules.stream()
                .map(schedule -> buildDateClassItem(schedule, clazzMap, campusMap, classDutyMap, teacherMap))
                .toList();
        List<ClassDutyCampusItemRes> campusDutyList = campusIds.stream()
                .map(campusId -> buildDateCampusItem(campusId, campusMap, campusDutyMap, teacherMap))
                .toList();

        ClassDutyDateRes res = new ClassDutyDateRes();
        res.setDutyDate(day);
        res.setClassDutyList(classDutyList);
        res.setCampusDutyList(campusDutyList);
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClassDutyDateSaveRes saveDateDuty(ClassDutyDateSaveReq req) {
        Date day = DateUtil.beginOfDay(req.getDutyDate());
        classDutyMapper.deleteByDutyDate(day);

        int saveCount = 0;
        List<ClassDutyDateSaveReq.ClassDutyItem> classDutyList = req.getClassDutyList() == null ? Collections.emptyList() : req.getClassDutyList();
        for (ClassDutyDateSaveReq.ClassDutyItem item : classDutyList) {
            ClassDutyEntity duty = buildDutyEntity(null, item.getClassId(), null, item.getTeacherId(), day, item.getDutyType(), null);
            classDutyMapper.insert(duty);
            saveCount++;
        }

        List<ClassDutyDateSaveReq.CampusDutyItem> campusDutyList = req.getCampusDutyList() == null ? Collections.emptyList() : req.getCampusDutyList();
        for (ClassDutyDateSaveReq.CampusDutyItem item : campusDutyList) {
            if (!DutyTypeEnum.EVENING_STUDY_CAMPUS.name().equals(item.getDutyType())) {
                throw BusinessException.DateError.newInstance("校区统一值班类型错误");
            }
            ClassDutyEntity duty = buildDutyEntity(null, null, item.getCampusId(), item.getTeacherId(), day, item.getDutyType(), null);
            classDutyMapper.insert(duty);
            saveCount++;
        }

        ClassDutyDateSaveRes res = new ClassDutyDateSaveRes();
        res.setSaveCount(saveCount);
        return res;
    }

    private ClassDutyEntity buildDutyEntity(Long id, Long classId, Long campusId, Long teacherId,
                                            Date dutyDate, String dutyTypeValue, String remark) {
        DutyTypeEnum dutyType = parseDutyType(dutyTypeValue);
        Date day = DateUtil.beginOfDay(dutyDate);

        validateTeacher(teacherId);
        if (dutyType.isClassRequired()) {
            if (classId == null) {
                throw BusinessException.DateError.newInstance("班级不能为空");
            }
            ClazzEntity clazz = clazzMapper.selectById(classId);
            if (clazz == null) {
                throw BusinessException.ClazzNotExist;
            }
            CampusEntity campus = campusMapper.selectById(clazz.getCampusId());
            if (campus == null) {
                throw BusinessException.CampusNotExist;
            }
            campusId = clazz.getCampusId();
        } else {
            if (campusId == null) {
                throw BusinessException.DateError.newInstance("校区不能为空");
            }
            CampusEntity campus = campusMapper.selectById(campusId);
            if (campus == null) {
                throw BusinessException.CampusNotExist;
            }
            classId = null;
        }

        validateDutyRule(id, classId, campusId, teacherId, day, dutyType);

        ClassDutyEntity duty = new ClassDutyEntity();
        duty.setId(id);
        duty.setClassId(classId);
        duty.setCampusId(campusId);
        duty.setTeacherId(teacherId);
        duty.setDutyDate(day);
        duty.setDutyType(dutyType.name());
        duty.setStartTime(dutyType.getStartTime());
        duty.setEndTime(dutyType.getEndTime());
        duty.setRemark(remark);
        return duty;
    }

    private DutyTypeEnum parseDutyType(String dutyType) {
        try {
            return DutyTypeEnum.parse(dutyType);
        } catch (IllegalArgumentException e) {
            throw BusinessException.DateError.newInstance(e.getMessage());
        }
    }

    private void validateTeacher(Long teacherId) {
        Set<Long> teacherIds = sysUserService.getUserByRoleCode("TEACHER").stream()
                .map(SysUserEntity::getId)
                .collect(Collectors.toSet());
        if (!teacherIds.contains(teacherId)) {
            throw BusinessException.DateError.newInstance("请选择老师角色的用户");
        }
    }

    private void validateDutyRule(Long id, Long classId, Long campusId, Long teacherId, Date dutyDate, DutyTypeEnum dutyType) {
        if (dutyType.isClassRequired()) {
            int count = classDutyMapper.countClassDuty(id, classId, dutyDate, dutyType.name());
            if (count > 0) {
                throw BusinessException.DateError.newInstance("该班级当天已存在同类型值班");
            }
        }
        if (dutyType.isCampusRequired()) {
            int count = classDutyMapper.countCampusDuty(id, campusId, dutyDate, dutyType.name());
            if (count > 0) {
                throw BusinessException.DateError.newInstance("该校区当天已存在统一值班");
            }
        }
        int conflictCount = classDutyMapper.countTeacherTimeConflict(id, teacherId, dutyDate, dutyType.getStartTime(), dutyType.getEndTime());
        if (conflictCount > 0) {
            throw BusinessException.DateError.newInstance("该老师在当前时间段已有值班");
        }
        if (dutyType == DutyTypeEnum.SELF_STUDY_CLASS) {
            List<ClassScheduleEntity> schedules = classScheduleMapper.selectByClassIdAndDate(classId,
                    DateUtil.beginOfDay(dutyDate), DateUtil.endOfDay(dutyDate));
            boolean selfStudy = schedules.stream()
                    .anyMatch(item -> ClassScheduleTypeEnum.SELF_STUDY.name().equals(item.getClassType()));
            if (!selfStudy) {
                throw BusinessException.DateError.newInstance("该班当天不是自习日");
            }
        }
    }

    private ClassDutyPageRes buildDutyPageRes(ClassDutyEntity duty) {
        ClassDutyPageRes res = new ClassDutyPageRes();
        BeanUtils.copyProperties(duty, res);
        fillDutyTypeDesc(res);
        return res;
    }

    private void fillDutyPageNames(List<ClassDutyPageRes> records) {
        if (records.isEmpty()) {
            return;
        }

        List<Long> classIds = records.stream()
                .map(ClassDutyPageRes::getClassId)
                .filter(item -> item != null)
                .distinct()
                .toList();
        List<Long> campusIds = records.stream().map(ClassDutyPageRes::getCampusId).distinct().toList();
        List<Long> teacherIds = records.stream().map(ClassDutyPageRes::getTeacherId).distinct().toList();

        Map<Long, ClazzEntity> clazzMap = classIds.isEmpty() ? Collections.emptyMap() : clazzMapper.selectByIds(classIds).stream()
                .collect(Collectors.toMap(ClazzEntity::getId, Function.identity(), (a, b) -> a));
        Map<Long, CampusEntity> campusMap = campusMapper.selectByIds(campusIds).stream()
                .collect(Collectors.toMap(CampusEntity::getId, Function.identity(), (a, b) -> a));
        Map<Long, SysUserEntity> userMap = sysUserMapper.selectByIds(teacherIds).stream()
                .collect(Collectors.toMap(SysUserEntity::getId, Function.identity(), (a, b) -> a));

        records.forEach(record -> {
            ClazzEntity clazz = clazzMap.get(record.getClassId());
            if (clazz != null) {
                record.setClassPeriod(clazz.getClassPeriod());
            }
            CampusEntity campus = campusMap.get(record.getCampusId());
            if (campus != null) {
                record.setCampusName(campus.getCampusLocation());
            }
            SysUserEntity user = userMap.get(record.getTeacherId());
            if (user != null) {
                record.setTeacherName(user.getNickname() != null ? user.getNickname() : user.getUsername());
            }
        });
    }

    private void fillDutyDetailNames(ClassDutyDetailRes res) {
        if (res.getClassId() != null) {
            ClazzEntity clazz = clazzMapper.selectByIds(Collections.singletonList(res.getClassId())).stream().findFirst().orElse(null);
            if (clazz != null) {
                res.setClassPeriod(clazz.getClassPeriod());
            }
        }
        CampusEntity campus = campusMapper.selectByIds(Collections.singletonList(res.getCampusId())).stream().findFirst().orElse(null);
        if (campus != null) {
            res.setCampusName(campus.getCampusLocation());
        }
        SysUserEntity user = sysUserMapper.selectByIds(Collections.singletonList(res.getTeacherId())).stream().findFirst().orElse(null);
        if (user != null) {
            res.setTeacherName(user.getNickname() != null ? user.getNickname() : user.getUsername());
        }
    }

    private void fillDutyTypeDesc(ClassDutyPageRes res) {
        DutyTypeEnum dutyType = parseDutyType(res.getDutyType());
        res.setDutyTypeDesc(dutyType.getDesc());
    }

    private void fillDutyTypeDesc(ClassDutyDetailRes res) {
        DutyTypeEnum dutyType = parseDutyType(res.getDutyType());
        res.setDutyTypeDesc(dutyType.getDesc());
    }

    private ClassDutyClassItemRes buildDateClassItem(ClassScheduleEntity schedule,
                                                     Map<Long, ClazzEntity> clazzMap,
                                                     Map<Long, CampusEntity> campusMap,
                                                     Map<String, ClassDutyEntity> classDutyMap,
                                                     Map<Long, SysUserEntity> teacherMap) {
        ClazzEntity clazz = clazzMap.get(schedule.getClassId());
        DutyTypeEnum dutyType = ClassScheduleTypeEnum.SELF_STUDY.name().equals(schedule.getClassType())
                ? DutyTypeEnum.SELF_STUDY_CLASS
                : DutyTypeEnum.EVENING_STUDY_CLASS;
        ClassDutyEntity duty = classDutyMap.get(buildClassDutyKey(schedule.getClassId(), dutyType.name()));

        ClassDutyClassItemRes res = new ClassDutyClassItemRes();
        res.setScheduleId(schedule.getId());
        res.setClassId(schedule.getClassId());
        res.setClassType(schedule.getClassType());
        res.setClassTypeDesc(getClassTypeDesc(schedule.getClassType()));
        res.setCourseContent(schedule.getCourseContent());
        res.setDutyType(dutyType.name());
        res.setDutyTypeDesc(dutyType.getDesc());
        res.setStartTime(dutyType.getStartTime());
        res.setEndTime(dutyType.getEndTime());
        if (clazz != null) {
            res.setClassPeriod(clazz.getClassPeriod());
            res.setCampusId(clazz.getCampusId());
            CampusEntity campus = campusMap.get(clazz.getCampusId());
            if (campus != null) {
                res.setCampusName(campus.getCampusLocation());
            }
        }
        if (duty != null) {
            res.setTeacherId(duty.getTeacherId());
            res.setTeacherName(getUserShowName(teacherMap.get(duty.getTeacherId())));
        }
        return res;
    }

    private ClassDutyCampusItemRes buildDateCampusItem(Long campusId,
                                                       Map<Long, CampusEntity> campusMap,
                                                       Map<Long, ClassDutyEntity> campusDutyMap,
                                                       Map<Long, SysUserEntity> teacherMap) {
        DutyTypeEnum dutyType = DutyTypeEnum.EVENING_STUDY_CAMPUS;
        ClassDutyEntity duty = campusDutyMap.get(campusId);
        CampusEntity campus = campusMap.get(campusId);

        ClassDutyCampusItemRes res = new ClassDutyCampusItemRes();
        res.setCampusId(campusId);
        res.setCampusName(campus == null ? null : campus.getCampusLocation());
        res.setDutyType(dutyType.name());
        res.setDutyTypeDesc(dutyType.getDesc());
        res.setStartTime(dutyType.getStartTime());
        res.setEndTime(dutyType.getEndTime());
        if (duty != null) {
            res.setTeacherId(duty.getTeacherId());
            res.setTeacherName(getUserShowName(teacherMap.get(duty.getTeacherId())));
        }
        return res;
    }

    private String buildClassDutyKey(Long classId, String dutyType) {
        return classId + ":" + dutyType;
    }

    private String getClassTypeDesc(String classType) {
        for (ClassScheduleTypeEnum item : ClassScheduleTypeEnum.values()) {
            if (item.name().equals(classType)) {
                return item.getDesc();
            }
        }
        return classType;
    }

    private String getUserShowName(SysUserEntity user) {
        if (user == null) {
            return null;
        }
        if (user.getRealName() != null && !user.getRealName().isEmpty()) {
            return user.getRealName();
        }
        return user.getNickname() != null ? user.getNickname() : user.getUsername();
    }
}
