package cn.yanque.models.student.coursehour.service.impl;

import cn.yanque.common.api.PageResult;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.student.coursehour.enums.CourseHourChangeTypeEnum;
import cn.yanque.models.student.coursehour.mapper.StudentCourseHourLogMapper;
import cn.yanque.models.student.coursehour.mapper.StudentCourseHourMapper;
import cn.yanque.models.student.coursehour.pojo.entity.StudentCourseHourEntity;
import cn.yanque.models.student.coursehour.pojo.entity.StudentCourseHourLogEntity;
import cn.yanque.models.student.coursehour.pojo.vo.req.CourseHourAdjustReq;
import cn.yanque.models.student.coursehour.pojo.vo.req.StudentCourseHourPageReq;
import cn.yanque.models.student.coursehour.pojo.vo.res.StudentCourseHourPageRes;
import cn.yanque.models.student.coursehour.pojo.vo.res.StudentCourseHourRes;
import cn.yanque.models.student.coursehour.service.StudentCourseHourService;
import cn.yanque.models.student.mapper.StudentMapper;
import cn.yanque.models.student.pojo.entity.StudentEntity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StudentCourseHourServiceImpl implements StudentCourseHourService {

    @Autowired
    private StudentCourseHourMapper courseHourMapper;

    @Autowired
    private StudentCourseHourLogMapper courseHourLogMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public StudentCourseHourRes getByStudentId(Long studentId) {
        StudentCourseHourEntity entity = courseHourMapper.selectByStudentId(studentId);
        StudentCourseHourRes res = new StudentCourseHourRes();
        res.setStudentId(studentId);
        if (entity == null) {
            // 账户尚未建立的学生视为零余额,便于前端统一展示。
            res.setTotalHours(BigDecimal.ZERO);
            res.setUsedHours(BigDecimal.ZERO);
            res.setRemainingHours(BigDecimal.ZERO);
        } else {
            res.setTotalHours(entity.getTotalHours());
            res.setUsedHours(entity.getUsedHours());
            res.setRemainingHours(entity.getRemainingHours());
        }
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentCourseHourRes adjust(CourseHourAdjustReq req, Long operatorId) {
        Long studentId = req.getStudentId();
        BigDecimal change = req.getChangeHours();

        ensureAccount(studentId);

        // total = used + remaining 守恒:充值进 total,扣减进 used。
        BigDecimal deltaTotal = change.signum() > 0 ? change : BigDecimal.ZERO;
        BigDecimal deltaUsed = change.signum() < 0 ? change.negate() : BigDecimal.ZERO;
        int rows = courseHourMapper.addHours(studentId, deltaTotal, deltaUsed, change);
        if (rows == 0) {
            throw BusinessException.InsufficientCourseHours;
        }

        StudentCourseHourEntity after = courseHourMapper.selectByStudentId(studentId);
        writeLog(studentId, CourseHourChangeTypeEnum.ADJUST.name(), change,
                after.getRemainingHours(), null, req.getReason(), operatorId);

        StudentCourseHourRes res = new StudentCourseHourRes();
        res.setStudentId(studentId);
        res.setTotalHours(after.getTotalHours());
        res.setUsedHours(after.getUsedHours());
        res.setRemainingHours(after.getRemainingHours());
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BigDecimal consume(Long studentId, BigDecimal hours, Long scheduleId, Long operatorId) {
        ensureAccount(studentId);
        // 消耗:used += hours,remaining -= hours。
        int rows = courseHourMapper.addHours(studentId, BigDecimal.ZERO, hours, hours.negate());
        if (rows == 0) {
            throw BusinessException.InsufficientCourseHours;
        }
        StudentCourseHourEntity after = courseHourMapper.selectByStudentId(studentId);
        writeLog(studentId, CourseHourChangeTypeEnum.CONSUME.name(), hours.negate(),
                after.getRemainingHours(), scheduleId, null, operatorId);
        return after.getRemainingHours();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void revert(Long studentId, BigDecimal hours, Long scheduleId, Long operatorId) {
        ensureAccount(studentId);
        // 回退:used -= hours,remaining += hours。
        int rows = courseHourMapper.addHours(studentId, BigDecimal.ZERO, hours.negate(), hours);
        if (rows == 0) {
            throw BusinessException.InsufficientCourseHours;
        }
        StudentCourseHourEntity after = courseHourMapper.selectByStudentId(studentId);
        writeLog(studentId, CourseHourChangeTypeEnum.REVERT.name(), hours,
                after.getRemainingHours(), scheduleId, null, operatorId);
    }

    @Override
    public PageResult<StudentCourseHourPageRes> page(StudentCourseHourPageReq req) {
        int pageNum = req.getPageNum() == null ? 1 : req.getPageNum();
        int pageSize = req.getPageSize() == null ? 10 : req.getPageSize();

        PageHelper.startPage(pageNum, pageSize);
        List<StudentCourseHourEntity> list = courseHourMapper.selectPage(req.getStudentId());
        PageInfo<StudentCourseHourEntity> pageInfo = new PageInfo<>(list);

        // 单表查出账户后,按 studentId 批量取学生姓名/编号组装,不做跨表 join。
        Map<Long, StudentEntity> studentMap = Collections.emptyMap();
        if (!list.isEmpty()) {
            List<Long> studentIds = list.stream().map(StudentCourseHourEntity::getStudentId).toList();
            studentMap = studentMapper.selectByIds(studentIds).stream()
                    .collect(Collectors.toMap(StudentEntity::getId, Function.identity()));
        }
        Map<Long, StudentEntity> finalMap = studentMap;
        List<StudentCourseHourPageRes> records = list.stream()
                .map(e -> buildPageRes(e, finalMap.get(e.getStudentId())))
                .toList();
        return new PageResult<>(pageInfo.getTotal(), pageNum, pageSize, records);
    }

    private StudentCourseHourPageRes buildPageRes(StudentCourseHourEntity entity, StudentEntity student) {
        StudentCourseHourPageRes res = new StudentCourseHourPageRes();
        res.setId(entity.getId());
        res.setStudentId(entity.getStudentId());
        res.setTotalHours(entity.getTotalHours());
        res.setUsedHours(entity.getUsedHours());
        res.setRemainingHours(entity.getRemainingHours());
        res.setUpdatedAt(entity.getUpdatedAt());
        if (student != null) {
            res.setStudentName(student.getStudentName());
            res.setStudentNo(student.getStudentNo());
        }
        return res;
    }

    /**
     * 确保学生课时账户存在,不存在则按零余额初始化。
     */
    private void ensureAccount(Long studentId) {
        if (courseHourMapper.selectByStudentId(studentId) == null) {
            StudentCourseHourEntity entity = new StudentCourseHourEntity();
            entity.setStudentId(studentId);
            entity.setTotalHours(BigDecimal.ZERO);
            entity.setUsedHours(BigDecimal.ZERO);
            entity.setRemainingHours(BigDecimal.ZERO);
            entity.setCreatedAt(new Date());
            entity.setUpdatedAt(new Date());
            courseHourMapper.insert(entity);
        }
    }

    private void writeLog(Long studentId, String changeType, BigDecimal changeHours,
                          BigDecimal remainingAfter, Long scheduleId, String reason, Long operatorId) {
        StudentCourseHourLogEntity log = new StudentCourseHourLogEntity();
        log.setStudentId(studentId);
        log.setChangeType(changeType);
        log.setChangeHours(changeHours);
        log.setRemainingAfter(remainingAfter);
        log.setScheduleId(scheduleId);
        log.setReason(reason);
        log.setOperatorId(operatorId);
        courseHourLogMapper.insert(log);
    }
}
