package cn.yanque.models.teaching.homework.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.yanque.common.api.PageResult;
import cn.yanque.common.dataConfig.service.SysConfig;
import cn.yanque.common.dataConfig.service.SysConfigService;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.config.TosProperties;
import cn.yanque.models.teaching.clazz.mapper.ClazzMapper;
import cn.yanque.models.teaching.clazz.pojo.entity.ClazzEntity;
import cn.yanque.models.teaching.homework.mapper.HomeworkMapper;
import cn.yanque.models.teaching.homework.pojo.bo.QueryHomeworkBo;
import cn.yanque.models.teaching.homework.pojo.entity.HomeworkEntity;
import cn.yanque.models.teaching.homework.pojo.vo.req.HomeworkCreateReq;
import cn.yanque.models.teaching.homework.pojo.vo.req.HomeworkPageReq;
import cn.yanque.models.teaching.homework.pojo.vo.req.HomeworkPrepareReq;
import cn.yanque.models.teaching.homework.pojo.vo.req.HomeworkUploadSignReq;
import cn.yanque.models.teaching.homework.pojo.vo.res.HomeworkContentRes;
import cn.yanque.models.teaching.homework.pojo.vo.res.HomeworkCreateRes;
import cn.yanque.models.teaching.homework.pojo.vo.res.HomeworkPageRes;
import cn.yanque.models.teaching.homework.pojo.vo.res.HomeworkPrepareRes;
import cn.yanque.models.teaching.homework.pojo.vo.res.HomeworkUploadSignRes;
import cn.yanque.models.teaching.homework.service.HomeworkService;
import cn.yanque.models.teaching.schedule.mapper.ClassScheduleMapper;
import cn.yanque.models.teaching.schedule.pojo.entity.ClassScheduleEntity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.comm.HttpMethod;
import com.volcengine.tos.model.object.PreSignedURLInput;
import com.volcengine.tos.model.object.PreSignedURLOutput;
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
public class HomeworkServiceImpl implements HomeworkService {

    private static final String MARKDOWN_CONTENT_TYPE = "text/markdown; charset=utf-8";

    @Autowired
    private HomeworkMapper homeworkMapper;

    @Autowired
    private ClazzMapper clazzMapper;

    @Autowired
    private ClassScheduleMapper classScheduleMapper;

    @Autowired
    private TosProperties tosProperties;

    @Autowired
    private SysConfigService sysConfigService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HomeworkCreateRes addHomework(HomeworkCreateReq req) {
        ClazzEntity clazz = validateClass(req.getClassId());
        Date homeworkDate = DateUtil.beginOfDay(req.getHomeworkDate());
        validateHomeworkNotExists(req.getClassId(), homeworkDate);
        ClassScheduleEntity schedule = getSchedule(req.getClassId(), homeworkDate);
        if (req.getDeadline().before(req.getStartTime())) {
            throw BusinessException.DateError.newInstance("截止时间不能早于开始时间");
        }

        HomeworkEntity homework = new HomeworkEntity();
        BeanUtils.copyProperties(req, homework);
        homework.setHomeworkDate(homeworkDate);
        homework.setClassContent(schedule.getCourseContent());
        homework.setCreatedAt(new Date());
        homework.setUpdatedAt(new Date());
        homeworkMapper.insert(homework);

        HomeworkCreateRes res = new HomeworkCreateRes();
        res.setId(homework.getId());
        return res;
    }

    @Override
    public PageResult<HomeworkPageRes> pageHomework(HomeworkPageReq req) {
        int pageNum = req.getPageNum() == null ? 1 : req.getPageNum();
        int pageSize = req.getPageSize() == null ? 10 : req.getPageSize();
        QueryHomeworkBo query = new QueryHomeworkBo();
        BeanUtils.copyProperties(req, query);
        PageHelper.startPage(pageNum, pageSize);
        List<HomeworkEntity> list = homeworkMapper.selectPage(query);
        PageInfo<HomeworkEntity> pageInfo = new PageInfo<>(list);
        Map<Long, String> classPeriodMap = buildClassPeriodMap(list);
        List<HomeworkPageRes> records = list.stream().map(item -> buildPageRes(item, classPeriodMap)).toList();
        return new PageResult<>(pageInfo.getTotal(), pageNum, pageSize, records);
    }

    @Override
    public HomeworkPrepareRes prepareHomework(HomeworkPrepareReq req) {
        ClazzEntity clazz = validateClass(req.getClassId());
        // 作业按“日期”维度发布，先归一到当天零点，避免时间部分影响查重和课表匹配。
        Date homeworkDate = DateUtil.beginOfDay(req.getHomeworkDate());
        // 同一班级同一天只允许发布一份作业，预填阶段提前拦截，减少老师无效上传。
        validateHomeworkNotExists(req.getClassId(), homeworkDate);
        // 课程内容以当天课表快照为准，前端只负责展示和确认，不让老师手动维护这份关联数据。
        ClassScheduleEntity schedule = getSchedule(req.getClassId(), homeworkDate);

        HomeworkPrepareRes res = new HomeworkPrepareRes();
        res.setClassId(req.getClassId());
        res.setClassPeriod(clazz.getClassPeriod());
        res.setHomeworkDate(homeworkDate);
        res.setClassContent(schedule.getCourseContent());
        res.setDefaultTitle(clazz.getClassPeriod() + " " + DateUtil.format(homeworkDate, DatePattern.NORM_DATE_PATTERN) + " 作业");
        res.setStartTime(DateUtil.beginOfDay(homeworkDate));
        res.setDeadline(DateUtil.endOfDay(homeworkDate));
        return res;
    }

