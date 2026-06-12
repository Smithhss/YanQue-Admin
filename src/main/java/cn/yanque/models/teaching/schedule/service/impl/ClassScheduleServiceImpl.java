package cn.yanque.models.teaching.schedule.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.teaching.clazz.mapper.ClazzMapper;
import cn.yanque.models.teaching.clazz.pojo.entity.ClazzEntity;
import cn.yanque.models.teaching.schedule.enums.ClassScheduleTypeEnum;
import cn.yanque.models.teaching.schedule.mapper.ClassScheduleMapper;
import cn.yanque.models.teaching.schedule.pojo.config.ScheduleRuleConfig;
import cn.yanque.models.teaching.schedule.pojo.entity.ClassScheduleEntity;
import cn.yanque.models.teaching.schedule.pojo.info.AddCourseInfo;
import cn.yanque.models.teaching.schedule.pojo.info.HolidayInfo;
import cn.yanque.models.teaching.schedule.pojo.vo.req.AddClassSchuleReq;
import cn.yanque.models.teaching.schedule.pojo.vo.req.ClassScheduleGenerateReq;
import cn.yanque.models.teaching.schedule.pojo.vo.req.ClassScheduleTeacherAssignReq;
import cn.yanque.models.teaching.schedule.pojo.vo.res.ClassScheduleDateDetailRes;
import cn.yanque.models.teaching.schedule.pojo.vo.res.ClassScheduleGenerateRes;
import cn.yanque.models.teaching.schedule.pojo.vo.res.ClassScheduleItemRes;
import cn.yanque.models.teaching.schedule.pojo.vo.res.ClassScheduleTeacherAssignRes;
import cn.yanque.models.teaching.schedule.pojo.vo.res.ClassStageInfoRes;
import cn.yanque.models.teaching.schedule.service.ClassScheduleService;
import cn.yanque.models.teaching.schedule.service.HolidayService;
import cn.yanque.models.teaching.schedule.service.ScheduleRuleService;
import cn.yanque.models.teaching.course.mapper.CourseDetailMapper;
import cn.yanque.models.teaching.course.pojo.entity.CourseDetailEntity;
import cn.yanque.models.users.pojo.entity.SysUserEntity;
import cn.yanque.models.users.pojo.vo.res.UserDetailRes;
import cn.yanque.models.users.service.SysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClassScheduleServiceImpl implements ClassScheduleService {

    @Autowired
    private ClazzMapper clazzMapper;

    @Autowired
    private CourseDetailMapper courseDetailMapper;

    @Autowired
    private ClassScheduleMapper classScheduleMapper;

    @Autowired
    private ScheduleRuleService scheduleRuleService;

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private SysUserService sysUserService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClassScheduleGenerateRes generateSchedule(ClassScheduleGenerateReq req) {
        ClazzEntity clazz = clazzMapper.selectById(req.getClassId());
        if (clazz == null) {
            throw BusinessException.ClazzNotExist;
        }

        // 获取上课规则配置
        ScheduleRuleConfig rule = scheduleRuleService.getScheduleRule();

        // 校验第一天上课时间
        validateFirstClassDate(req.getFirstClassDate(), rule);

        // 查询课程id
        List<CourseDetailEntity> courseDetails = courseDetailMapper.selectByCourseId(clazz.getCourseId());
        if (courseDetails.isEmpty()) {
            throw BusinessException.CourseDetailNotExist;
        }

        // 构建课表信息
        List<ClassScheduleEntity> schedules = buildSchedules(req.getClassId(), req.getFirstClassDate(), courseDetails, rule);

        // 删除之前的课表信息/ 批量插入信息
        classScheduleMapper.deleteByClassId(req.getClassId());
        classScheduleMapper.batchInsert(schedules);

        ClassScheduleGenerateRes res = new ClassScheduleGenerateRes();
        res.setClassId(req.getClassId());
        res.setScheduleCount(schedules.size());
        return res;
    }

    @Override
    public List<ClassScheduleItemRes> listSchedule(Long classId) {
        ClazzEntity clazz = clazzMapper.selectById(classId);
        if (clazz == null) {
            throw BusinessException.ClazzNotExist;
        }
        return classScheduleMapper.selectByClassId(classId).stream().map(this::buildItemRes).toList();
    }

    @Override
    public List<ClassStageInfoRes> classStageInfo(Long classId) {
        ClazzEntity clazz = clazzMapper.selectById(classId);
        if (clazz == null) {
            throw BusinessException.ClazzNotExist;
        }

        // 查看每个阶段上课时间范围 以及天数
        // 查询课程id
        List<CourseDetailEntity> courseDetails = courseDetailMapper.selectByCourseId(clazz.getCourseId());
        if (courseDetails.isEmpty()) {
            throw BusinessException.CourseDetailNotExist;
        }

        // key 阶段名称  value 阶段对应课程信息
        Map<String, List<CourseDetailEntity>> courseDetailGroup = courseDetails.stream().collect(Collectors.groupingBy(CourseDetailEntity::getStageName));


        List<ClassStageInfoRes> classStageInfoResList = new ArrayList<>();
        for (Map.Entry<String, List<CourseDetailEntity>> courseDetail : courseDetailGroup.entrySet()) {

            ClassStageInfoRes stageInfo = new ClassStageInfoRes();
            stageInfo.setStageName(courseDetail.getKey());
            stageInfo.setStageNumber(courseDetail.getValue().size());

            // 该阶段所有课程id
            List<Long> courseIds = courseDetail.getValue().stream().map(CourseDetailEntity::getId).toList();

            // 该阶段次班级上课信息
            List<ClassScheduleEntity> list = classScheduleMapper.selectByCourseIds(courseIds, classId);

            //得到阶段开始 结束时间
            Date stageStartDate = list.get(0).getScheduleDate();
            Date stageEndDate = list.get(list.size()-1).getScheduleDate();

            // 先查询上课老师
            List<Long> teacherIds = classScheduleMapper.selectTeacheringUserId(stageStartDate, stageEndDate, classId);

            // 查询所有老师
            List<SysUserEntity> teacher = sysUserService.getUserByRoleCode("TEACHER");

            teacher.removeIf(next -> teacherIds.contains(next.getId()));

            stageInfo.setFreeTeacherName(teacher.stream().collect(Collectors.toMap(SysUserEntity::getId, SysUserEntity::getRealName)));
            classStageInfoResList.add(stageInfo);
        }

        // 查看每个阶段上课时间范围的空闲老师
        return classStageInfoResList;
    }

    @Override
    public ClassScheduleDateDetailRes getDateDetail(Long classId, Date scheduleDate) {
        ClazzEntity clazz = clazzMapper.selectById(classId);
        if (clazz == null) {
            throw BusinessException.ClazzNotExist;
        }

        Date startDate = truncateDate(scheduleDate);
        Date endDate = addDays(startDate, 1);
        ClassScheduleEntity schedule = classScheduleMapper.selectByClassIdAndDate(classId, startDate, endDate);
        if (schedule == null) {
            throw BusinessException.DateError.newInstance("当天没有课表");
        }

        ClassScheduleDateDetailRes res = new ClassScheduleDateDetailRes();
        BeanUtils.copyProperties(schedule, res);

        if (schedule.getCourseDetailId() != null) {
            CourseDetailEntity courseDetail = courseDetailMapper.selectById(schedule.getCourseDetailId());
            if (courseDetail != null) {
                res.setStageName(courseDetail.getStageName());
                res.setDayNumber(courseDetail.getDayNumber());
            }
        }

        if (schedule.getTeacherId() != null) {
            UserDetailRes teacher = sysUserService.getUserById(schedule.getTeacherId());
            res.setTeacherName(getUserShowName(teacher));
        }
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClassScheduleTeacherAssignRes assignTeachers(Long classId, ClassScheduleTeacherAssignReq req) {
        ClazzEntity clazz = clazzMapper.selectById(classId);
        if (clazz == null) {
            throw BusinessException.ClazzNotExist;
        }

        //课程信息
        List<CourseDetailEntity> courseDetails = courseDetailMapper.selectByCourseId(clazz.getCourseId());
        if (courseDetails.isEmpty()) {
            throw BusinessException.CourseDetailNotExist;
        }

        // 课程信息分组 key 阶段名称 value list
        Map<String, List<CourseDetailEntity>> courseDetailGroup = groupCourseDetailsByStage(courseDetails);

        // 查询所有老师id
        Set<Long> teacherIds = sysUserService.getUserByRoleCode("TEACHER").stream()
                .map(SysUserEntity::getId)
                .collect(Collectors.toSet());

        int updateCount = 0;
        for (ClassScheduleTeacherAssignReq.StageTeacherAssignItem item : req.getStages()) {
            // 给每个阶段分配老师


            if (!teacherIds.contains(item.getTeacherId())) {
                throw BusinessException.UserNotExist.newInstance("老师不存在");
            }

            // 查询该阶段课程信息
            List<CourseDetailEntity> stageDetails = courseDetailGroup.get(item.getStageName());
            if (stageDetails == null || stageDetails.isEmpty()) {
                throw BusinessException.CourseDetailNotExist.newInstance("阶段不存在：" + item.getStageName());
            }

            List<Long> courseDetailIds = stageDetails.stream().map(CourseDetailEntity::getId).toList();

            // 此阶段课表的数据
            List<ClassScheduleEntity> schedules = classScheduleMapper.selectByCourseIds(courseDetailIds, classId);
            if (schedules.isEmpty()) {
                throw BusinessException.DateError.newInstance("该阶段还没有生成课表：" + item.getStageName());
            }

            Date stageStartDate = schedules.get(0).getScheduleDate();
            Date stageEndDate = schedules.get(schedules.size() - 1).getScheduleDate();
            List<Long> occupiedTeacherIds = classScheduleMapper.selectTeacheringUserId(stageStartDate, stageEndDate, classId);
            if (occupiedTeacherIds.contains(item.getTeacherId())) {
                throw BusinessException.DateError.newInstance("老师在该阶段时间内已有课程：" + item.getStageName());
            }

            updateCount += classScheduleMapper.updateTeacherByCourseDetailIds(classId, courseDetailIds, item.getTeacherId());
        }

        ClassScheduleTeacherAssignRes res = new ClassScheduleTeacherAssignRes();
        res.setClassId(classId);
        res.setUpdateCount(updateCount);
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addClassSchule(Long classId, AddClassSchuleReq req) {

        // 查询班级信息
        ClazzEntity clazz = clazzMapper.selectById(classId);
        if (clazz == null) {
            throw BusinessException.ClazzNotExist;
        }
        if (req.getScheduleDate() == null) {
            throw BusinessException.DateError.newInstance("加课日期不能为空");
        }

        Date scheduleDate = truncateDate(req.getScheduleDate());

       // 查询加课日期后面的课表信息
        List<ClassScheduleEntity> oldList = classScheduleMapper.selectByClassIdAndAfterScheduleDate(classId, scheduleDate);
        if (oldList.isEmpty()) {
            throw BusinessException.DateError.newInstance("加课日期后没有课表数据");
        }

        AddCourseInfo addCourseInfo = new AddCourseInfo();
        BeanUtils.copyProperties(req, addCourseInfo);
        addCourseInfo.setScheduleDate(scheduleDate);
        List<ClassScheduleEntity> newList = buildAddClassSchuleList(oldList, addCourseInfo);
        if (newList.isEmpty()) {
            throw BusinessException.DateError.newInstance("新课表数据不能为空");
        }

        // 删除之前课表的数据
        classScheduleMapper.deleteByClassIdAndAfterScheduleDate(classId, scheduleDate);

        // 新增新数据
        classScheduleMapper.batchInsert(newList);

        // 判断是否有重复
        // select teacher_id,schedule_date,count(*) cnt from sys_class_schedule where teacher_id IS NOT NULL group by teacher_id,schedule_date having  cnt > 1
        int duplicateCount = classScheduleMapper.countDuplicateTeacherSchedule();

        // 有重复抛异常
        if (duplicateCount > 0) {
            throw BusinessException.DateError.newInstance("老师同一天存在重复课表");
        }

    }

    private List<ClassScheduleEntity> buildAddClassSchuleList(List<ClassScheduleEntity> oldList, AddCourseInfo addCourseInfo) {

        List<ClassScheduleEntity> newList = new ArrayList<>();
        List<ClassScheduleEntity> removedList = new ArrayList<>(oldList);
        Date curDate = null;

        for (ClassScheduleEntity classScheduleEntity : oldList) {

            if (DateUtil.isSameDay(classScheduleEntity.getScheduleDate(), addCourseInfo.getScheduleDate())) {

                // 新增加进去
                // 是否不是上课
                // 如果不是上课直接修改当前数据
                // 如果是上课加一条新数据
                if (!classScheduleEntity.getClassType().equals(ClassScheduleTypeEnum.CLASS.name())) {
                    ClassScheduleEntity newEntity = new ClassScheduleEntity();
                    BeanUtils.copyProperties(classScheduleEntity, newEntity);
                    newEntity.setTeacherId(addCourseInfo.getTeacherId());
                    newEntity.setCourseContent(addCourseInfo.getCourseContent());
                    newEntity.setCourseDetailId(null);
                    newEntity.setClassType(ClassScheduleTypeEnum.CLASS.name());
                    newList.add(newEntity);
                    removedList.remove(classScheduleEntity);
                }  else {
                    ClassScheduleEntity newEntity = new ClassScheduleEntity();
                    newEntity.setClassType(ClassScheduleTypeEnum.CLASS.name());
                    newEntity.setScheduleDate(addCourseInfo.getScheduleDate());
                    newEntity.setTeacherId(addCourseInfo.getTeacherId());
                    newEntity.setCourseContent(addCourseInfo.getCourseContent());
                    newEntity.setClassId(classScheduleEntity.getClassId());
                    newList.add(newEntity);
                }
                curDate = DateUtil.offsetDay(addCourseInfo.getScheduleDate(), 1);
                break;

            } else {
                // 老数据
                ClassScheduleEntity newEntity = new ClassScheduleEntity();
                BeanUtils.copyProperties(classScheduleEntity, newEntity);
                newList.add(newEntity);
                removedList.remove(classScheduleEntity);
            }

        }
        if (curDate == null) {
            throw BusinessException.DateError.newInstance("加课日期不在当前课表范围内");
        }

        // 获取上课规则配置
        ScheduleRuleConfig rule = scheduleRuleService.getScheduleRule();
        // 只要对removeList重新排课
        int courseIndex = 0;
        removedList.removeIf(classScheduleEntity -> !classScheduleEntity.getClassType().equals(ClassScheduleTypeEnum.CLASS.name()));
        while (courseIndex < removedList.size()) {
            int weekValue = getWeekValue(curDate);

            if (Boolean.TRUE.equals(rule.getHolidayRest())) {
                HolidayInfo holidayInfo = holidayService.getHolidayInfo(curDate);
                if (holidayInfo != null
                        && Boolean.TRUE.equals(holidayInfo.getHoliday())) {
                    newList.add(buildSchedule(oldList.get(0).getClassId(), curDate, null, holidayInfo.getName(), ClassScheduleTypeEnum.HOLIDAY));
                    curDate = addDays(curDate, 1);
                    continue;
                }
            }

            if (rule.getRestDays() != null && rule.getRestDays().contains(weekValue)) {
                newList.add(buildSchedule(oldList.get(0).getClassId(), curDate, null, "休息", ClassScheduleTypeEnum.REST));
                curDate = addDays(curDate, 1);
                continue;
            }

            if (rule.getSelfStudyDays() != null && rule.getSelfStudyDays().contains(weekValue)) {
                newList.add(buildSchedule(oldList.get(0).getClassId(), curDate, null, "自习", ClassScheduleTypeEnum.SELF_STUDY));
                curDate = addDays(curDate, 1);
                continue;
            }

            if (rule.getClassDays().contains(weekValue)) {
                ClassScheduleEntity detail = removedList.get(courseIndex);
                ClassScheduleEntity schedule = new ClassScheduleEntity();
                schedule.setClassId(oldList.get(0).getClassId());
                schedule.setTeacherId(detail.getTeacherId());
                schedule.setScheduleDate(truncateDate(curDate));
                schedule.setCourseDetailId(detail.getCourseDetailId());
                schedule.setCourseContent(detail.getCourseContent());
                schedule.setClassType(detail.getClassType());
                newList.add(schedule);
                courseIndex++;
            }

            curDate = addDays(curDate, 1);
        }



        return newList;
    }

    private void validateFirstClassDate(Date firstClassDate, ScheduleRuleConfig rule) {
        int weekValue = getWeekValue(firstClassDate);
        if (Boolean.TRUE.equals(rule.getHolidayRest())) {
            HolidayInfo holidayInfo = holidayService.getHolidayInfo(firstClassDate);
            if (holidayInfo != null
                && Boolean.TRUE.equals(holidayInfo.getHoliday())) {
                throw BusinessException.DateError.newInstance("第一天上课时间不能是节假日");
            }
        }
        if (rule.getClassDays() == null || !rule.getClassDays().contains(weekValue)) {
            throw BusinessException.DateError.newInstance("第一天上课时间必须是配置中的上课日");
        }
    }

    private List<ClassScheduleEntity> buildSchedules(Long classId,
                                                     Date firstClassDate,
                                                     List<CourseDetailEntity> courseDetails,
                                                     ScheduleRuleConfig rule) {
        List<ClassScheduleEntity> schedules = new ArrayList<>();
        Date currentDate = truncateDate(firstClassDate);
        int courseIndex = 0;

        while (courseIndex < courseDetails.size()) {
            int weekValue = getWeekValue(currentDate);

            if (Boolean.TRUE.equals(rule.getHolidayRest())) {
                HolidayInfo holidayInfo = holidayService.getHolidayInfo(currentDate);
                if (holidayInfo != null
                    && Boolean.TRUE.equals(holidayInfo.getHoliday())) {
                    schedules.add(buildSchedule(classId, currentDate, null, holidayInfo.getName(), ClassScheduleTypeEnum.HOLIDAY));
                    currentDate = addDays(currentDate, 1);
                    continue;
                }
            }

            if (rule.getRestDays() != null && rule.getRestDays().contains(weekValue)) {
                schedules.add(buildSchedule(classId, currentDate, null, "休息", ClassScheduleTypeEnum.REST));
                currentDate = addDays(currentDate, 1);
                continue;
            }

            if (rule.getSelfStudyDays() != null && rule.getSelfStudyDays().contains(weekValue)) {
                schedules.add(buildSchedule(classId, currentDate, null, "自习", ClassScheduleTypeEnum.SELF_STUDY));
                currentDate = addDays(currentDate, 1);
                continue;
            }

            if (rule.getClassDays().contains(weekValue)) {
                CourseDetailEntity detail = courseDetails.get(courseIndex);
                schedules.add(buildSchedule(classId, currentDate, detail, detail.getClassContent(), ClassScheduleTypeEnum.CLASS));
                courseIndex++;
            }

            currentDate = addDays(currentDate, 1);
        }
        return schedules;
    }

    private ClassScheduleEntity buildSchedule(Long classId,
                                              Date scheduleDate,
                                              CourseDetailEntity detail,
                                              String courseContent,
                                              ClassScheduleTypeEnum classType) {
        ClassScheduleEntity schedule = new ClassScheduleEntity();
        schedule.setClassId(classId);
        schedule.setTeacherId(null);
        schedule.setScheduleDate(truncateDate(scheduleDate));
        schedule.setCourseDetailId(detail == null ? null : detail.getId());
        schedule.setCourseContent(courseContent == null ? classType.getDesc() : courseContent);
        schedule.setClassType(classType.name());
        return schedule;
    }

    private ClassScheduleItemRes buildItemRes(ClassScheduleEntity schedule) {
        ClassScheduleItemRes res = new ClassScheduleItemRes();
        BeanUtils.copyProperties(schedule, res);
        return res;
    }

    private Map<String, List<CourseDetailEntity>> groupCourseDetailsByStage(List<CourseDetailEntity> courseDetails) {
        return courseDetails.stream().collect(Collectors.groupingBy(
                item -> item.getStageName() == null ? "未分阶段" : item.getStageName(),
                LinkedHashMap::new,
                Collectors.toList()
        ));
    }

    private String getUserShowName(SysUserEntity user) {
        if (user.getRealName() != null && !user.getRealName().isBlank()) {
            return user.getRealName();
        }
        if (user.getNickname() != null && !user.getNickname().isBlank()) {
            return user.getNickname();
        }
        return user.getUsername();
    }

    private String getUserShowName(UserDetailRes user) {
        if (user.getRealName() != null && !user.getRealName().isBlank()) {
            return user.getRealName();
        }
        if (user.getNickname() != null && !user.getNickname().isBlank()) {
            return user.getNickname();
        }
        return user.getUsername();
    }

    private int getWeekValue(Date date) {
        return DateUtil.dayOfWeekEnum(date).getIso8601Value();
    }

    private Date addDays(Date date, int days) {
        return DateUtil.beginOfDay(DateUtil.offset(date, DateField.DAY_OF_MONTH, days));
    }

    private Date truncateDate(Date date) {
        return DateUtil.beginOfDay(date);
    }
}
