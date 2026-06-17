package cn.yanque.models.student.followup.service.impl;

import cn.yanque.common.api.PageResult;
import cn.yanque.common.dataConfig.service.SysConfig;
import cn.yanque.common.dataConfig.service.SysConfigService;
import cn.yanque.common.enums.ActiveEnum;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.student.followup.pojo.bo.QueryStudentFollowupTagBo;
import cn.yanque.models.student.followup.pojo.entity.StudentFollowupTagEntity;
import cn.yanque.models.student.followup.pojo.vo.req.StudentFollowupTagCreateReq;
import cn.yanque.models.student.followup.pojo.vo.req.StudentFollowupTagPageReq;
import cn.yanque.models.student.followup.pojo.vo.req.StudentFollowupTagUpdateReq;
import cn.yanque.models.student.followup.pojo.vo.res.StudentFollowupTagCreateRes;
import cn.yanque.models.student.followup.pojo.vo.res.StudentFollowupTagDeleteRes;
import cn.yanque.models.student.followup.pojo.vo.res.StudentFollowupTagDetailRes;
import cn.yanque.models.student.followup.pojo.vo.res.StudentFollowupTagPageRes;
import cn.yanque.models.student.followup.pojo.vo.res.StudentFollowupTagUpdateRes;
import cn.yanque.models.student.followup.service.StudentFollowupTagService;
import cn.yanque.models.student.mapper.StudentFollowupTagMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentFollowupTagServiceImpl implements StudentFollowupTagService {

    @Autowired
    private StudentFollowupTagMapper studentFollowupTagMapper;

    @Autowired
    private SysConfigService sysConfigService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentFollowupTagCreateRes add(StudentFollowupTagCreateReq req) {
        StudentFollowupTagEntity entity = new StudentFollowupTagEntity();
        fillFields(entity, req.getStudentTag(), req.getFollowupIntervalDays(), req.getStatus(), req.getRemark(), true);
        try {
            studentFollowupTagMapper.insert(entity);
        } catch (DuplicateKeyException e) {
            throw BusinessException.DateExist.newInstance("该学生标签已配置回访规则");
        }

        StudentFollowupTagCreateRes res = new StudentFollowupTagCreateRes();
        res.setId(entity.getId());
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentFollowupTagUpdateRes update(StudentFollowupTagUpdateReq req) {
        if (studentFollowupTagMapper.selectById(req.getId()) == null) {
            throw BusinessException.DateError.newInstance("回访标签配置不存在");
        }
        StudentFollowupTagEntity entity = new StudentFollowupTagEntity();
        entity.setId(req.getId());
        fillFields(entity, req.getStudentTag(), req.getFollowupIntervalDays(), req.getStatus(), req.getRemark(), false);
        try {
            int rows = studentFollowupTagMapper.updateById(entity);
            if (rows == 0) {
                throw BusinessException.DateError.newInstance("回访标签配置不存在");
            }
        } catch (DuplicateKeyException e) {
            throw BusinessException.DateExist.newInstance("该学生标签已配置回访规则");
        }

        StudentFollowupTagUpdateRes res = new StudentFollowupTagUpdateRes();
        res.setId(req.getId());
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentFollowupTagDeleteRes delete(Long id) {
        int rows = studentFollowupTagMapper.deleteById(id);
        if (rows == 0) {
            throw BusinessException.DateError.newInstance("回访标签配置不存在");
        }
        StudentFollowupTagDeleteRes res = new StudentFollowupTagDeleteRes();
        res.setId(id);
        return res;
    }

    @Override
    public StudentFollowupTagDetailRes detail(Long id) {
        StudentFollowupTagEntity entity = studentFollowupTagMapper.selectById(id);
        if (entity == null) {
            throw BusinessException.DateError.newInstance("回访标签配置不存在");
        }
        StudentFollowupTagDetailRes res = new StudentFollowupTagDetailRes();
        BeanUtils.copyProperties(entity, res);
        return res;
    }

    @Override
    public PageResult<StudentFollowupTagPageRes> page(StudentFollowupTagPageReq req) {
        int pageNum = req.getPageNum() == null ? 1 : req.getPageNum();
        int pageSize = req.getPageSize() == null ? 10 : req.getPageSize();
        QueryStudentFollowupTagBo query = new QueryStudentFollowupTagBo();
        BeanUtils.copyProperties(req, query);
        PageHelper.startPage(pageNum, pageSize);
        List<StudentFollowupTagEntity> list = studentFollowupTagMapper.selectPage(query);
        PageInfo<StudentFollowupTagEntity> pageInfo = new PageInfo<>(list);
        List<StudentFollowupTagPageRes> records = list.stream().map(this::buildPageRes).toList();
        return new PageResult<>(pageInfo.getTotal(), pageNum, pageSize, records);
    }

    private void fillFields(StudentFollowupTagEntity entity, String studentTag, Integer followupIntervalDays,
                            String status, String remark, boolean creating) {
        String normalizedTag = normalizeStudentTag(studentTag);
        validateStudentTag(normalizedTag);
        entity.setStudentTag(normalizedTag);
        entity.setFollowupIntervalDays(followupIntervalDays);
        entity.setStatus(normalizeStatus(status));
        entity.setRemark(normalizeBlank(remark));
        entity.setUpdatedAt(new Date());
        if (creating) {
            entity.setCreatedAt(entity.getUpdatedAt());
        }
    }

    private StudentFollowupTagPageRes buildPageRes(StudentFollowupTagEntity entity) {
        StudentFollowupTagPageRes res = new StudentFollowupTagPageRes();
        BeanUtils.copyProperties(entity, res);
        return res;
    }

    private void validateStudentTag(String studentTag) {
        if (!parseStudentTagOptions().contains(studentTag)) {
            throw BusinessException.DateError.newInstance("学生标签不在配置范围内");
        }
    }

    private List<String> parseStudentTagOptions() {
        String value = sysConfigService.get(SysConfig.studentTagOptions);
        if (value == null || value.isBlank()) {
            return List.of();
        }
        Set<String> options = Arrays.stream(value.split("[,，、;；\\n\\r]+"))
                .map(String::trim)
                .filter(item -> !item.isBlank())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return List.copyOf(options);
    }

    private String normalizeStudentTag(String studentTag) {
        if (studentTag == null || studentTag.isBlank()) {
            throw BusinessException.DateError.newInstance("学生标签不能为空");
        }
        return studentTag.trim();
    }

    private String normalizeStatus(String status) {
        if (status == null || status.isBlank()) {
            return ActiveEnum.ACTIVE.name();
        }
        String normalized = status.trim();
        if (!ActiveEnum.ACTIVE.name().equals(normalized) && !ActiveEnum.INACTIVE.name().equals(normalized)) {
            throw BusinessException.DateError.newInstance("状态只能是ACTIVE或INACTIVE");
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
