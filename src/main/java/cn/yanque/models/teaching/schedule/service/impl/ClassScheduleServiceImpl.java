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
import cn.yanque.models.teaching.schedule.pojo.info.HolidayInfo;
import cn.yanque.models.teaching.schedule.pojo.vo.req.ClassScheduleGenerateReq;
import cn.yanque.models.teaching.schedule.pojo.vo.res.ClassScheduleGenerateRes;
import cn.yanque.models.teaching.schedule.pojo.vo.res.ClassScheduleItemRes;
import cn.yanque.models.teaching.schedule.service.ClassScheduleService;
import cn.yanque.models.teaching.schedule.service.HolidayService;
import cn.yanque.models.teaching.schedule.service.ScheduleRuleService;
import cn.yanque.models.teaching.course.mapper.CourseDetailMapper;
import cn.yanque.models.teaching.course.pojo.entity.CourseDetailEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
