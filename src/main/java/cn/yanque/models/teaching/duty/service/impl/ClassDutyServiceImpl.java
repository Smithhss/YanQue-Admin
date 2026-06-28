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
 *
 * 三种值班类型:
 *   EVENING_STUDY_CLASS  — 晚自习值班(每班一个老师,19:00-21:00)
 *   EVENING_STUDY_CAMPUS — 晚自习统一值班(每校区一个老师,21:00-22:30)
 *   SELF_STUDY_CLASS     — 自习日值班(每班一个老师,09:00-18:00)
 *
 * 核心设计:
 *   - 所有新增/修改都经过 buildDutyEntity() 构建+校验流水线
 *   - 按日期保存采用"先删后插"策略,事务保证一致性
 *   - 按日期查询聚合 5 个数据源(课表+值班+班级+校区+老师),Map 内存关联
 */
@Service
public class ClassDutyServiceImpl implements ClassDutyService {

    private static final String ROLE_CODE_TEACHER = "TEACHER";

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

    /**
     * 新增单个值班。
     * 构建+校验交给 buildDutyEntity(),这里只负责插入。
     */
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

    /**
     * 修改单个值班。
     * 先查存在性,再 buildDutyEntity() 构建+校验,最后更新。
     */
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

    /**
     * 删除单个值班。
     */
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

    /**
     * 查询单个值班详情。
     * 填充班级班期,校区名称,老师名称,值班类型描述。
     */
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

    /**
     * 分页查询值班列表。
     * 支持按班级,校区,老师,值班类型,日期范围筛选。
     */
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

    /**
     * 按日期查询排班页面数据(值班模块最复杂的查询)。
     *
     * 聚合 5 个数据源:
     *   1. 课表(ClassSchedule)→ 确定当天哪些班有课/自习
     *   2. 值班记录(ClassDuty)→ 已排班的老师
     *   3. 班级信息(Clazz)→ 班期,所属校区
     *   4. 校区信息(Campus)→ 校区名称
     *   5. 老师信息(SysUser)→ 老师姓名
     *
     * 返回两类列表:
     *   - classDutyList:班级级值班(晚自习/自习日,按课表遍历)
     *   - campusDutyList:校区级值班(晚自习统一值班,按校区遍历)
     */
    @Override
    public ClassDutyDateRes getDateDuty(Date dutyDate) {
        Date day = DateUtil.beginOfDay(dutyDate);

        // 1. 查询当天所有课表,只保留上课日和自习日(排除休息日/节假日)
        List<ClassScheduleEntity> schedules = classScheduleMapper.selectByClassIdAndDate(null, day, DateUtil.endOfDay(day))
                .stream()
                .filter(item -> ClassScheduleTypeEnum.CLASS.name().equals(item.getClassType())
                        || ClassScheduleTypeEnum.SELF_STUDY.name().equals(item.getClassType()))
                .toList();

        // 2. 查询当天已有值班记录
        List<ClassDutyEntity> duties = classDutyMapper.selectByDutyDate(day);

        // 3. 构建班级值班 Map(key = classId:dutyType),用于快速查找已有值班
        Map<String, ClassDutyEntity> classDutyMap = duties.stream()
                .filter(item -> item.getClassId() != null)
                .collect(Collectors.toMap(item -> buildClassDutyKey(item.getClassId(), item.getDutyType()), Function.identity(), (a, b) -> b));

        // 4. 构建校区值班 Map(key = campusId),用于快速查找校区统一值班
        Map<Long, ClassDutyEntity> campusDutyMap = duties.stream()
                .filter(item -> DutyTypeEnum.EVENING_STUDY_CAMPUS.name().equals(item.getDutyType()))
                .collect(Collectors.toMap(ClassDutyEntity::getCampusId, Function.identity(), (a, b) -> b));

        // 5. 批量查询班级信息(从课表提取 classId,避免 N+1)
        List<Long> classIds = schedules.stream().map(ClassScheduleEntity::getClassId).distinct().toList();
        Map<Long, ClazzEntity> clazzMap = classIds.isEmpty() ? Collections.emptyMap() : clazzMapper.selectByIds(classIds).stream()
                .collect(Collectors.toMap(ClazzEntity::getId, Function.identity(), (a, b) -> a));

        // 6. 收集所有校区ID(班级所属校区 + 已有校区值班的校区)
        Set<Long> campusIds = new LinkedHashSet<>();
        clazzMap.values().forEach(clazz -> campusIds.add(clazz.getCampusId()));
        campusIds.addAll(campusDutyMap.keySet());

        // 7. 批量查询校区信息
        Map<Long, CampusEntity> campusMap = campusIds.isEmpty() ? Collections.emptyMap() : campusMapper.selectByIds(new ArrayList<>(campusIds)).stream()
                .collect(Collectors.toMap(CampusEntity::getId, Function.identity(), (a, b) -> a));

        // 8. 批量查询老师信息(从值班记录提取 teacherId)
        Set<Long> teacherIds = duties.stream()
                .map(ClassDutyEntity::getTeacherId)
                .filter(item -> item != null)
                .collect(Collectors.toSet());
        Map<Long, SysUserEntity> teacherMap = teacherIds.isEmpty() ? Collections.emptyMap() : sysUserMapper.selectByIds(new ArrayList<>(teacherIds)).stream()
                .collect(Collectors.toMap(SysUserEntity::getId, Function.identity(), (a, b) -> a));

        // 9. 组装班级值班列表(遍历课表,每条课表对应一个值班项)
        List<ClassDutyClassItemRes> classDutyList = schedules.stream()
                .map(schedule -> buildDateClassItem(schedule, clazzMap, campusMap, classDutyMap, teacherMap))
                .toList();

        // 10. 组装校区值班列表(遍历所有校区,每个校区一个值班项)
        List<ClassDutyCampusItemRes> campusDutyList = campusIds.stream()
                .map(campusId -> buildDateCampusItem(campusId, campusMap, campusDutyMap, teacherMap))
                .toList();

        ClassDutyDateRes res = new ClassDutyDateRes();
        res.setDutyDate(day);
        res.setClassDutyList(classDutyList);
        res.setCampusDutyList(campusDutyList);
        return res;
    }

