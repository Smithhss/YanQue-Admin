package cn.yanque.models.teaching.course.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.yanque.common.api.PageResult;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.teaching.course.mapper.CourseDetailMapper;
import cn.yanque.models.teaching.course.mapper.CourseHomeworkTemplateMapper;
import cn.yanque.models.teaching.course.mapper.CourseMapper;
import cn.yanque.models.teaching.course.pojo.Info.CourseImportInfo;
import cn.yanque.models.teaching.course.pojo.entity.CourseDetailEntity;
import cn.yanque.models.teaching.course.pojo.entity.CourseEntity;
import cn.yanque.models.teaching.course.pojo.vo.req.CourseCreateReq;
import cn.yanque.models.teaching.course.pojo.vo.req.CourseDetailCreateReq;
import cn.yanque.models.teaching.course.pojo.vo.req.CourseDetailUpdateReq;
import cn.yanque.models.teaching.course.pojo.vo.req.CoursePageReq;
import cn.yanque.models.teaching.course.pojo.vo.req.CourseUpdateReq;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseCreateRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseDetailCreateRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseDetailDeleteRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseDetailItemRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseDetailUpdateRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseDeleteRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseDetailRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CoursePageRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseUpdateRes;
import cn.yanque.models.teaching.course.service.CourseService;
import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private static final String TEACHING_MODE_ONLINE = "ONLINE";
    private static final String TEACHING_MODE_OFFLINE = "OFFLINE";

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseDetailMapper courseDetailMapper;

    @Autowired
    private CourseHomeworkTemplateMapper courseHomeworkTemplateMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CourseCreateRes addCourse(CourseCreateReq req) {
        CourseEntity course = new CourseEntity();

        course.setCourseName(req.getCourseName());
        course.setCourseDays(req.getCourseDays());
        course.setTeachingMode(validateTeachingMode(req.getTeachingMode()));
        course.setMaterialPath(req.getMaterialPath());
        course.setCreatedAt(new Date());
        course.setUpdatedAt(new Date());
        courseMapper.insert(course);

        CourseCreateRes res = new CourseCreateRes();
        res.setId(course.getId());
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CourseUpdateRes updateCourse(CourseUpdateReq req) {
        CourseEntity course = new CourseEntity();
        course.setId(req.getId());
        course.setCourseName(req.getCourseName());
        course.setCourseDays(req.getCourseDays());
        course.setTeachingMode(validateTeachingMode(req.getTeachingMode()));
        course.setMaterialPath(req.getMaterialPath());
        course.setUpdatedAt(new Date());

        int rows = courseMapper.updateById(course);
        if (rows == 0) {
            throw BusinessException.CourseNotExist;
        }

        CourseUpdateRes res = new CourseUpdateRes();
        res.setId(req.getId());
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CourseDeleteRes deleteCourse(Long id) {
        // 课程是主表，课程详情和作业标准都是从表；删除课程时先清理从表，避免留下无效数据。
        courseHomeworkTemplateMapper.deleteByCourseId(id);
        courseDetailMapper.deleteByCourseId(id);
        int rows = courseMapper.deleteById(id);
        if (rows == 0) {
            throw BusinessException.CourseNotExist;
        }

        CourseDeleteRes res = new CourseDeleteRes();
        res.setId(id);
        return res;
    }

    @Override
    public CourseDetailRes getCourseById(Long id) {
        CourseEntity course = courseMapper.selectById(id);
        if (course == null) {
            throw BusinessException.CourseNotExist;
        }
        return buildCourseDetailRes(course);
    }

    @Override
    public PageResult<CoursePageRes> pageCourse(CoursePageReq req) {
        // PageHelper 会拦截紧跟在 startPage 后面的第一条查询 SQL，并自动追加分页。
        int pageNum = req.getPageNum() == null ? 1 : req.getPageNum();
        int pageSize = req.getPageSize() == null ? 10 : req.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<CourseEntity> list = courseMapper.selectPage(req.getKeyword());
        PageInfo<CourseEntity> pageInfo = new PageInfo<>(list);
        List<CoursePageRes> records = list.stream().map(this::buildCoursePageRes).toList();
        return new PageResult<>(pageInfo.getTotal(), pageNum, pageSize, records);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CourseDetailCreateRes addCourseDetail(Long courseId, CourseDetailCreateReq req) {
        // 详情必须挂在一个已存在的课程下面，先检查课程是否存在。
        CourseEntity course = courseMapper.selectById(courseId);
        if (course == null) {
            throw BusinessException.CourseNotExist;
        }

        CourseDetailEntity detail = new CourseDetailEntity();
        detail.setCourseId(courseId);
        fillCourseDetailFields(detail, course, req.getStageName(), req.getDayNumber(), req.getClassContent(), null, true);
        courseDetailMapper.insert(detail);

        CourseDetailCreateRes res = new CourseDetailCreateRes();
        res.setId(detail.getId());
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CourseDetailUpdateRes updateCourseDetail(CourseDetailUpdateReq req) {
        CourseDetailEntity existing = courseDetailMapper.selectById(req.getId());
        if (existing == null) {
            throw BusinessException.CourseDetailNotExist;
        }
        CourseEntity course = courseMapper.selectById(existing.getCourseId());
        if (course == null) {
            throw BusinessException.CourseNotExist;
        }

        CourseDetailEntity detail = new CourseDetailEntity();
        detail.setId(req.getId());
        detail.setCourseId(existing.getCourseId());
        fillCourseDetailFields(detail, course, req.getStageName(), req.getDayNumber(), req.getClassContent(), req.getId(), true);

        int rows = courseDetailMapper.updateById(detail);
        if (rows == 0) {
            throw BusinessException.CourseDetailNotExist;
        }

        CourseDetailUpdateRes res = new CourseDetailUpdateRes();
        res.setId(req.getId());
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CourseDetailDeleteRes deleteCourseDetail(Long id) {
        int rows = courseDetailMapper.deleteById(id);
        if (rows == 0) {
            throw BusinessException.CourseDetailNotExist;
        }

        CourseDetailDeleteRes res = new CourseDetailDeleteRes();
        res.setId(id);
        return res;
    }

    @Override
    public CourseDetailItemRes getCourseDetailById(Long id) {
        CourseDetailEntity detail = courseDetailMapper.selectById(id);
        if (detail == null) {
            throw BusinessException.CourseDetailNotExist;
        }
        return buildCourseDetailItemRes(detail);
    }

    @Override
    public List<CourseDetailItemRes> listCourseDetails(Long courseId) {
        // 先校验课程，再返回该课程下的阶段/天数/上课内容列表。
        CourseEntity course = courseMapper.selectById(courseId);
        if (course == null) {
            throw BusinessException.CourseNotExist;
        }
        return courseDetailMapper.selectByCourseId(courseId).stream().map(this::buildCourseDetailItemRes).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importClazzDetail(Long courseId, MultipartFile file) throws IOException {

        CourseEntity courseEntity = courseMapper.selectById(courseId);
        if (courseEntity == null) {
            throw BusinessException.CourseNotExist;
        }

        List<CourseImportInfo> dataList = EasyExcel.read(file.getInputStream(), CourseImportInfo.class, null).sheet().doReadSync();
        if (dataList.isEmpty()) {
            throw BusinessException.DateError.newInstance("导入文件不能为空");
        }

        List<CourseDetailEntity> courseDetailEntityList = new ArrayList<>();
        List<String> existStageList = new ArrayList<>();
        String curStage = "";
        for (int i = 0; i < dataList.size(); i++) {
            CourseImportInfo info = dataList.get(i);

            checkImportData(courseEntity, info, i);

            if (TEACHING_MODE_ONLINE.equals(courseEntity.getTeachingMode())) {
                if (existStageList.contains(info.getStageName())) {
                    throw BusinessException.DateExist.newInstance("第" + (i + 1) + "行阶段重复");
                }
                existStageList.add(info.getStageName());
            }

            if (TEACHING_MODE_OFFLINE.equals(courseEntity.getTeachingMode()) && existStageList.contains(info.getStageName())) {
                throw BusinessException.DateError.newInstance("第" + (i + 1) + "行阶段不连续");
            }

            if (TEACHING_MODE_OFFLINE.equals(courseEntity.getTeachingMode()) && StrUtil.isEmpty(curStage)) {
                curStage = info.getStageName();
            }

            if (TEACHING_MODE_OFFLINE.equals(courseEntity.getTeachingMode()) && StrUtil.isNotEmpty(curStage) && !curStage.equals(info.getStageName())){
                existStageList.add(curStage);
                curStage = info.getStageName();
            }


            CourseDetailEntity courseDetailEntity = new CourseDetailEntity();
            BeanUtils.copyProperties(info, courseDetailEntity);
            courseDetailEntity.setCourseId(courseId);
            fillCourseDetailFields(courseDetailEntity, courseEntity, info.getStageName(), info.getDayNumber(), info.getClassContent(), null, false);

            courseDetailEntityList.add(courseDetailEntity);
        }
        courseDetailMapper.deleteByCourseId(courseId);
        courseDetailMapper.batchInsert(courseDetailEntityList);

    }

    private void checkImportData(CourseEntity course, CourseImportInfo info, int row) {
        if (StrUtil.isEmpty(info.getStageName())) {
            throw BusinessException.DateError.newInstance("第" + (row + 1) + "行阶段为空");
        }

        if (TEACHING_MODE_OFFLINE.equals(course.getTeachingMode())
                && (info.getDayNumber() == null || StrUtil.isEmpty(info.getClassContent()))) {
            throw BusinessException.DateError.newInstance("第" + (row + 1) + "行数据有字段为空");
        }

        if (TEACHING_MODE_OFFLINE.equals(course.getTeachingMode()) && row + 1 != info.getDayNumber()) {
            throw BusinessException.DateError.newInstance("第" + (row + 1) + "行天数有误");
        }
    }

    private void fillCourseDetailFields(CourseDetailEntity detail, CourseEntity course, String stageName,
                                        Integer dayNumber, String classContent, Long excludeId) {
        fillCourseDetailFields(detail, course, stageName, dayNumber, classContent, excludeId, true);
    }

    private void fillCourseDetailFields(CourseDetailEntity detail, CourseEntity course, String stageName,
                                        Integer dayNumber, String classContent, Long excludeId, boolean checkExistingDuplicate) {
        if (StrUtil.isEmpty(stageName)) {
            throw BusinessException.DateError.newInstance("阶段不能为空");
        }
        detail.setStageName(stageName.trim());

        // 线上课程详情只维护阶段；天数和每日内容属于线下课程维度。
        if (TEACHING_MODE_ONLINE.equals(course.getTeachingMode())) {
            detail.setDayNumber(null);
            detail.setClassContent(null);
            if (checkExistingDuplicate) {
                checkOnlineStageDuplicate(course.getId(), detail.getStageName(), excludeId);
            }
            return;
        }

        if (TEACHING_MODE_OFFLINE.equals(course.getTeachingMode())) {
            if (dayNumber == null) {
                throw BusinessException.DateError.newInstance("线下课程第几天不能为空");
            }
            if (StrUtil.isEmpty(classContent)) {
                throw BusinessException.DateError.newInstance("线下课程上课内容不能为空");
            }
            detail.setDayNumber(dayNumber);
            detail.setClassContent(classContent.trim());
            if (checkExistingDuplicate) {
                checkOfflineDayDuplicate(course.getId(), dayNumber, excludeId);
            }
            return;
        }

        throw BusinessException.DateError.newInstance("课程上课方式异常");
    }

    private void checkOnlineStageDuplicate(Long courseId, String stageName, Long excludeId) {
        boolean exists = courseDetailMapper.selectByCourseId(courseId).stream()
                .anyMatch(item -> item.getStageName().equals(stageName) && !item.getId().equals(excludeId));
        if (exists) {
            throw BusinessException.DateExist.newInstance("该线上课程阶段已存在");
        }
    }

    private void checkOfflineDayDuplicate(Long courseId, Integer dayNumber, Long excludeId) {
        boolean exists = courseDetailMapper.selectByCourseId(courseId).stream()
                .anyMatch(item -> dayNumber.equals(item.getDayNumber()) && !item.getId().equals(excludeId));
        if (exists) {
            throw BusinessException.DateExist.newInstance("该线下课程天数已存在");
        }
    }

    private CourseDetailRes buildCourseDetailRes(CourseEntity course) {
        CourseDetailRes res = new CourseDetailRes();
        BeanUtils.copyProperties(course, res);
        return res;
    }

    private CoursePageRes buildCoursePageRes(CourseEntity course) {
        CoursePageRes res = new CoursePageRes();
        BeanUtils.copyProperties(course, res);
        return res;
    }

    private String validateTeachingMode(String teachingMode) {
        if (teachingMode == null || teachingMode.isBlank()) {
            throw BusinessException.DateError.newInstance("上课方式不能为空");
        }
        String normalizedTeachingMode = teachingMode.trim();
        if (!TEACHING_MODE_ONLINE.equals(normalizedTeachingMode) && !TEACHING_MODE_OFFLINE.equals(normalizedTeachingMode)) {
            throw BusinessException.DateError.newInstance("上课方式只能是ONLINE或OFFLINE");
        }
        return normalizedTeachingMode;
    }

    private CourseDetailItemRes buildCourseDetailItemRes(CourseDetailEntity detail) {
        CourseDetailItemRes res = new CourseDetailItemRes();
        BeanUtils.copyProperties(detail, res);
        return res;
    }
}
