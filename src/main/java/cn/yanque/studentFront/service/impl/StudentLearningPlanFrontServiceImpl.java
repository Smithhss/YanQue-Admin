package cn.yanque.studentFront.service.impl;

import cn.yanque.common.exception.BusinessException;
import cn.yanque.common.threadlocal.StudentThreadLocal;
import cn.yanque.models.student.mapper.StudentLearningCalendarMapper;
import cn.yanque.models.student.mapper.StudentLearningPlanMapper;
import cn.yanque.models.student.mapper.StudentMapper;
import cn.yanque.models.student.pojo.entity.StudentEntity;
import cn.yanque.models.student.pojo.entity.StudentLearningCalendarEntity;
import cn.yanque.models.student.pojo.entity.StudentLearningPlanEntity;
import cn.yanque.models.teaching.course.mapper.CourseMapper;
import cn.yanque.models.teaching.course.pojo.entity.CourseEntity;
import cn.yanque.studentFront.pojo.res.StudentLearningCalendarFrontRes;
import cn.yanque.studentFront.pojo.res.StudentLearningPlanCurrentRes;
import cn.yanque.studentFront.service.StudentLearningPlanFrontService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class StudentLearningPlanFrontServiceImpl implements StudentLearningPlanFrontService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private StudentLearningPlanMapper learningPlanMapper;

    @Autowired
    private StudentLearningCalendarMapper calendarMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public StudentLearningPlanCurrentRes current() {
        StudentEntity student = validateStudent(StudentThreadLocal.get().getId());
        StudentLearningPlanEntity plan = learningPlanMapper.selectActiveByStudentId(student.getId());
        StudentLearningPlanCurrentRes res = new StudentLearningPlanCurrentRes();
        if (plan == null) {
            res.setHasPlan(false);
            res.setTotalDays(0);
            return res;
        }

        List<StudentLearningCalendarEntity> calendars = calendarMapper.selectByPlanId(plan.getId());
        res.setHasPlan(true);
        res.setId(plan.getId());
        res.setStartDate(plan.getStartDate());
        res.setStatus(plan.getStatus());
        res.setTotalDays(calendars.size());
        if (!calendars.isEmpty()) {
            res.setEndDate(calendars.get(calendars.size() - 1).getStudyDate());
        }

        CourseEntity course = courseMapper.selectById(plan.getCourseId());
        if (course != null) {
            res.setCourseName(course.getCourseName());
        }

        Date today = truncateDate(new Date());
        calendars.stream()
                .filter(item -> sameDay(item.getStudyDate(), today))
                .findFirst()
                .ifPresent(item -> {
                    res.setCurrentDayIndex(item.getDayIndex());
                    res.setTodayStageName(item.getStageName());
                });
        return res;
    }

    @Override
    public List<StudentLearningCalendarFrontRes> calendar() {
        StudentEntity student = validateStudent(StudentThreadLocal.get().getId());
        StudentLearningPlanEntity plan = learningPlanMapper.selectActiveByStudentId(student.getId());
        if (plan == null) {
            return List.of();
        }
        return calendarMapper.selectByPlanId(plan.getId()).stream().map(this::buildCalendarRes).toList();
    }

    private StudentEntity validateStudent(Long studentId) {
        StudentEntity student = studentMapper.selectById(studentId);
        if (student == null) {
            throw BusinessException.DateError.newInstance("学生不存在");
        }
        return student;
    }

    private StudentLearningCalendarFrontRes buildCalendarRes(StudentLearningCalendarEntity calendar) {
        StudentLearningCalendarFrontRes res = new StudentLearningCalendarFrontRes();
        BeanUtils.copyProperties(calendar, res);
        return res;
    }

    private boolean sameDay(Date left, Date right) {
        return left != null && right != null && truncateDate(left).equals(truncateDate(right));
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
}
