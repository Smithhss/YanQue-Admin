package cn.yanque.models.exam.paper.service.impl;

import cn.yanque.common.api.PageResult;
import cn.yanque.common.enums.EnableStatusEnum;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.exam.paper.mapper.ExamPaperMapper;
import cn.yanque.models.exam.paper.mapper.ExamPaperQuestionMapper;
import cn.yanque.models.exam.paper.pojo.bo.QueryExamPaperBo;
import cn.yanque.models.exam.paper.pojo.entity.ExamPaperEntity;
import cn.yanque.models.exam.paper.pojo.entity.ExamPaperQuestionEntity;
import cn.yanque.models.exam.paper.pojo.vo.res.ExamPaperDetailRes;
import cn.yanque.models.exam.paper.pojo.vo.res.ExamPaperPageRes;
import cn.yanque.models.exam.paper.pojo.vo.res.ExamPaperQuestionRes;
import cn.yanque.models.exam.paper.service.ExamPaperService;
import cn.yanque.models.exam.question.mapper.ExamQuestionCourseMapper;
import cn.yanque.models.exam.question.mapper.ExamQuestionMapper;
import cn.yanque.models.exam.question.pojo.entity.ExamQuestionCourseEntity;
import cn.yanque.models.exam.question.pojo.entity.ExamQuestionEntity;
import cn.yanque.models.teaching.course.mapper.CourseDetailMapper;
import cn.yanque.models.teaching.course.mapper.CourseMapper;
import cn.yanque.models.teaching.course.pojo.entity.CourseDetailEntity;
import cn.yanque.models.teaching.course.pojo.entity.CourseEntity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ExamPaperServiceImpl implements ExamPaperService {

    @Autowired
    private ExamPaperMapper examPaperMapper;

    @Autowired
    private ExamPaperQuestionMapper examPaperQuestionMapper;

    @Autowired
    private ExamQuestionMapper examQuestionMapper;

    @Autowired
    private ExamQuestionCourseMapper examQuestionCourseMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseDetailMapper courseDetailMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long savePaperWithQuestions(ExamPaperEntity paper, List<ExamPaperQuestionEntity> questions) {
        validatePaper(paper, questions);
        Date now = new Date();
        paper.setCreatedAt(now);
        paper.setUpdatedAt(now);
        examPaperMapper.insert(paper);
        questions.forEach(question -> {
            question.setPaperId(paper.getId());
            question.setCreatedAt(now);
            question.setUpdatedAt(now);
        });
        examPaperQuestionMapper.insertBatch(questions);
        return paper.getId();
    }

    @Override
    public PageResult<ExamPaperPageRes> pagePaper(QueryExamPaperBo query, Integer pageNum, Integer pageSize) {
        int currentPage = pageNum == null ? 1 : pageNum;
        int currentSize = pageSize == null ? 10 : pageSize;
        PageHelper.startPage(currentPage, currentSize);
        List<ExamPaperEntity> list = examPaperMapper.selectPage(query);
        PageInfo<ExamPaperEntity> pageInfo = new PageInfo<>(list);
        List<ExamPaperPageRes> records = list.stream().map(this::buildPageRes).toList();
        fillCourseName(records);
        fillQuestionCount(records);
        return new PageResult<>(pageInfo.getTotal(), currentPage, currentSize, records);
    }

    @Override
    public ExamPaperDetailRes getPaperById(Long id) {
        ExamPaperEntity paper = examPaperMapper.selectById(id);
        if (paper == null) {
            throw BusinessException.DateError.newInstance("试卷不存在");
        }
        ExamPaperDetailRes res = new ExamPaperDetailRes();
        BeanUtils.copyProperties(paper, res);
        fillCourseName(List.of(res));
        List<ExamPaperQuestionEntity> questions = examPaperQuestionMapper.selectByPaperId(id);
        res.setQuestionCount(questions.size());
        res.setQuestions(buildQuestionResList(questions));
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long deletePaper(Long id) {
        examPaperQuestionMapper.deleteByPaperId(id);
        int rows = examPaperMapper.deleteById(id);
        if (rows == 0) {
            throw BusinessException.DateError.newInstance("试卷不存在");
        }
        return id;
    }

    private void validatePaper(ExamPaperEntity paper, List<ExamPaperQuestionEntity> questions) {
        CourseEntity course = courseMapper.selectById(paper.getCourseId());
        if (course == null) {
            throw BusinessException.CourseNotExist;
        }
        if (StringUtils.hasText(paper.getStageName())) {
            boolean stageExist = courseDetailMapper.selectByCourseId(paper.getCourseId()).stream()
                    .map(CourseDetailEntity::getStageName)
                    .anyMatch(stageName -> stageName.equals(paper.getStageName()));
            if (!stageExist) {
                throw BusinessException.DateError.newInstance("课程阶段不存在:" + paper.getStageName());
            }
        }

        List<Long> questionIds = questions.stream().map(ExamPaperQuestionEntity::getQuestionId).toList();
        Map<Long, ExamQuestionEntity> questionMap = questionIds.stream()
                .map(examQuestionMapper::selectById)
                .filter(question -> question != null)
                .collect(Collectors.toMap(ExamQuestionEntity::getId, Function.identity()));
        if (questionMap.size() != questionIds.size()) {
            throw BusinessException.DateError.newInstance("试卷包含不存在的题目");
        }
        List<ExamQuestionCourseEntity> relations = examQuestionCourseMapper.selectByQuestionIds(questionIds);
        Map<Long, List<ExamQuestionCourseEntity>> relationMap = relations.stream()
                .collect(Collectors.groupingBy(ExamQuestionCourseEntity::getQuestionId));
        for (Long questionId : questionIds) {
            ExamQuestionEntity question = questionMap.get(questionId);
            if (!EnableStatusEnum.ENABLED.name().equals(question.getStatus())) {
                throw BusinessException.DateError.newInstance("试卷包含未启用题目:" + questionId);
            }
            List<ExamQuestionCourseEntity> questionRelations = relationMap.getOrDefault(questionId, List.of());
            boolean courseMatched = questionRelations.stream().anyMatch(relation -> paper.getCourseId().equals(relation.getCourseId()));
            if (!courseMatched) {
                throw BusinessException.DateError.newInstance("题目不属于当前课程:" + questionId);
            }
            if (StringUtils.hasText(paper.getStageName())) {
                boolean stageMatched = questionRelations.stream().anyMatch(relation ->
                        paper.getCourseId().equals(relation.getCourseId()) && paper.getStageName().equals(relation.getStageName()));
                if (!stageMatched) {
                    throw BusinessException.DateError.newInstance("题目不属于当前课程阶段:" + questionId);
                }
            }
        }
    }

    private ExamPaperPageRes buildPageRes(ExamPaperEntity paper) {
        ExamPaperPageRes res = new ExamPaperPageRes();
        BeanUtils.copyProperties(paper, res);
        return res;
    }

    private List<ExamPaperQuestionRes> buildQuestionResList(List<ExamPaperQuestionEntity> questions) {
        Map<Long, ExamQuestionEntity> questionMap = questions.stream()
                .map(ExamPaperQuestionEntity::getQuestionId)
                .distinct()
                .map(examQuestionMapper::selectById)
                .filter(question -> question != null)
                .collect(Collectors.toMap(ExamQuestionEntity::getId, Function.identity()));
        return questions.stream().map(question -> {
            ExamPaperQuestionRes res = new ExamPaperQuestionRes();
            BeanUtils.copyProperties(question, res);
            ExamQuestionEntity questionEntity = questionMap.get(question.getQuestionId());
            if (questionEntity != null) {
                res.setQuestionContent(questionEntity.getQuestionContent());
                res.setQuestionType(questionEntity.getQuestionType());
                res.setDifficulty(questionEntity.getDifficulty());
            }
            return res;
        }).toList();
    }

    private void fillCourseName(List<? extends ExamPaperPageRes> records) {
        List<Long> courseIds = records.stream().map(ExamPaperPageRes::getCourseId).distinct().toList();
        if (courseIds.isEmpty()) {
            return;
        }
        Map<Long, CourseEntity> courseMap = courseMapper.selectByIds(courseIds).stream()
                .collect(Collectors.toMap(CourseEntity::getId, Function.identity()));
        records.forEach(record -> {
            CourseEntity course = courseMap.get(record.getCourseId());
            record.setCourseName(course == null ? null : course.getCourseName());
        });
    }

    private void fillQuestionCount(List<ExamPaperPageRes> records) {
        List<Long> paperIds = records.stream().map(ExamPaperPageRes::getId).toList();
        if (paperIds.isEmpty()) {
            return;
        }
        Map<Long, Long> countMap = examPaperQuestionMapper.selectByPaperIds(paperIds).stream()
                .collect(Collectors.groupingBy(ExamPaperQuestionEntity::getPaperId, Collectors.counting()));
        records.forEach(record -> record.setQuestionCount(countMap.getOrDefault(record.getId(), 0L).intValue()));
    }
}