    @Override
    public HomeworkUploadSignRes createContentUploadSign(HomeworkUploadSignReq req) {
        validateClass(req.getClassId());
        String fileName = req.getFileName().trim();
        if (!fileName.toLowerCase().endsWith(".md")) {
            throw BusinessException.DateError.newInstance("作业内容只支持md格式");
        }
        validateTosConfig();

        String contentFileName = fileName;
        String objectKey = "homework/" + req.getClassId() + "/" + DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN)
                + "/" + IdUtil.fastSimpleUUID() + ".md";
        Long uploadExpireSeconds = getUploadExpireSeconds();
        Map<String, String> headers = Map.of("Content-Type", MARKDOWN_CONTENT_TYPE);
        TOSV2 tos = createTosClient();
        PreSignedURLInput input = PreSignedURLInput.builder()
                .bucket(tosProperties.getBucket())
                .key(objectKey)
                .httpMethod(HttpMethod.PUT)
                .expires(uploadExpireSeconds)
                .header(headers)
                .build();
        PreSignedURLOutput output = tos.preSignedURL(input);

        HomeworkUploadSignRes res = new HomeworkUploadSignRes();
        res.setUploadUrl(output.getSignedUrl());
        res.setContentObjectKey(objectKey);
        res.setContentFileName(contentFileName);
        res.setExpires(uploadExpireSeconds);
        res.setHeaders(output.getSignedHeader() == null || output.getSignedHeader().isEmpty() ? headers : output.getSignedHeader());
        return res;
    }

    @Override
    public HomeworkContentRes getHomeworkContent(Long id) {
        HomeworkEntity homework = homeworkMapper.selectById(id);
        if (homework == null) {
            throw BusinessException.DateError.newInstance("作业不存在");
        }
        validateTosConfig();
        Long previewExpireSeconds = getPreviewExpireSeconds();
        PreSignedURLInput input = PreSignedURLInput.builder()
                .bucket(tosProperties.getBucket())
                .key(homework.getContentObjectKey())
                .httpMethod(HttpMethod.GET)
                .expires(previewExpireSeconds)
                .build();
        PreSignedURLOutput output = createTosClient().preSignedURL(input);

        HomeworkContentRes res = new HomeworkContentRes();
        res.setContentFileName(homework.getContentFileName());
        res.setPreviewUrl(output.getSignedUrl());
        res.setExpires(previewExpireSeconds);
        return res;
    }

    private Long getUploadExpireSeconds() {
        Long uploadExpireSeconds = sysConfigService.get(SysConfig.tosUploadExpireSeconds);
        if (uploadExpireSeconds == null || uploadExpireSeconds <= 0) {
            throw BusinessException.DateError.newInstance("TOS上传签名过期时间配置错误");
        }
        return uploadExpireSeconds;
    }

    private Long getPreviewExpireSeconds() {
        Long previewExpireSeconds = sysConfigService.get(SysConfig.tosPreviewExpireSeconds);
        if (previewExpireSeconds == null || previewExpireSeconds <= 0) {
            throw BusinessException.DateError.newInstance("TOS预览签名过期时间配置错误");
        }
        return previewExpireSeconds;
    }

    private ClazzEntity validateClass(Long classId) {
        ClazzEntity clazz = clazzMapper.selectById(classId);
        if (clazz == null) {
            throw BusinessException.DateError.newInstance("班级不存在");
        }
        return clazz;
    }

    private ClassScheduleEntity getSchedule(Long classId, Date homeworkDate) {
        List<ClassScheduleEntity> schedules = classScheduleMapper.selectByClassIdAndDate(
                classId,
                DateUtil.beginOfDay(homeworkDate),
                DateUtil.endOfDay(homeworkDate)
        );
        if (schedules.isEmpty()) {
            throw BusinessException.DateError.newInstance("该班级当天没有课表，无法发布作业");
        }
        return schedules.get(0);
    }

    private void validateHomeworkNotExists(Long classId, Date homeworkDate) {
        if (homeworkMapper.selectByClassIdAndHomeworkDate(classId, homeworkDate) != null) {
            throw BusinessException.DateError.newInstance("该班级当天作业已存在");
        }
    }

    private void validateTosConfig() {
        if (isBlank(tosProperties.getEndpoint()) || isBlank(tosProperties.getRegion()) || isBlank(tosProperties.getBucket())
                || isBlank(tosProperties.getAccessKey()) || isBlank(tosProperties.getSecretKey())) {
            throw BusinessException.DateError.newInstance("TOS配置不完整");
        }
    }

    private TOSV2 createTosClient() {
        return new TOSV2ClientBuilder().build(tosProperties.getRegion(), tosProperties.getEndpoint(),
                tosProperties.getAccessKey(), tosProperties.getSecretKey());
    }

    private Map<Long, String> buildClassPeriodMap(List<HomeworkEntity> homeworks) {
        List<Long> classIds = homeworks.stream()
                .map(HomeworkEntity::getClassId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (classIds.isEmpty()) {
            return Map.of();
        }
        return clazzMapper.selectByIds(classIds).stream()
                .collect(Collectors.toMap(ClazzEntity::getId, ClazzEntity::getClassPeriod));
    }

    private HomeworkPageRes buildPageRes(HomeworkEntity homework, Map<Long, String> classPeriodMap) {
        HomeworkPageRes res = new HomeworkPageRes();
        BeanUtils.copyProperties(homework, res);
        if (isBlank(res.getContentFileName())) {
            res.setContentFileName(getFileNameFromObjectKey(homework.getContentObjectKey()));
        }
        res.setClassPeriod(classPeriodMap.get(homework.getClassId()));
        return res;
    }

    private String getFileNameFromObjectKey(String objectKey) {
        if (isBlank(objectKey)) {
            return "";
        }
        int index = objectKey.lastIndexOf("/");
        return index < 0 ? objectKey : objectKey.substring(index + 1);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
