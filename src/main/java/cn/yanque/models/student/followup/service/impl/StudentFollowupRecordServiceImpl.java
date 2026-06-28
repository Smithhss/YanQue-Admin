package cn.yanque.models.student.followup.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.yanque.common.api.PageResult;
import cn.yanque.common.enums.ActiveEnum;
import cn.yanque.common.enums.TeachingModeEnum;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.student.followup.pojo.bo.QueryStudentFollowupRecordBo;
import cn.yanque.models.student.followup.pojo.bo.StudentFollowupRecordStatsBo;
import cn.yanque.models.student.followup.pojo.entity.StudentFollowupRecordEntity;
import cn.yanque.models.student.followup.pojo.entity.StudentFollowupTagEntity;
import cn.yanque.models.student.followup.pojo.vo.req.StudentFollowupRecordCancelReq;
import cn.yanque.models.student.followup.pojo.vo.req.StudentFollowupRecordCompleteReq;
import cn.yanque.models.student.followup.pojo.vo.req.StudentFollowupRecordPageReq;
import cn.yanque.models.student.followup.pojo.vo.res.StudentFollowupRecordDetailRes;
import cn.yanque.models.student.followup.pojo.vo.res.StudentFollowupRecordGenerateRes;
import cn.yanque.models.student.followup.pojo.vo.res.StudentFollowupRecordPageRes;
import cn.yanque.models.student.followup.pojo.vo.res.StudentFollowupRecordStatsRes;
import cn.yanque.models.student.followup.pojo.vo.res.StudentFollowupRecordUpdateRes;
import cn.yanque.models.student.followup.service.StudentFollowupRecordService;
import cn.yanque.models.student.mapper.StudentFollowupRecordMapper;
import cn.yanque.models.student.mapper.StudentFollowupTagMapper;
import cn.yanque.models.student.mapper.StudentMapper;
import cn.yanque.models.student.pojo.entity.StudentEntity;
import cn.yanque.models.users.mapper.SysUserMapper;
import cn.yanque.models.users.pojo.entity.SysUserEntity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StudentFollowupRecordServiceImpl implements StudentFollowupRecordService {

    private static final String RECORD_STATUS_NEED_FOLLOWUP = "NEED_FOLLOWUP";

    @Autowired
    private StudentFollowupRecordMapper studentFollowupRecordMapper;

    @Autowired
    private StudentFollowupTagMapper studentFollowupTagMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public PageResult<StudentFollowupRecordPageRes> page(StudentFollowupRecordPageReq req) {
        int pageNum = req.getPageNum() == null ? 1 : req.getPageNum();
        int pageSize = req.getPageSize() == null ? 10 : req.getPageSize();
        QueryStudentFollowupRecordBo query = new QueryStudentFollowupRecordBo();
        BeanUtils.copyProperties(req, query);
        PageHelper.startPage(pageNum, pageSize);
        List<StudentFollowupRecordEntity> list = studentFollowupRecordMapper.selectPage(query);
        PageInfo<StudentFollowupRecordEntity> pageInfo = new PageInfo<>(list);

        Map<Long, StudentEntity> studentMap = buildStudentMap(list);
        Map<Long, SysUserEntity> userMap = buildUserMap(list);
        List<StudentFollowupRecordPageRes> records = list.stream()
                .map(item -> buildPageRes(item, studentMap, userMap))
                .toList();
        return new PageResult<>(pageInfo.getTotal(), pageNum, pageSize, records);
    }

    @Override
    public StudentFollowupRecordStatsRes stats() {
        Date todayStart = DateUtil.beginOfDay(new Date());
        Date tomorrowStart = DateUtil.offsetDay(todayStart, 1);
        StudentFollowupRecordStatsBo stats = studentFollowupRecordMapper.selectStats(todayStart, tomorrowStart);
        return buildStatsRes(stats);
    }

    @Override
    public StudentFollowupRecordDetailRes detail(Long id) {
        StudentFollowupRecordEntity entity = getRecord(id);
        Map<Long, StudentEntity> studentMap = buildStudentMap(List.of(entity));
        Map<Long, SysUserEntity> userMap = buildUserMap(List.of(entity));
        StudentFollowupRecordDetailRes res = new StudentFollowupRecordDetailRes();
        BeanUtils.copyProperties(entity, res);
        fillDisplayFields(res, entity, studentMap, userMap);
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentFollowupRecordGenerateRes generateDueRecords(Date generateDate) {
        /*
         * 回访记录生成口径:
         * 1. 回访不依赖学习计划,直接以学生表为主数据;线上,启用,有标签的学生才进入自动生成范围。
         * 2. 如果学生上一条回访记录仍是 NEED_FOLLOWUP,说明已经有待处理任务,本次不再新增。
         * 3. 如果有历史回访记录,按上一条实际回访时间 followup_time 计算下一次回访;如果没有回访记录,则用学生创建时间作为入学时间。
         * 4. 判断规则是:基准时间 + 当前标签配置的间隔天数 <= 今天,就生成一条 NEED_FOLLOWUP 记录。
         * 5. 回访记录保存 studentTag,followupTagId,followupIntervalDays 快照,历史记录不跟随后续标签或规则修改变化。
         * 6. student_followup_record 上有 (student_id, due_date) 唯一键,insertIgnore 可以保证重复触发任务时不会重复生成。
         */
        Date today = DateUtil.beginOfDay(generateDate == null ? new Date() : generateDate);

        List<StudentEntity> students = studentMapper.selectFollowupCandidates();
        if (students.isEmpty()) {
            return buildGenerateRes(0);
        }

        // 只取启用的回访标签配置;学生当前标签不在这里,说明暂时不参与自动回访。
        Map<String, StudentFollowupTagEntity> followupTagMap = studentFollowupTagMapper.selectActiveList().stream()
                .collect(Collectors.toMap(StudentFollowupTagEntity::getStudentTag, Function.identity(), (a, b) -> a));

        // 每个学生只看最新一条记录:待回访则跳过,已回访则按 followup_time 继续计算下一次。
        Map<Long, StudentFollowupRecordEntity> latestRecordMap = buildLatestRecordMap(students.stream()
                .map(StudentEntity::getId)
                .toList());

        int generatedCount = 0;
        Date now = new Date();
        for (StudentEntity student : students) {
            // 只生成线上,启用,有标签的学生;线下学生和未打标签学生不进入回访池。
            if (!canGenerate(student)) {
                continue;
            }

            StudentFollowupRecordEntity latestRecord = latestRecordMap.get(student.getId());
            if (latestRecord != null && RECORD_STATUS_NEED_FOLLOWUP.equals(latestRecord.getStatus())) {
                continue;
            }

            // 回访周期以学生"当前标签"对应的启用规则为准;历史记录里的标签快照不影响后续生成。
            StudentFollowupTagEntity rule = followupTagMap.get(student.getStudentTag());
            if (rule == null || rule.getFollowupIntervalDays() == null || rule.getFollowupIntervalDays() <= 0) {
                continue;
            }

            Date enrollDate = DateUtil.beginOfDay(student.getCreatedAt());
            Date baseDate = latestRecord == null ? enrollDate : getLatestFollowupBaseDate(latestRecord);
            Date dueDate = DateUtil.offsetDay(DateUtil.beginOfDay(baseDate), rule.getFollowupIntervalDays());
            if (!dueDate.after(today)) {
                StudentFollowupRecordEntity record = buildRecord(student, rule, enrollDate, latestRecord, dueDate, now);
                generatedCount += studentFollowupRecordMapper.insertIgnore(record);
            }
        }
        return buildGenerateRes(generatedCount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentFollowupRecordUpdateRes complete(Long id, Long followupUserId, StudentFollowupRecordCompleteReq req) {
        if (followupUserId == null) {
            throw BusinessException.UserNotExist.newInstance("回访人不能为空");
        }
        String content = normalizeRequired(req.getFollowupContent(), "回访内容不能为空");
        String videoObjectKey = normalizeRequired(req.getFollowupVideoObjectKey(), "回访会议视频不能为空");
        String videoFileName = normalizeRequired(req.getFollowupVideoFileName(), "回访会议视频文件名不能为空");
        String remark = normalizeBlank(req.getRemark());
        int rows = studentFollowupRecordMapper.updateComplete(id, followupUserId, new Date(), content, videoObjectKey, videoFileName, remark, new Date());
        if (rows == 0) {
            throw BusinessException.DateError.newInstance("只有需要回访的记录才能标记已回访");
        }
        return buildUpdateRes(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentFollowupRecordUpdateRes cancel(Long id, StudentFollowupRecordCancelReq req) {
        int rows = studentFollowupRecordMapper.updateCancel(id, normalizeBlank(req.getRemark()), new Date());
        if (rows == 0) {
            throw BusinessException.DateError.newInstance("只有需要回访的记录才能取消");
        }
        return buildUpdateRes(id);
    }

    private StudentFollowupRecordEntity getRecord(Long id) {
        StudentFollowupRecordEntity entity = studentFollowupRecordMapper.selectById(id);
        if (entity == null) {
            throw BusinessException.DateError.newInstance("回访记录不存在");
        }
        return entity;
    }

    private boolean canGenerate(StudentEntity student) {
        return student != null
                && TeachingModeEnum.ONLINE.name().equals(student.getTeachingMode())
                && ActiveEnum.ACTIVE.name().equals(student.getStatus())
                && student.getStudentTag() != null
                && !student.getStudentTag().isBlank();
    }

    private Date getLatestFollowupBaseDate(StudentFollowupRecordEntity latestRecord) {
        if (latestRecord.getFollowupTime() != null) {
            return latestRecord.getFollowupTime();
        }
        return latestRecord.getDueDate();
    }

    private StudentFollowupRecordEntity buildRecord(StudentEntity student, StudentFollowupTagEntity rule, Date enrollDate,
                                                    StudentFollowupRecordEntity latestRecord, Date dueDate, Date now) {
        StudentFollowupRecordEntity record = new StudentFollowupRecordEntity();
        record.setStudentId(student.getId());
        record.setLearningPlanId(null);
        record.setStudentTag(student.getStudentTag());
        record.setFollowupTagId(rule.getId());
        record.setEnrollDate(enrollDate);
        record.setLastFollowupTime(latestRecord == null ? null : getLatestFollowupBaseDate(latestRecord));
        record.setDueDate(dueDate);
        record.setFollowupIntervalDays(rule.getFollowupIntervalDays());
        record.setStatus(RECORD_STATUS_NEED_FOLLOWUP);
        record.setCreatedAt(now);
        record.setUpdatedAt(now);
        return record;
    }

    private Map<Long, StudentFollowupRecordEntity> buildLatestRecordMap(List<Long> studentIds) {
        if (studentIds.isEmpty()) {
            return Map.of();
        }
        Map<Long, StudentFollowupRecordEntity> result = new LinkedHashMap<>();
        for (StudentFollowupRecordEntity record : studentFollowupRecordMapper.selectLatestByStudentIds(studentIds)) {
            result.putIfAbsent(record.getStudentId(), record);
        }
        return result;
    }

    private Map<Long, StudentEntity> buildStudentMap(List<StudentFollowupRecordEntity> records) {
        List<Long> studentIds = records.stream()
                .map(StudentFollowupRecordEntity::getStudentId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        return buildStudentMapByIds(studentIds);
    }

    private Map<Long, StudentEntity> buildStudentMapByIds(List<Long> studentIds) {
        if (studentIds.isEmpty()) {
            return Map.of();
        }
        return studentMapper.selectByIds(studentIds).stream()
                .collect(Collectors.toMap(StudentEntity::getId, Function.identity(), (a, b) -> a));
    }

    private Map<Long, SysUserEntity> buildUserMap(List<StudentFollowupRecordEntity> records) {
        List<Long> userIds = records.stream()
                .map(StudentFollowupRecordEntity::getFollowupUserId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (userIds.isEmpty()) {
            return Map.of();
        }
        return sysUserMapper.selectByIds(userIds).stream()
                .collect(Collectors.toMap(SysUserEntity::getId, Function.identity(), (a, b) -> a));
    }

    private StudentFollowupRecordPageRes buildPageRes(StudentFollowupRecordEntity entity,
                                                      Map<Long, StudentEntity> studentMap,
                                                      Map<Long, SysUserEntity> userMap) {
        StudentFollowupRecordPageRes res = new StudentFollowupRecordPageRes();
        BeanUtils.copyProperties(entity, res);
        fillDisplayFields(res, entity, studentMap, userMap);
        return res;
    }

    private void fillDisplayFields(StudentFollowupRecordPageRes res, StudentFollowupRecordEntity entity,
                                   Map<Long, StudentEntity> studentMap, Map<Long, SysUserEntity> userMap) {
        StudentEntity student = studentMap.get(entity.getStudentId());
        if (student != null) {
            res.setStudentName(student.getStudentName());
            res.setStudentPhone(student.getStudentPhone());
        }
        SysUserEntity user = entity.getFollowupUserId() == null ? null : userMap.get(entity.getFollowupUserId());
        res.setFollowupUserName(getUserShowName(user));
    }

    private void fillDisplayFields(StudentFollowupRecordDetailRes res, StudentFollowupRecordEntity entity,
                                   Map<Long, StudentEntity> studentMap, Map<Long, SysUserEntity> userMap) {
        StudentEntity student = studentMap.get(entity.getStudentId());
        if (student != null) {
            res.setStudentName(student.getStudentName());
            res.setStudentPhone(student.getStudentPhone());
        }
        SysUserEntity user = entity.getFollowupUserId() == null ? null : userMap.get(entity.getFollowupUserId());
        res.setFollowupUserName(getUserShowName(user));
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

    private StudentFollowupRecordUpdateRes buildUpdateRes(Long id) {
        StudentFollowupRecordUpdateRes res = new StudentFollowupRecordUpdateRes();
        res.setId(id);
        return res;
    }

    private StudentFollowupRecordGenerateRes buildGenerateRes(Integer generatedCount) {
        StudentFollowupRecordGenerateRes res = new StudentFollowupRecordGenerateRes();
        res.setGeneratedCount(generatedCount);
        return res;
    }

    private StudentFollowupRecordStatsRes buildStatsRes(StudentFollowupRecordStatsBo stats) {
        long totalCount = nullToZero(stats == null ? null : stats.getTotalCount());
        long completedCount = nullToZero(stats == null ? null : stats.getCompletedCount());
        StudentFollowupRecordStatsRes res = new StudentFollowupRecordStatsRes();
        res.setTotalCount(totalCount);
        res.setTodayNeedFollowupCount(nullToZero(stats == null ? null : stats.getTodayNeedFollowupCount()));
        res.setTodayFollowedCount(nullToZero(stats == null ? null : stats.getTodayFollowedCount()));
        res.setCompletedCount(completedCount);
        res.setCompletionRate(totalCount == 0
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(completedCount)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(totalCount), 1, RoundingMode.HALF_UP));
        return res;
    }

    private long nullToZero(Long value) {
        return value == null ? 0L : value;
    }

    private String normalizeRequired(String value, String message) {
        String normalized = normalizeBlank(value);
        if (normalized == null) {
            throw BusinessException.DateError.newInstance(message);
        }
        return normalized;
    }

    private String normalizeBlank(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
