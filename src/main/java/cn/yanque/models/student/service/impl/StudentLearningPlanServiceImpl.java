package cn.yanque.models.student.service.impl;

import cn.yanque.common.api.PageResult;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.student.mapper.StudentLearningCalendarMapper;
import cn.yanque.models.student.mapper.StudentLearningPlanMapper;
import cn.yanque.models.student.mapper.StudentMapper;
import cn.yanque.models.student.mapper.StudentSopMapper;
import cn.yanque.models.student.pojo.bo.QueryStudentLearningPlanBo;
import cn.yanque.models.student.pojo.entity.StudentEntity;
import cn.yanque.models.student.pojo.entity.StudentLearningCalendarEntity;
import cn.yanque.models.student.pojo.entity.StudentLearningPlanEntity;
import cn.yanque.models.student.pojo.entity.StudentSopEntity;
import cn.yanque.models.student.pojo.vo.req.StudentLearningPlanCreateReq;
import cn.yanque.models.student.pojo.vo.req.StudentLearningPlanPageReq;
import cn.yanque.models.student.pojo.vo.req.StudentLearningPlanStageReq;
import cn.yanque.models.student.pojo.vo.res.StudentLearningCalendarRes;
import cn.yanque.models.student.pojo.vo.res.StudentLearningPlanCreateRes;
import cn.yanque.models.student.pojo.vo.res.StudentLearningPlanDetailRes;
import cn.yanque.models.student.pojo.vo.res.StudentLearningPlanPageRes;
import cn.yanque.models.student.service.StudentLearningPlanService;
import cn.yanque.models.teaching.course.mapper.CourseDetailMapper;
import cn.yanque.models.teaching.course.mapper.CourseMapper;
import cn.yanque.models.teaching.course.pojo.entity.CourseDetailEntity;
import cn.yanque.models.teaching.course.pojo.entity.CourseEntity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class StudentLearningPlanServiceImpl implements StudentLearningPlanService {

    private static final String TEACHING_MODE_ONLINE = "ONLINE";
    private static final String SOP_STATUS_COMPLETED = "COMPLETED";
    private static final String PLAN_STATUS_ACTIVE = "ACTIVE";
    private static final String CALENDAR_STATUS_TODO = "TODO";

    @Autowired
    private StudentLearningPlanMapper planMapper;

    @Autowired
    private StudentLearningCalendarMapper calendarMapper;

    @Autowired
    private StudentSopMapper studentSopMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseDetailMapper courseDetailMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentLearningPlanCreateRes createPlan(StudentLearningPlanCreateReq req) {
        StudentSopEntity sop = studentSopMapper.selectById(req.getSopId());
        if (sop == null) {
            throw BusinessException.DateError.newInstance("SOP记录不存在");
        }
        if (!SOP_STATUS_COMPLETED.equals(sop.getStatus())) {
            throw BusinessException.DateError.newInstance("只有完成SOP后才能定制学习计划");
        }
        StudentEntity student = studentMapper.selectById(sop.getStudentId());
        if (student == null) {
            throw BusinessException.DateError.newInstance("学生不存在");
        }
        if (!TEACHING_MODE_ONLINE.equals(student.getTeachingMode())) {
            throw BusinessException.DateError.newInstance("只有线上学员可以定制学习计划");
        }
        if (planMapper.selectActiveByStudentId(student.getId()) != null) {
            throw BusinessException.DateExist.newInstance("该学员已有生效学习计划");
        }

        CourseEntity course = courseMapper.selectById(req.getCourseId());
        if (course == null) {
            throw BusinessException.CourseNotExist;
        }
        if (!TEACHING_MODE_ONLINE.equals(course.getTeachingMode())) {
            throw BusinessException.DateError.newInstance("学习计划只能选择线上课程");
        }
        List<StudentLearningPlanStageReq> normalizedStages = validateStages(course.getId(), req.getStages());

        StudentLearningPlanEntity plan = new StudentLearningPlanEntity();
        plan.setStudentId(student.getId());
        plan.setCourseId(course.getId());
        plan.setSopId(sop.getId());
        plan.setStartDate(truncateDate(req.getStartDate()));
        plan.setStatus(PLAN_STATUS_ACTIVE);
        plan.setCreatedAt(new Date());
        plan.setUpdatedAt(new Date());
        planMapper.insert(plan);

        List<StudentLearningCalendarEntity> calendars = buildCalendars(plan, normalizedStages);
        calendarMapper.batchInsert(calendars);

        StudentLearningPlanCreateRes res = new StudentLearningPlanCreateRes();
        res.setId(plan.getId());
        res.setCalendarCount(calendars.size());
        return res;
    }

    @Override
    public PageResult<StudentLearningPlanPageRes> pagePlan(StudentLearningPlanPageReq req) {
        int pageNum = req.getPageNum() == null ? 1 : req.getPageNum();
        int pageSize = req.getPageSize() == null ? 10 : req.getPageSize();
        QueryStudentLearningPlanBo query = new QueryStudentLearningPlanBo();
        BeanUtils.copyProperties(req, query);
        PageHelper.startPage(pageNum, pageSize);
        List<StudentLearningPlanEntity> list = planMapper.selectPage(query);
        PageInfo<StudentLearningPlanEntity> pageInfo = new PageInfo<>(list);

        Map<Long, StudentEntity> studentMap = buildStudentMap(list);
        Map<Long, CourseEntity> courseMap = buildCourseMap(list);
        List<StudentLearningPlanPageRes> records = list.stream()
                .map(item -> buildPageRes(item, studentMap, courseMap))
                .toList();
        return new PageResult<>(pageInfo.getTotal(), pageNum, pageSize, records);
    }

    @Override
    public StudentLearningPlanDetailRes detail(Long id) {
        StudentLearningPlanEntity plan = getPlan(id);
        StudentEntity student = studentMapper.selectById(plan.getStudentId());
        CourseEntity course = courseMapper.selectById(plan.getCourseId());

        StudentLearningPlanDetailRes res = new StudentLearningPlanDetailRes();
        BeanUtils.copyProperties(plan, res);
        if (student != null) {
            res.setStudentName(student.getStudentName());
            res.setStudentPhone(student.getStudentPhone());
        }
        if (course != null) {
            res.setCourseName(course.getCourseName());
        }
        res.setStages(buildStageResFromCalendars(calendarMapper.selectByPlanId(id)));
        return res;
    }

    @Override
    public List<StudentLearningCalendarRes> calendar(Long id) {
        getPlan(id);
        return calendarMapper.selectByPlanId(id).stream().map(this::buildCalendarRes).toList();
    }

    private List<StudentLearningPlanStageReq> validateStages(Long courseId, List<StudentLearningPlanStageReq> stages) {
        List<String> courseStages = courseDetailMapper.selectByCourseId(courseId).stream()
                .map(CourseDetailEntity::getStageName)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (courseStages.isEmpty()) {
            throw BusinessException.DateError.newInstance("该线上课程未维护阶段");
        }

        Map<String, StudentLearningPlanStageReq> stageMap = new LinkedHashMap<>();
        for (StudentLearningPlanStageReq item : stages) {
            String stageName = item.getStageName() == null ? null : item.getStageName().trim();
            if (stageName == null || stageName.isBlank()) {
                throw BusinessException.DateError.newInstance("阶段不能为空");
            }
            if (!courseStages.contains(stageName)) {
                throw BusinessException.DateError.newInstance("阶段不属于所选课程：" + stageName);
            }
            if (item.getStageDays() == null || item.getStageDays() <= 0) {
                throw BusinessException.DateError.newInstance("阶段学习天数必须大于0");
            }
            StudentLearningPlanStageReq normalized = new StudentLearningPlanStageReq();
            normalized.setStageName(stageName);
            normalized.setStageDays(item.getStageDays());
            stageMap.put(stageName, normalized);
        }

        // 阶段顺序以课程详情为准，防止前端传参顺序和课程阶段顺序不一致。
        List<StudentLearningPlanStageReq> normalizedStages = new ArrayList<>();
        for (String courseStage : courseStages) {
            StudentLearningPlanStageReq stage = stageMap.get(courseStage);
            if (stage == null) {
                throw BusinessException.DateError.newInstance("请填写阶段学习天数：" + courseStage);
            }
            normalizedStages.add(stage);
        }
        return normalizedStages;
    }

    private List<StudentLearningCalendarEntity> buildCalendars(StudentLearningPlanEntity plan, List<StudentLearningPlanStageReq> stages) {
        Date now = new Date();
        List<StudentLearningCalendarEntity> list = new ArrayList<>();
        int dayIndex = 1;
        for (StudentLearningPlanStageReq stage : stages) {
            for (int stageDayIndex = 1; stageDayIndex <= stage.getStageDays(); stageDayIndex++) {
                StudentLearningCalendarEntity calendar = new StudentLearningCalendarEntity();
                calendar.setPlanId(plan.getId());
                calendar.setStudentId(plan.getStudentId());
                calendar.setStudyDate(addDays(plan.getStartDate(), dayIndex - 1));
                calendar.setStageName(stage.getStageName());
                calendar.setDayIndex(dayIndex);
                calendar.setStageDayIndex(stageDayIndex);
                calendar.setStatus(CALENDAR_STATUS_TODO);
                calendar.setCreatedAt(now);
                calendar.setUpdatedAt(now);
                list.add(calendar);
                dayIndex++;
            }
        }
        return list;
    }

    private List<cn.yanque.models.student.pojo.vo.res.StudentLearningPlanStageRes> buildStageResFromCalendars(List<StudentLearningCalendarEntity> calendars) {
        Map<String, Integer> stageDaysMap = new LinkedHashMap<>();
        for (StudentLearningCalendarEntity calendar : calendars) {
            stageDaysMap.merge(calendar.getStageName(), 1, Integer::sum);
        }
        List<cn.yanque.models.student.pojo.vo.res.StudentLearningPlanStageRes> list = new ArrayList<>();
        int sortOrder = 1;
        for (Map.Entry<String, Integer> entry : stageDaysMap.entrySet()) {
            cn.yanque.models.student.pojo.vo.res.StudentLearningPlanStageRes res = new cn.yanque.models.student.pojo.vo.res.StudentLearningPlanStageRes();
            res.setStageName(entry.getKey());
            res.setStageDays(entry.getValue());
            res.setSortOrder(sortOrder++);
            list.add(res);
        }
        return list;
    }

    private StudentLearningPlanEntity getPlan(Long id) {
        StudentLearningPlanEntity plan = planMapper.selectById(id);
        if (plan == null) {
            throw BusinessException.DateError.newInstance("学习计划不存在");
        }
        return plan;
    }

    private Map<Long, StudentEntity> buildStudentMap(List<StudentLearningPlanEntity> list) {
        List<Long> studentIds = list.stream().map(StudentLearningPlanEntity::getStudentId).distinct().toList();
        if (studentIds.isEmpty()) {
            return Map.of();
        }
        return studentMapper.selectByIds(studentIds).stream().collect(Collectors.toMap(StudentEntity::getId, item -> item));
    }

    private Map<Long, CourseEntity> buildCourseMap(List<StudentLearningPlanEntity> list) {
        List<Long> courseIds = list.stream().map(StudentLearningPlanEntity::getCourseId).distinct().toList();
        if (courseIds.isEmpty()) {
            return Map.of();
        }
        return courseMapper.selectByIds(courseIds).stream().collect(Collectors.toMap(CourseEntity::getId, item -> item));
    }

    private StudentLearningPlanPageRes buildPageRes(StudentLearningPlanEntity plan,
                                                    Map<Long, StudentEntity> studentMap,
                                                    Map<Long, CourseEntity> courseMap) {
        StudentLearningPlanPageRes res = new StudentLearningPlanPageRes();
        BeanUtils.copyProperties(plan, res);
        StudentEntity student = studentMap.get(plan.getStudentId());
        if (student != null) {
            res.setStudentName(student.getStudentName());
            res.setStudentPhone(student.getStudentPhone());
        }
        CourseEntity course = courseMap.get(plan.getCourseId());
        if (course != null) {
            res.setCourseName(course.getCourseName());
        }
        return res;
    }

    private StudentLearningCalendarRes buildCalendarRes(StudentLearningCalendarEntity calendar) {
        StudentLearningCalendarRes res = new StudentLearningCalendarRes();
        BeanUtils.copyProperties(calendar, res);
        return res;
    }

    private Date truncateDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private Date addDays(Date startDate, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }
}
