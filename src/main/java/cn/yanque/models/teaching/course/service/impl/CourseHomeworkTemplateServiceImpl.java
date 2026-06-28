package cn.yanque.models.teaching.course.service.impl;

import cn.yanque.common.enums.ActiveEnum;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.teaching.course.mapper.CourseDetailMapper;
import cn.yanque.models.teaching.course.mapper.CourseHomeworkTemplateMapper;
import cn.yanque.models.teaching.course.mapper.CourseMapper;
import cn.yanque.models.teaching.course.pojo.bo.QueryCourseHomeworkTemplateBo;
import cn.yanque.models.teaching.course.pojo.entity.CourseDetailEntity;
import cn.yanque.models.teaching.course.pojo.entity.CourseEntity;
import cn.yanque.models.teaching.course.pojo.entity.CourseHomeworkTemplateEntity;
import cn.yanque.models.teaching.course.pojo.vo.req.CourseHomeworkTemplateCreateReq;
import cn.yanque.models.teaching.course.pojo.vo.req.CourseHomeworkTemplateUpdateReq;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseHomeworkTemplateCreateRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseHomeworkTemplateDeleteRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseHomeworkTemplateItemRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseHomeworkTemplateUpdateRes;
import cn.yanque.models.teaching.course.service.CourseHomeworkTemplateService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CourseHomeworkTemplateServiceImpl implements CourseHomeworkTemplateService {

    private static final String TEACHING_MODE_ONLINE = "ONLINE";
    private static final String TEACHING_MODE_OFFLINE = "OFFLINE";
    private static final String COURSE_HOMEWORK_TEMPLATE_PREFIX = "course/homework-template/";

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseDetailMapper courseDetailMapper;

    @Autowired
    private CourseHomeworkTemplateMapper templateMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CourseHomeworkTemplateCreateRes addTemplate(Long courseId, CourseHomeworkTemplateCreateReq req) {
        CourseEntity course = getCourse(courseId);

        CourseHomeworkTemplateEntity template = new CourseHomeworkTemplateEntity();
        template.setCourseId(courseId);
        template.setTeachingMode(course.getTeachingMode());
        fillTemplateFields(template, course, req.getStageName(), req.getDayNumber(), req.getContentObjectKey(),
                req.getContentFileName(), req.getStatus(), req.getRemark(), null);
        template.setCreatedAt(new Date());
        template.setUpdatedAt(new Date());

        templateMapper.insert(template);

        CourseHomeworkTemplateCreateRes res = new CourseHomeworkTemplateCreateRes();
        res.setId(template.getId());
        return res;
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public CourseHomeworkTemplateUpdateRes updateTemplate(CourseHomeworkTemplateUpdateReq req) {
        CourseHomeworkTemplateEntity existing = templateMapper.selectById(req.getId());
        if (existing == null) {
            throw BusinessException.DateError.newInstance("课程作业标准不存在");
        }
        CourseEntity course = getCourse(existing.getCourseId());

        CourseHomeworkTemplateEntity template = new CourseHomeworkTemplateEntity();
        template.setId(req.getId());
        template.setCourseId(existing.getCourseId());
        template.setTeachingMode(course.getTeachingMode());
        fillTemplateFields(template, course, req.getStageName(), req.getDayNumber(), req.getContentObjectKey(),
                req.getContentFileName(), req.getStatus(), req.getRemark(), req.getId());
        template.setUpdatedAt(new Date());

        int rows = templateMapper.updateById(template);
        if (rows == 0) {
            throw BusinessException.DateError.newInstance("课程作业标准不存在");
        }

        CourseHomeworkTemplateUpdateRes res = new CourseHomeworkTemplateUpdateRes();
        res.setId(req.getId());
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CourseHomeworkTemplateDeleteRes deleteTemplate(Long id) {
        int rows = templateMapper.deleteById(id);
        if (rows == 0) {
            throw BusinessException.DateError.newInstance("课程作业标准不存在");
        }
        CourseHomeworkTemplateDeleteRes res = new CourseHomeworkTemplateDeleteRes();
        res.setId(id);
        return res;
    }

    @Override
    public CourseHomeworkTemplateItemRes getTemplateById(Long id) {
        CourseHomeworkTemplateEntity template = templateMapper.selectById(id);
        if (template == null) {
            throw BusinessException.DateError.newInstance("课程作业标准不存在");
        }
        return buildItemRes(template);
    }

    @Override
    public List<CourseHomeworkTemplateItemRes> listTemplates(Long courseId) {
        getCourse(courseId);
        QueryCourseHomeworkTemplateBo bo = new QueryCourseHomeworkTemplateBo();
        bo.setCourseId(courseId);
        return templateMapper.selectList(bo).stream().map(this::buildItemRes).toList();
    }

    private CourseEntity getCourse(Long courseId) {
        CourseEntity course = courseMapper.selectById(courseId);
        if (course == null) {
            throw BusinessException.CourseNotExist;
        }
        return course;
    }

    private void fillTemplateFields(CourseHomeworkTemplateEntity template, CourseEntity course, String stageName,
                                    Integer dayNumber, String contentObjectKey, String contentFileName, String status,
                                    String remark, Long excludeId) {
        template.setContentObjectKey(validateContentObjectKey(contentObjectKey));
        template.setContentFileName(contentFileName);
        template.setStatus(validateStatus(status));
        template.setRemark(remark);

        // 线上按阶段配置训练集,线下按天配置训练集,两个维度不能混用。
        if (TEACHING_MODE_ONLINE.equals(course.getTeachingMode())) {
            template.setStageName(validateStageName(course.getId(), stageName));
            template.setDayNumber(null);
        } else if (TEACHING_MODE_OFFLINE.equals(course.getTeachingMode())) {
            template.setStageName(null);
            template.setDayNumber(validateDayNumber(course.getId(), dayNumber, course.getCourseDays()));
        } else {
            throw BusinessException.DateError.newInstance("课程上课方式异常");
        }

        checkDuplicate(template, excludeId);
    }

    private String validateStageName(Long courseId, String stageName) {
        if (stageName == null || stageName.isBlank()) {
            throw BusinessException.DateError.newInstance("线上课程必须选择阶段");
        }
        String normalizedStageName = stageName.trim();
        // 阶段来源以课程详情为准,避免作业标准配置到课程不存在的阶段上。
        boolean exists = courseDetailMapper.selectByCourseId(courseId).stream()
                .map(CourseDetailEntity::getStageName)
                .anyMatch(normalizedStageName::equals);
        if (!exists) {
            throw BusinessException.DateError.newInstance("该课程详情中不存在所选阶段");
        }
        return normalizedStageName;
    }

    private Integer validateDayNumber(Long courseId, Integer dayNumber, Integer courseDays) {
        if (dayNumber == null) {
            throw BusinessException.DateError.newInstance("线下课程必须填写第几天");
        }
        if (dayNumber <= 0 || (courseDays != null && dayNumber > courseDays)) {
            throw BusinessException.DateError.newInstance("第几天必须在课程天数范围内");
        }
        // 线下课程按天配置,也必须落在课程详情已经维护过的天数上。
        boolean exists = courseDetailMapper.selectByCourseId(courseId).stream()
                .map(CourseDetailEntity::getDayNumber)
                .anyMatch(dayNumber::equals);
        if (!exists) {
            throw BusinessException.DateError.newInstance("该课程详情中不存在所选天数");
        }
        return dayNumber;
    }

    private String validateStatus(String status) {
        try {
            String normalizedStatus = status == null ? null : status.trim();
            ActiveEnum.valueOf(normalizedStatus);
            return normalizedStatus;
        } catch (Exception e) {
            throw BusinessException.DateError.newInstance("状态只能是ACTIVE或INACTIVE");
        }
    }

    private String validateContentObjectKey(String contentObjectKey) {
        if (contentObjectKey == null || contentObjectKey.isBlank()) {
            throw BusinessException.DateError.newInstance("作业标准文件不能为空");
        }
        String normalizedObjectKey = contentObjectKey.trim();
        // 课程作业标准只允许保存本业务目录下的 md 文档,防止误关联其他业务文件。
        if (!normalizedObjectKey.startsWith(COURSE_HOMEWORK_TEMPLATE_PREFIX) || !normalizedObjectKey.toLowerCase().endsWith(".md")) {
            throw BusinessException.DateError.newInstance("作业标准文件路径不合法");
        }
        return normalizedObjectKey;
    }

    private void checkDuplicate(CourseHomeworkTemplateEntity template, Long excludeId) {
        QueryCourseHomeworkTemplateBo bo = new QueryCourseHomeworkTemplateBo();
        bo.setCourseId(template.getCourseId());
        bo.setTeachingMode(template.getTeachingMode());
        bo.setStageName(template.getStageName());
        bo.setDayNumber(template.getDayNumber());
        bo.setExcludeId(excludeId);

        CourseHomeworkTemplateEntity duplicate = templateMapper.selectDuplicate(bo);
        if (duplicate != null) {
            if (TEACHING_MODE_ONLINE.equals(template.getTeachingMode())) {
                throw BusinessException.DateExist.newInstance("该课程阶段已配置作业标准");
            }
            throw BusinessException.DateExist.newInstance("该课程第" + template.getDayNumber() + "天已配置作业标准");
        }
    }

    private CourseHomeworkTemplateItemRes buildItemRes(CourseHomeworkTemplateEntity template) {
        CourseHomeworkTemplateItemRes res = new CourseHomeworkTemplateItemRes();
        BeanUtils.copyProperties(template, res);
        return res;
    }
}
