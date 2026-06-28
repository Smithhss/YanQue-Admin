package cn.yanque.models.teaching.clazz.service.impl;

import cn.yanque.common.api.PageResult;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.teaching.campus.mapper.CampusMapper;
import cn.yanque.models.teaching.campus.pojo.entity.CampusEntity;
import cn.yanque.models.teaching.clazz.mapper.ClazzMapper;
import cn.yanque.models.teaching.clazz.pojo.entity.ClazzEntity;
import cn.yanque.models.teaching.clazz.pojo.vo.req.ClazzCreateReq;
import cn.yanque.models.teaching.clazz.pojo.vo.req.ClazzPageReq;
import cn.yanque.models.teaching.clazz.pojo.vo.req.ClazzUpdateReq;
import cn.yanque.models.teaching.clazz.pojo.vo.res.*;
import cn.yanque.models.teaching.clazz.service.ClazzService;
import cn.yanque.models.teaching.course.mapper.CourseMapper;
import cn.yanque.models.teaching.course.pojo.entity.CourseEntity;
import cn.yanque.models.users.mapper.SysUserMapper;
import cn.yanque.models.users.pojo.entity.SysUserEntity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ClazzServiceImpl implements ClazzService {

    @Autowired
    private ClazzMapper clazzMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private CampusMapper campusMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClazzCreateRes addClazz(ClazzCreateReq req) {
        ClazzEntity clazz = new ClazzEntity();
        clazz.setClassPeriod(req.getClassPeriod());
        clazz.setHeadTeacherId(req.getHeadTeacherId());
        clazz.setCampusId(req.getCampusId());
        clazz.setCourseId(req.getCourseId());
        clazz.setCreatedAt(new Date());
        clazz.setUpdatedAt(new Date());
        clazzMapper.insert(clazz);

        ClazzCreateRes res = new ClazzCreateRes();
        res.setId(clazz.getId());
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClazzUpdateRes updateClazz(ClazzUpdateReq req) {
        ClazzEntity clazz = new ClazzEntity();
        clazz.setId(req.getId());
        clazz.setClassPeriod(req.getClassPeriod());
        clazz.setHeadTeacherId(req.getHeadTeacherId());
        clazz.setCampusId(req.getCampusId());
        clazz.setCourseId(req.getCourseId());
        clazz.setUpdatedAt(new Date());

        int rows = clazzMapper.updateById(clazz);
        if (rows == 0) {
            throw BusinessException.ClazzNotExist;
        }

        ClazzUpdateRes res = new ClazzUpdateRes();
        res.setId(req.getId());
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClazzDeleteRes deleteClazz(Long id) {
        int rows = clazzMapper.deleteById(id);
        if (rows == 0) {
            throw BusinessException.ClazzNotExist;
        }

        ClazzDeleteRes res = new ClazzDeleteRes();
        res.setId(id);
        return res;
    }

    @Override
    public ClazzDetailRes getClazzById(Long id) {
        ClazzEntity clazz = clazzMapper.selectById(id);
        if (clazz == null) {
            throw BusinessException.ClazzNotExist;
        }
        ClazzDetailRes res = buildClazzDetailRes(clazz);
        fillClazzDetailName(res);
        return res;
    }

    @Override
    public PageResult<ClazzPageRes> pageClazz(ClazzPageReq req) {
        // 班级列表只在班级表里做分页查询,关联名称在后面分批补齐,避免写连表 SQL。
        int pageNum = req.getPageNum() == null ? 1 : req.getPageNum();
        int pageSize = req.getPageSize() == null ? 10 : req.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<ClazzEntity> list = clazzMapper.selectPage(req.getKeyword(), req.getHeadTeacherId(), req.getCampusId(), req.getCourseId());
        PageInfo<ClazzEntity> pageInfo = new PageInfo<>(list);
        List<ClazzPageRes> records = list.stream().map(this::buildClazzPageRes).toList();
        fillClazzPageNames(records);
        return new PageResult<>(pageInfo.getTotal(), pageNum, pageSize, records);
    }

    private void fillClazzPageNames(List<ClazzPageRes> records) {
        if (records.isEmpty()) {
            return;
        }

        // 从本页班级数据中提取需要查询的ID,分别去用户,校区,课程表批量查询。
        List<Long> headTeacherIds = records.stream().map(ClazzPageRes::getHeadTeacherId).distinct().toList();
        List<Long> campusIds = records.stream().map(ClazzPageRes::getCampusId).distinct().toList();
        List<Long> courseIds = records.stream().map(ClazzPageRes::getCourseId).distinct().toList();

        // 转成 Map 后按ID回填名称,前端就不需要自己维护 ID -> 名称 的映射。
        Map<Long, SysUserEntity> userMap = sysUserMapper.selectByIds(headTeacherIds).stream()
                .collect(Collectors.toMap(SysUserEntity::getId, Function.identity()));
        Map<Long, CampusEntity> campusMap = campusMapper.selectByIds(campusIds).stream()
                .collect(Collectors.toMap(CampusEntity::getId, Function.identity()));
        Map<Long, CourseEntity> courseMap = courseMapper.selectByIds(courseIds).stream()
                .collect(Collectors.toMap(CourseEntity::getId, Function.identity()));

        records.forEach(record -> {
            SysUserEntity user = userMap.get(record.getHeadTeacherId());
            if (user != null) {
                record.setHeadTeacherName(user.getNickname() != null ? user.getNickname() : user.getUsername());
            }
            CampusEntity campus = campusMap.get(record.getCampusId());
            if (campus != null) {
                record.setCampusName(campus.getCampusLocation());
            }
            CourseEntity course = courseMap.get(record.getCourseId());
            if (course != null) {
                record.setCourseName(course.getCourseName());
            }
        });
    }

    private void fillClazzDetailName(ClazzDetailRes res) {
        // 详情页也是单表取班级,再分别查询名称,保持"不连表"的实现方式一致。
        SysUserEntity user = sysUserMapper.selectByIds(Collections.singletonList(res.getHeadTeacherId())).stream().findFirst().orElse(null);
        if (user != null) {
            res.setHeadTeacherName(user.getNickname() != null ? user.getNickname() : user.getUsername());
        }
        CampusEntity campus = campusMapper.selectByIds(Collections.singletonList(res.getCampusId())).stream().findFirst().orElse(null);
        if (campus != null) {
            res.setCampusName(campus.getCampusLocation());
        }
        CourseEntity course = courseMapper.selectByIds(Collections.singletonList(res.getCourseId())).stream().findFirst().orElse(null);
        if (course != null) {
            res.setCourseName(course.getCourseName());
        }
    }

    private ClazzDetailRes buildClazzDetailRes(ClazzEntity clazz) {
        ClazzDetailRes res = new ClazzDetailRes();
        BeanUtils.copyProperties(clazz, res);
        return res;
    }

    private ClazzPageRes buildClazzPageRes(ClazzEntity clazz) {
        ClazzPageRes res = new ClazzPageRes();
        BeanUtils.copyProperties(clazz, res);
        return res;
    }
}
