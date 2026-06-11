package cn.yanque.models.teaching.course.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.yanque.common.api.PageResult;
import cn.yanque.common.enums.StageNameEnum;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.teaching.course.mapper.CourseDetailMapper;
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

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseDetailMapper courseDetailMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CourseCreateRes addCourse(CourseCreateReq req) {
        CourseEntity course = new CourseEntity();
        course.setCourseName(req.getCourseName());
        course.setCourseDays(req.getCourseDays());
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
        // 课程是主表，课程详情是从表；删除课程时先清理明细，避免留下无效数据。
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
        detail.setStageName(req.getStageName());
        detail.setDayNumber(req.getDayNumber());
        detail.setClassContent(req.getClassContent());
        courseDetailMapper.insert(detail);

        CourseDetailCreateRes res = new CourseDetailCreateRes();
        res.setId(detail.getId());
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CourseDetailUpdateRes updateCourseDetail(CourseDetailUpdateReq req) {
        CourseDetailEntity detail = new CourseDetailEntity();
        detail.setId(req.getId());
        detail.setStageName(req.getStageName());
        detail.setDayNumber(req.getDayNumber());
        detail.setClassContent(req.getClassContent());

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

            checkImportData(info, i);

            if (existStageList.contains(info.getStageName())) {
                throw BusinessException.DateError.newInstance("第" + (i + 1) + "行阶段不连续");
            }

            if (StrUtil.isEmpty(curStage)) {
                curStage = info.getStageName();
            }

            if (StrUtil.isNotEmpty(curStage) && !curStage.equals(info.getStageName())){
                existStageList.add(curStage);
                curStage = info.getStageName();
            }


            CourseDetailEntity courseDetailEntity = new CourseDetailEntity();
            BeanUtils.copyProperties(info, courseDetailEntity);
            courseDetailEntity.setCourseId(courseId);

            courseDetailEntityList.add(courseDetailEntity);
        }
        courseDetailMapper.deleteByCourseId(courseId);
        courseDetailMapper.batchInsert(courseDetailEntityList);

    }

    private void checkImportData(CourseImportInfo info, int row) {
        if (StrUtil.isEmpty(info.getStageName()) || info.getDayNumber() == null || StrUtil.isEmpty(info.getClassContent())) {
            throw BusinessException.DateError.newInstance("第" + row +1 + "行数据有字段为空");
        }

        try {
            StageNameEnum.valueOf(info.getStageName());
        } catch (IllegalArgumentException e) {
            throw BusinessException.DateError.newInstance("第" + info.getDayNumber() + "阶段名称有误");
        }


        if (row + 1 != info.getDayNumber()) {
            throw BusinessException.DateError.newInstance("第" + (row + 1) + "行天数有误");
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

    private CourseDetailItemRes buildCourseDetailItemRes(CourseDetailEntity detail) {
        CourseDetailItemRes res = new CourseDetailItemRes();
        BeanUtils.copyProperties(detail, res);
        return res;
    }
}
