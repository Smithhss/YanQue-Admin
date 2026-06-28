package cn.yanque.models.teaching.attendance.service.impl;

import cn.yanque.common.api.PageResult;
import cn.yanque.common.enums.ActiveEnum;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.student.coursehour.service.StudentCourseHourService;
import cn.yanque.models.student.mapper.StudentMapper;
import cn.yanque.models.student.pojo.entity.StudentEntity;
import cn.yanque.models.teaching.attendance.enums.AttendanceStatusEnum;
import cn.yanque.models.teaching.attendance.mapper.AttendanceMapper;
import cn.yanque.models.teaching.attendance.pojo.entity.ClassAttendanceEntity;
import cn.yanque.models.teaching.attendance.pojo.vo.req.AttendanceCommitItem;
import cn.yanque.models.teaching.attendance.pojo.vo.req.AttendanceCommitReq;
import cn.yanque.models.teaching.attendance.pojo.vo.req.AttendancePageReq;
import cn.yanque.models.teaching.attendance.pojo.vo.res.AttendanceCommitRes;
import cn.yanque.models.teaching.attendance.pojo.vo.res.AttendancePageRes;
import cn.yanque.models.teaching.attendance.pojo.vo.res.AttendanceRosterItemRes;
import cn.yanque.models.teaching.attendance.pojo.vo.res.AttendanceRosterRes;
import cn.yanque.models.teaching.attendance.pojo.vo.res.AttendanceWarningRes;
import cn.yanque.models.teaching.attendance.service.AttendanceService;
import cn.yanque.models.teaching.schedule.enums.ClassScheduleTypeEnum;
import cn.yanque.models.teaching.schedule.pojo.entity.ClassScheduleEntity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    /** 一次上课消耗的课时;出勤/迟到/旷课扣减,请假不扣。 */
    private static final BigDecimal HOUR_PER_CLASS = new BigDecimal("1.0");

    @Autowired
    private AttendanceMapper attendanceMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private StudentCourseHourService studentCourseHourService;

    @Override
    public AttendanceRosterRes roster(Long scheduleId) {
        ClassScheduleEntity schedule = attendanceMapper.selectScheduleById(scheduleId);
        if (schedule == null) {
            throw BusinessException.ScheduleNotExist;
        }

        List<StudentEntity> students = studentMapper.selectByClassId(schedule.getClassId()).stream()
                .filter(s -> ActiveEnum.ACTIVE.name().equals(s.getStatus()))
                .toList();
        Map<Long, ClassAttendanceEntity> done = attendanceMapper.selectByScheduleId(scheduleId).stream()
                .collect(Collectors.toMap(ClassAttendanceEntity::getStudentId, Function.identity()));

        List<AttendanceRosterItemRes> items = students.stream().map(s -> {
            AttendanceRosterItemRes item = new AttendanceRosterItemRes();
            item.setStudentId(s.getId());
            item.setStudentName(s.getStudentName());
            item.setStudentNo(s.getStudentNo());
            ClassAttendanceEntity a = done.get(s.getId());
            if (a != null) {
                item.setStatus(a.getStatus());
                item.setLeaveReason(a.getLeaveReason());
            }
            return item;
        }).toList();

        AttendanceRosterRes res = new AttendanceRosterRes();
        res.setScheduleId(scheduleId);
        res.setClassId(schedule.getClassId());
        res.setScheduleDate(schedule.getScheduleDate());
        res.setCourseContent(schedule.getCourseContent());
        res.setItems(items);
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AttendanceCommitRes commit(AttendanceCommitReq req, Long operatorId) {
        ClassScheduleEntity schedule = attendanceMapper.selectScheduleById(req.getScheduleId());
        if (schedule == null) {
            throw BusinessException.ScheduleNotExist;
        }
        if (!ClassScheduleTypeEnum.CLASS.name().equals(schedule.getClassType())) {
            throw BusinessException.AttendanceNotClassType;
        }

        Map<Long, ClassAttendanceEntity> existing = attendanceMapper.selectByScheduleId(req.getScheduleId()).stream()
                .collect(Collectors.toMap(ClassAttendanceEntity::getStudentId, Function.identity()));

        List<AttendanceWarningRes> warnings = new ArrayList<>();
        int committed = 0;
        for (AttendanceCommitItem item : req.getItems()) {
            BigDecimal newDeduct = deductByStatus(item.getStatus());
            ClassAttendanceEntity old = existing.get(item.getStudentId());

            if (old != null) {
                // 改点名:先按旧快照退回课时,再更新记录与扣减,避免重复扣。
                if (old.getHourDeducted() != null && old.getHourDeducted().signum() > 0) {
                    studentCourseHourService.revert(item.getStudentId(), old.getHourDeducted(), schedule.getId(), operatorId);
                }
                old.setStatus(item.getStatus());
                old.setLeaveReason(item.getLeaveReason());
                old.setHourDeducted(newDeduct);
                old.setOperatorId(operatorId);
                attendanceMapper.updateById(old);
            } else {
                ClassAttendanceEntity entity = new ClassAttendanceEntity();
                entity.setScheduleId(schedule.getId());
                entity.setClassId(schedule.getClassId());
                entity.setStudentId(item.getStudentId());
                entity.setScheduleDate(schedule.getScheduleDate());
                entity.setStatus(item.getStatus());
                entity.setLeaveReason(item.getLeaveReason());
                entity.setHourDeducted(newDeduct);
                entity.setOperatorId(operatorId);
                attendanceMapper.insert(entity);
            }

            if (newDeduct.signum() > 0) {
                BigDecimal remaining = studentCourseHourService.consume(item.getStudentId(), newDeduct, schedule.getId(), operatorId);
                if (remaining.signum() < 0) {
                    AttendanceWarningRes warning = new AttendanceWarningRes();
                    warning.setStudentId(item.getStudentId());
                    warning.setRemainingHours(remaining);
                    warnings.add(warning);
                }
            }
            committed++;
        }

        fillWarningNames(warnings);

        AttendanceCommitRes res = new AttendanceCommitRes();
        res.setCommittedCount(committed);
        res.setWarnings(warnings);
        return res;
    }

    @Override
    public PageResult<AttendancePageRes> page(AttendancePageReq req) {
        int pageNum = req.getPageNum() == null ? 1 : req.getPageNum();
        int pageSize = req.getPageSize() == null ? 10 : req.getPageSize();

        PageHelper.startPage(pageNum, pageSize);
        List<ClassAttendanceEntity> list = attendanceMapper.selectPage(req.getClassId(), req.getStudentId(), req.getDateFrom(), req.getDateTo());
        PageInfo<ClassAttendanceEntity> pageInfo = new PageInfo<>(list);

        Map<Long, StudentEntity> studentMap = Collections.emptyMap();
        if (!list.isEmpty()) {
            List<Long> studentIds = list.stream().map(ClassAttendanceEntity::getStudentId).distinct().toList();
            studentMap = studentMapper.selectByIds(studentIds).stream()
                    .collect(Collectors.toMap(StudentEntity::getId, Function.identity()));
        }
        Map<Long, StudentEntity> finalMap = studentMap;
        List<AttendancePageRes> records = list.stream()
                .map(e -> buildPageRes(e, finalMap.get(e.getStudentId())))
                .toList();
        return new PageResult<>(pageInfo.getTotal(), pageNum, pageSize, records);
    }

    private BigDecimal deductByStatus(String status) {
        // 请假不扣课时;出勤/迟到/旷课均按一次课消耗。
        if (AttendanceStatusEnum.LEAVE.name().equals(status)) {
            return BigDecimal.ZERO;
        }
        return HOUR_PER_CLASS;
    }

    private void fillWarningNames(List<AttendanceWarningRes> warnings) {
        if (warnings.isEmpty()) {
            return;
        }
        List<Long> ids = warnings.stream().map(AttendanceWarningRes::getStudentId).toList();
        Map<Long, StudentEntity> map = studentMapper.selectByIds(ids).stream()
                .collect(Collectors.toMap(StudentEntity::getId, Function.identity()));
        warnings.forEach(w -> {
            StudentEntity s = map.get(w.getStudentId());
            if (s != null) {
                w.setStudentName(s.getStudentName());
            }
        });
    }

    private AttendancePageRes buildPageRes(ClassAttendanceEntity entity, StudentEntity student) {
        AttendancePageRes res = new AttendancePageRes();
        res.setId(entity.getId());
        res.setScheduleId(entity.getScheduleId());
        res.setClassId(entity.getClassId());
        res.setStudentId(entity.getStudentId());
        res.setScheduleDate(entity.getScheduleDate());
        res.setStatus(entity.getStatus());
        res.setHourDeducted(entity.getHourDeducted());
        if (student != null) {
            res.setStudentName(student.getStudentName());
            res.setStudentNo(student.getStudentNo());
        }
        return res;
    }
}
