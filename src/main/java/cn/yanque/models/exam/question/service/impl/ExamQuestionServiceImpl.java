package cn.yanque.models.exam.question.service.impl;

import cn.yanque.common.api.PageResult;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.exam.question.mapper.ExamQuestionCourseMapper;
import cn.yanque.models.exam.question.mapper.ExamQuestionMapper;
import cn.yanque.models.exam.question.mapper.ExamQuestionOptionMapper;
import cn.yanque.models.exam.question.pojo.bo.QueryExamQuestionBo;
import cn.yanque.models.exam.question.pojo.entity.ExamQuestionCourseEntity;
import cn.yanque.models.exam.question.pojo.entity.ExamQuestionEntity;
import cn.yanque.models.exam.question.pojo.entity.ExamQuestionOptionEntity;
import cn.yanque.models.exam.question.pojo.vo.res.ExamQuestionDetailRes;
import cn.yanque.models.exam.question.pojo.vo.res.ExamQuestionOptionRes;
import cn.yanque.models.exam.question.pojo.vo.res.ExamQuestionPageRes;
import cn.yanque.models.exam.question.service.ExamQuestionService;
import cn.yanque.models.teaching.course.mapper.CourseMapper;
import cn.yanque.models.teaching.course.mapper.CourseDetailMapper;
import cn.yanque.models.teaching.course.pojo.entity.CourseDetailEntity;
import cn.yanque.models.teaching.course.pojo.entity.CourseEntity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ExamQuestionServiceImpl implements ExamQuestionService {

    @Autowired
    private ExamQuestionMapper questionMapper;

    @Autowired
    private ExamQuestionOptionMapper optionMapper;

    @Autowired
    private ExamQuestionCourseMapper questionCourseMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseDetailMapper courseDetailMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createQuestion(ExamQuestionEntity question, List<ExamQuestionCourseEntity> courseStages, List<ExamQuestionOptionEntity> options) {
        validateCourseStages(courseStages);
        Date now = new Date();
        question.setCreatedAt(now);
        question.setUpdatedAt(now);
        questionMapper.insert(question);
        saveQuestionCourses(question.getId(), courseStages, now);
        saveOptions(question.getId(), options, now);
        return question.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long updateQuestion(ExamQuestionEntity question, List<ExamQuestionCourseEntity> courseStages, List<ExamQuestionOptionEntity> options) {
        validateCourseStages(courseStages);
        question.setUpdatedAt(new Date());
        int rows = questionMapper.updateById(question);
        if (rows == 0) {
            throw BusinessException.DateError.newInstance("题目不存在");
        }
        questionCourseMapper.deleteByQuestionId(question.getId());
        saveQuestionCourses(question.getId(), courseStages, new Date());
        optionMapper.deleteByQuestionId(question.getId());
        saveOptions(question.getId(), options, new Date());
        return question.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long deleteQuestion(Long id) {
        questionCourseMapper.deleteByQuestionId(id);
        optionMapper.deleteByQuestionId(id);
        int rows = questionMapper.deleteById(id);
        if (rows == 0) {
            throw BusinessException.DateError.newInstance("题目不存在");
        }
        return id;
    }

    @Override
    public ExamQuestionDetailRes getQuestionById(Long id) {
        ExamQuestionEntity question = questionMapper.selectById(id);
        if (question == null) {
            throw BusinessException.DateError.newInstance("题目不存在");
        }
        ExamQuestionDetailRes res = new ExamQuestionDetailRes();
        BeanUtils.copyProperties(question, res);
        fillCourseInfo(List.of(res));
        res.setOptions(optionMapper.selectByQuestionId(id).stream().map(this::buildOptionRes).toList());
        return res;
    }

    @Override
    public PageResult<ExamQuestionPageRes> pageQuestion(QueryExamQuestionBo query, Integer pageNum, Integer pageSize) {
        int currentPage = pageNum == null ? 1 : pageNum;
        int currentSize = pageSize == null ? 10 : pageSize;
        PageHelper.startPage(currentPage, currentSize);
        List<ExamQuestionEntity> list = questionMapper.selectPage(query);
        PageInfo<ExamQuestionEntity> pageInfo = new PageInfo<>(list);
        List<ExamQuestionPageRes> records = list.stream().map(this::buildPageRes).toList();
        fillCourseInfo(records);
        return new PageResult<>(pageInfo.getTotal(), currentPage, currentSize, records);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long updateStatus(Long id, String status) {
        int rows = questionMapper.updateStatusById(id, status);
        if (rows == 0) {
            throw BusinessException.DateError.newInstance("题目不存在");
        }
        return id;
    }

    private void saveOptions(Long questionId, List<ExamQuestionOptionEntity> options, Date now) {
        if (options == null || options.isEmpty()) {
            return;
        }
        options.forEach(option -> {
            option.setQuestionId(questionId);
            option.setCreatedAt(now);
            option.setUpdatedAt(now);
        });
        optionMapper.insertBatch(options);
    }

    private void saveQuestionCourses(Long questionId, List<ExamQuestionCourseEntity> courseStages, Date now) {
        if (courseStages == null || courseStages.isEmpty()) {
            return;
        }
        courseStages.forEach(relation -> {
            relation.setQuestionId(questionId);
            relation.setCreatedAt(now);
        });
        questionCourseMapper.insertBatch(courseStages);
    }

    private void validateCourseStages(List<ExamQuestionCourseEntity> courseStages) {
        if (courseStages == null || courseStages.isEmpty()) {
            return;
        }
        List<Long> distinctIds = courseStages.stream().map(ExamQuestionCourseEntity::getCourseId).distinct().toList();
        List<CourseEntity> courses = courseMapper.selectByIds(distinctIds);
        if (courses.size() != distinctIds.size()) {
            throw BusinessException.CourseNotExist;
        }
        Map<Long, Set<String>> validStageMap = distinctIds.stream().collect(Collectors.toMap(
                Function.identity(),
                courseId -> courseDetailMapper.selectByCourseId(courseId).stream()
                        .map(CourseDetailEntity::getStageName)
                        .collect(Collectors.toSet())
        ));
        for (ExamQuestionCourseEntity relation : courseStages) {
            Set<String> validStages = validStageMap.get(relation.getCourseId());
            if (validStages == null || !validStages.contains(relation.getStageName())) {
                throw BusinessException.DateError.newInstance("课程阶段不存在：" + relation.getStageName());
            }
        }
    }

    private ExamQuestionPageRes buildPageRes(ExamQuestionEntity question) {
        ExamQuestionPageRes res = new ExamQuestionPageRes();
        BeanUtils.copyProperties(question, res);
        return res;
    }

    private ExamQuestionOptionRes buildOptionRes(ExamQuestionOptionEntity option) {
        ExamQuestionOptionRes res = new ExamQuestionOptionRes();
        BeanUtils.copyProperties(option, res);
        return res;
    }

    private void fillCourseInfo(List<? extends ExamQuestionPageRes> records) {
        List<Long> questionIds = records.stream().map(ExamQuestionPageRes::getId).toList();
        if (questionIds.isEmpty()) {
            return;
        }
        List<ExamQuestionCourseEntity> relations = questionCourseMapper.selectByQuestionIds(questionIds);
        List<Long> courseIds = relations.stream().map(ExamQuestionCourseEntity::getCourseId).distinct().toList();
        if (courseIds.isEmpty()) {
            return;
        }
        Map<Long, CourseEntity> courseMap = courseMapper.selectByIds(courseIds).stream()
                .collect(Collectors.toMap(CourseEntity::getId, Function.identity()));
        Map<Long, List<ExamQuestionCourseEntity>> relationMap = relations.stream()
                .collect(Collectors.groupingBy(ExamQuestionCourseEntity::getQuestionId));
        records.forEach(record -> {
            List<ExamQuestionCourseEntity> questionRelations = relationMap.getOrDefault(record.getId(), List.of());
            List<Long> recordCourseIds = questionRelations.stream().map(ExamQuestionCourseEntity::getCourseId).toList();
            Set<Long> usedCourseIds = new HashSet<>(recordCourseIds);
            String courseNames = recordCourseIds.stream()
                    .map(courseMap::get)
                    .filter(course -> course != null && usedCourseIds.contains(course.getId()))
                    .map(CourseEntity::getCourseName)
                    .collect(Collectors.joining("、"));
            List<String> courseStageKeys = questionRelations.stream()
                    .map(relation -> relation.getCourseId() + "::" + relation.getStageName())
                    .toList();
            String courseStageNames = questionRelations.stream()
                    .map(relation -> {
                        CourseEntity course = courseMap.get(relation.getCourseId());
                        String courseName = course == null ? String.valueOf(relation.getCourseId()) : course.getCourseName();
                        return courseName + " / " + relation.getStageName();
                    })
                    .collect(Collectors.joining("、"));
            record.setCourseIds(recordCourseIds);
            record.setCourseNames(courseNames);
            record.setCourseStageKeys(courseStageKeys);
            record.setCourseStageNames(courseStageNames);
        });
    }
}