    /**
     * 按日期覆盖保存值班(先删后插)。
     *
     * 策略:删除当天所有值班 → 重新插入前端传入的完整列表。
     * 事务保证"删+插"的原子性。
     *
     * 为什么用先删后插而非逐条对比？
     * - 前端传的是当天完整列表,不是增量
     * - 先删后插逻辑简单,不需要处理"删除/新增/修改"三种情况
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClassDutyDateSaveRes saveDateDuty(ClassDutyDateSaveReq req) {
        Date day = DateUtil.beginOfDay(req.getDutyDate());

        // 1. 删除当天所有值班
        classDutyMapper.deleteByDutyDate(day);

        // 2. 重新插入班级值班(每条都经过 buildDutyEntity 校验)
        int saveCount = 0;
        List<ClassDutyDateSaveReq.ClassDutyItem> classDutyList = req.getClassDutyList() == null ? Collections.emptyList() : req.getClassDutyList();
        for (ClassDutyDateSaveReq.ClassDutyItem item : classDutyList) {
            ClassDutyEntity duty = buildDutyEntity(null, item.getClassId(), null, item.getTeacherId(), day, item.getDutyType(), null);
            classDutyMapper.insert(duty);
            saveCount++;
        }

        // 3. 重新插入校区值班(类型必须是 EVENING_STUDY_CAMPUS)
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

    /**
     * 构建+校验流水线(所有新增/修改值班的统一入口)。
     *
     * 流程:
     *   1. parseDutyType — 解析值班类型枚举
     *   2. validateTeacher — 校验老师是否为 TEACHER 角色
     *   3. 校验班级/校区(根据 dutyType 互斥)
     *   4. validateDutyRule — 业务规则校验(重复+冲突+自习日)
     *   5. 组装 ClassDutyEntity(startTime/endTime 从枚举获取,不由前端传入)
     */
    private ClassDutyEntity buildDutyEntity(Long id, Long classId, Long campusId, Long teacherId,
                                            Date dutyDate, String dutyTypeValue, String remark) {
        // 1. 解析值班类型
        DutyTypeEnum dutyType = parseDutyType(dutyTypeValue);
        Date day = DateUtil.beginOfDay(dutyDate);

        // 2. 校验老师角色(必须是 TEACHER)
        validateTeacher(teacherId);

        // 3. 根据值班类型校验班级/校区(互斥:班级值班有 classId,校区值班有 campusId)
        if (dutyType.isClassRequired()) {
            // 班级值班:classId 必填,campusId 从班级自动获取
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
            campusId = clazz.getCampusId(); // 自动填充
        } else {
            // 校区值班:campusId 必填,classId 设为 null
            if (campusId == null) {
                throw BusinessException.DateError.newInstance("校区不能为空");
            }
            CampusEntity campus = campusMapper.selectById(campusId);
            if (campus == null) {
                throw BusinessException.CampusNotExist;
            }
            classId = null; // 强制清空,防止脏数据
        }

        // 4. 业务规则校验(重复 + 老师冲突 + 自习日)
        validateDutyRule(id, classId, campusId, teacherId, day, dutyType);

        // 5. 组装实体(startTime/endTime 从枚举获取,保证数据一致性)
        ClassDutyEntity duty = new ClassDutyEntity();
        duty.setId(id);
        duty.setClassId(classId);
        duty.setCampusId(campusId);
        duty.setTeacherId(teacherId);
        duty.setDutyDate(day);
        duty.setDutyType(dutyType.name());
        duty.setStartTime(dutyType.getStartTime()); // 从枚举获取,不由前端传入
        duty.setEndTime(dutyType.getEndTime());
        duty.setRemark(remark);
        return duty;
    }

    /**
     * 解析值班类型字符串为枚举。
     */
    private DutyTypeEnum parseDutyType(String dutyType) {
        try {
            return DutyTypeEnum.parse(dutyType);
        } catch (IllegalArgumentException e) {
            throw BusinessException.DateError.newInstance(e.getMessage());
        }
    }

    /**
     * 校验老师是否具有 TEACHER 角色。
     * 查询所有 TEACHER 角色的用户ID,检查传入的 teacherId 是否在其中。
     */
    private void validateTeacher(Long teacherId) {
        Set<Long> teacherIds = sysUserService.getUserByRoleCode(ROLE_CODE_TEACHER).stream()
                .map(SysUserEntity::getId)
                .collect(Collectors.toSet());
        if (!teacherIds.contains(teacherId)) {
            throw BusinessException.DateError.newInstance("请选择老师角色的用户");
        }
    }

    /**
     * 业务规则校验(4 项检查)。
     *
     * 1. 班级值班重复检查:同班同天同类型不能重复
     * 2. 校区值班重复检查:同校区同天不能重复
     * 3. 老师时间段冲突:同一老师同一时间段不能有多个值班(时间重叠检测)
     * 4. 自习日校验:自习日值班只能排在课表标记为 SELF_STUDY 的天
     */
    private void validateDutyRule(Long id, Long classId, Long campusId, Long teacherId, Date dutyDate, DutyTypeEnum dutyType) {
        // 检查1:班级值班重复
        if (dutyType.isClassRequired()) {
            int count = classDutyMapper.countClassDuty(id, classId, dutyDate, dutyType.name());
            if (count > 0) {
                throw BusinessException.DateError.newInstance("该班级当天已存在同类型值班");
            }
        }
        // 检查2:校区统一值班重复
        if (dutyType.isCampusRequired()) {
            int count = classDutyMapper.countCampusDuty(id, campusId, dutyDate, dutyType.name());
            if (count > 0) {
                throw BusinessException.DateError.newInstance("该校区当天已存在统一值班");
            }
        }
        // 检查3:老师时间段冲突(时间重叠:start < endTime AND end > startTime)
        int conflictCount = classDutyMapper.countTeacherTimeConflict(id, teacherId, dutyDate, dutyType.getStartTime(), dutyType.getEndTime());
        if (conflictCount > 0) {
            throw BusinessException.DateError.newInstance("该老师在当前时间段已有值班");
        }
        // 检查4:自习日值班必须在课表标记为 SELF_STUDY 的天
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

    /**
     * 构建分页查询结果(填充值班类型描述)。
     */
    private ClassDutyPageRes buildDutyPageRes(ClassDutyEntity duty) {
        ClassDutyPageRes res = new ClassDutyPageRes();
        BeanUtils.copyProperties(duty, res);
        fillDutyTypeDesc(res);
        return res;
    }

    /**
     * 批量填充值班分页列表的名称字段(班级班期,校区名称,老师名称)。
     *
     * 设计:先批量查询所有关联实体(避免 N+1),再遍历填充。
     * 老师名称显示:nickname > username。
     */
    private void fillDutyPageNames(List<ClassDutyPageRes> records) {
        if (records.isEmpty()) {
            return;
        }

        // 收集所有关联ID(去重)
        List<Long> classIds = records.stream()
                .map(ClassDutyPageRes::getClassId)
                .filter(item -> item != null)
                .distinct()
                .toList();
        List<Long> campusIds = records.stream().map(ClassDutyPageRes::getCampusId).distinct().toList();
        List<Long> teacherIds = records.stream().map(ClassDutyPageRes::getTeacherId).distinct().toList();

        // 批量查询关联实体,构建 Map
        Map<Long, ClazzEntity> clazzMap = classIds.isEmpty() ? Collections.emptyMap() : clazzMapper.selectByIds(classIds).stream()
                .collect(Collectors.toMap(ClazzEntity::getId, Function.identity(), (a, b) -> a));
        Map<Long, CampusEntity> campusMap = campusMapper.selectByIds(campusIds).stream()
                .collect(Collectors.toMap(CampusEntity::getId, Function.identity(), (a, b) -> a));
        Map<Long, SysUserEntity> userMap = sysUserMapper.selectByIds(teacherIds).stream()
                .collect(Collectors.toMap(SysUserEntity::getId, Function.identity(), (a, b) -> a));

        // 遍历填充名称
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

    /**
     * 填充值班详情的名称字段(单条记录,用 selectByIds(singletonList) 保持统一风格)。
     */
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

    /**
     * 填充分页结果的值班类型描述。
     */
    private void fillDutyTypeDesc(ClassDutyPageRes res) {
        DutyTypeEnum dutyType = parseDutyType(res.getDutyType());
        res.setDutyTypeDesc(dutyType.getDesc());
    }

    /**
     * 填充详情的值班类型描述。
     */
    private void fillDutyTypeDesc(ClassDutyDetailRes res) {
        DutyTypeEnum dutyType = parseDutyType(res.getDutyType());
        res.setDutyTypeDesc(dutyType.getDesc());
    }

    /**
     * 组装班级值班项(按日期查询时使用)。
     *
     * 根据课表类型自动判断值班类型:
     *   - SELF_STUDY → 自习日值班(09:00-18:00)
     *   - CLASS      → 晚自习值班(19:00-21:00)
     *
     * 从 Map 中查找已有值班记录,填充已排班的老师信息。
     */
    private ClassDutyClassItemRes buildDateClassItem(ClassScheduleEntity schedule,
                                                     Map<Long, ClazzEntity> clazzMap,
                                                     Map<Long, CampusEntity> campusMap,
                                                     Map<String, ClassDutyEntity> classDutyMap,
                                                     Map<Long, SysUserEntity> teacherMap) {
        ClazzEntity clazz = clazzMap.get(schedule.getClassId());
        // 根据课表类型自动判断值班类型
        DutyTypeEnum dutyType = ClassScheduleTypeEnum.SELF_STUDY.name().equals(schedule.getClassType())
                ? DutyTypeEnum.SELF_STUDY_CLASS
                : DutyTypeEnum.EVENING_STUDY_CLASS;
        // 从 Map 查找已有值班(key = classId:dutyType)
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
        // 填充已排班的老师信息(可能为空,表示尚未排班)
        if (duty != null) {
            res.setTeacherId(duty.getTeacherId());
            res.setTeacherName(getUserShowName(teacherMap.get(duty.getTeacherId())));
        }
        return res;
    }

    /**
     * 组装校区值班项(按日期查询时使用)。
     *
     * 校区统一值班固定为 EVENING_STUDY_CAMPUS 类型(21:00-22:30)。
     * 从 Map 查找已有值班记录,填充已排班的老师信息。
     */
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

    /**
     * 构建班级值班 Map 的 key。
     * 格式:classId:dutyType(一个班级可能有多种值班类型)
     */
    private String buildClassDutyKey(Long classId, String dutyType) {
        return classId + ":" + dutyType;
    }

    /**
     * 获取课表类型描述。
     */
    private String getClassTypeDesc(String classType) {
        for (ClassScheduleTypeEnum item : ClassScheduleTypeEnum.values()) {
            if (item.name().equals(classType)) {
                return item.getDesc();
            }
        }
        return classType;
    }

    /**
     * 获取老师显示名称(排班页面使用)。
     * 优先级:realName > nickname > username
     */
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
