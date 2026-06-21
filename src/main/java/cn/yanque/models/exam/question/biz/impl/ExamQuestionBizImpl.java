package cn.yanque.models.exam.question.biz.impl;

import cn.yanque.common.api.PageResult;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.exam.question.biz.ExamQuestionBiz;
import cn.yanque.models.exam.question.pojo.bo.QueryExamQuestionBo;
import cn.yanque.models.exam.question.pojo.entity.ExamQuestionCourseEntity;
import cn.yanque.models.exam.question.pojo.entity.ExamQuestionEntity;
import cn.yanque.models.exam.question.pojo.entity.ExamQuestionOptionEntity;
import cn.yanque.models.exam.question.pojo.vo.req.ExamQuestionCourseStageReq;
import cn.yanque.models.exam.question.pojo.vo.req.ExamQuestionCreateReq;
import cn.yanque.models.exam.question.pojo.vo.req.ExamQuestionOptionReq;
import cn.yanque.models.exam.question.pojo.vo.req.ExamQuestionPageReq;
import cn.yanque.models.exam.question.pojo.vo.req.ExamQuestionStatusReq;
import cn.yanque.models.exam.question.pojo.vo.req.ExamQuestionUpdateReq;
import cn.yanque.models.exam.question.pojo.vo.res.ExamQuestionCreateRes;
import cn.yanque.models.exam.question.pojo.vo.res.ExamQuestionDeleteRes;
import cn.yanque.models.exam.question.pojo.vo.res.ExamQuestionDetailRes;
import cn.yanque.models.exam.question.pojo.vo.res.ExamQuestionPageRes;
import cn.yanque.models.exam.question.pojo.vo.res.ExamQuestionStatusRes;
import cn.yanque.models.exam.question.pojo.vo.res.ExamQuestionUpdateRes;
import cn.yanque.models.exam.question.service.ExamQuestionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * 考试题库业务层实现。
 *
 * <p>核心功能:
 * <ul>
 *   <li>题目的创建、更新、删除、查询
 *   <li>题目类型校验(单选/多选/判断/填空/简答/编程)
 *   <li>选项校验(选择题必须有选项, 非选择题不能有选项)
 *   <li>答案标准化处理(去空格、转大写、去重、排序)
 *   <li>课程阶段关联管理
 * </ul>
 *
 * <p>数据校验规则:
 * <ul>
 *   <li>单选题: 必须有2个以上选项, 只能有1个正确答案
 *   <li>多选题: 必须有2个以上选项, 至少2个正确答案
 *   <li>判断题: 答案只能是TRUE或FALSE
 *   <li>填空/简答/编程题: 不能有选项
 * </ul>
 */
@Service
public class ExamQuestionBizImpl implements ExamQuestionBiz {

    // 题目类型
    private static final Set<String> QUESTION_TYPES = Set.of("SINGLE", "MULTIPLE", "JUDGE", "FILL", "SHORT", "PROGRAMMING");
    // 选择题类型(需要选项)
    private static final Set<String> CHOICE_TYPES = Set.of("SINGLE", "MULTIPLE");
    // 难度等级
    private static final Set<String> DIFFICULTIES = Set.of("VERY_EASY", "EASY", "NORMAL", "HARD", "VERY_HARD");
    // 题目状态
    private static final Set<String> STATUSES = Set.of("ENABLED", "DISABLED");
    // 判断题答案
    private static final Set<String> JUDGE_ANSWERS = Set.of("TRUE", "FALSE");

    @Autowired
    private ExamQuestionService examQuestionService;

    /**
     * 创建题目。
     * 构建题目实体、课程关联、选项, 调用Service层完成事务性创建。
     */
    @Override
    public ExamQuestionCreateRes addQuestion(ExamQuestionCreateReq req) {
        ExamQuestionEntity question = buildQuestion(req);
        List<ExamQuestionCourseEntity> courseStages = buildCourseStages(req.getCourseStages());
        List<ExamQuestionOptionEntity> options = buildOptions(req.getQuestionType(), req.getAnswerContent(), req.getOptions());
        Long id = examQuestionService.createQuestion(question, courseStages, options);
        ExamQuestionCreateRes res = new ExamQuestionCreateRes();
        res.setId(id);
        return res;
    }

    /**
     * 更新题目。
     * 构建题目实体、课程关联、选项, 调用Service层完成事务性更新。
     */
    @Override
    public ExamQuestionUpdateRes updateQuestion(ExamQuestionUpdateReq req) {
        ExamQuestionEntity question = buildQuestion(req);
        question.setId(req.getId());
        List<ExamQuestionCourseEntity> courseStages = buildCourseStages(req.getCourseStages());
        List<ExamQuestionOptionEntity> options = buildOptions(req.getQuestionType(), req.getAnswerContent(), req.getOptions());
        Long id = examQuestionService.updateQuestion(question, courseStages, options);
        ExamQuestionUpdateRes res = new ExamQuestionUpdateRes();
        res.setId(id);
        return res;
    }

    /**
     * 删除题目。
     */
    @Override
    public ExamQuestionDeleteRes deleteQuestion(Long id) {
        ExamQuestionDeleteRes res = new ExamQuestionDeleteRes();
        res.setId(examQuestionService.deleteQuestion(id));
        return res;
    }

    /**
     * 查询题目详情。
     */
    @Override
    public ExamQuestionDetailRes getQuestionById(Long id) {
        return examQuestionService.getQuestionById(id);
    }

    /**
     * 分页查询题目列表。
     */
    @Override
    public PageResult<ExamQuestionPageRes> pageQuestion(ExamQuestionPageReq req) {
        QueryExamQuestionBo query = new QueryExamQuestionBo();
        BeanUtils.copyProperties(req, query);
        return examQuestionService.pageQuestion(query, req.getPageNum(), req.getPageSize());
    }

    /**
     * 更新题目状态(启用/禁用)。
     */
    @Override
    public ExamQuestionStatusRes updateStatus(Long id, ExamQuestionStatusReq req) {
        String status = normalize(req.getStatus());
        validateIn(status, STATUSES, "题目状态只能是ENABLED或DISABLED");
        examQuestionService.updateStatus(id, status);
        ExamQuestionStatusRes res = new ExamQuestionStatusRes();
        res.setId(id);
        res.setStatus(status);
        return res;
    }

    /**
     * 构建题目实体。
     * 校验题目类型、难度、状态, 标准化答案。
     */
    private ExamQuestionEntity buildQuestion(ExamQuestionCreateReq req) {
        String questionType = normalize(req.getQuestionType());
        String difficulty = normalize(req.getDifficulty());
        String status = normalize(req.getStatus());
        validateIn(questionType, QUESTION_TYPES, "题目类型错误");
        validateIn(difficulty, DIFFICULTIES, "难度只能是VERY_EASY、EASY、NORMAL、HARD或VERY_HARD");
        validateIn(status, STATUSES, "题目状态只能是ENABLED或DISABLED");

        String answerContent = normalizeAnswer(questionType, req.getAnswerContent());
        ExamQuestionEntity question = new ExamQuestionEntity();
        question.setQuestionType(questionType);
        question.setQuestionContent(req.getQuestionContent().trim());
        question.setAnswerContent(answerContent);
        question.setAnalysisContent(StringUtils.hasText(req.getAnalysisContent()) ? req.getAnalysisContent().trim() : null);
        question.setDifficulty(difficulty);
        question.setStatus(status);
        return question;
    }

    /**
     * 构建选项列表。
     * 非选择题不能有选项, 选择题至少2个选项, 选项标识不能重复, 正确答案必须在选项中。
     */
    private List<ExamQuestionOptionEntity> buildOptions(String questionType, String answerContent, List<ExamQuestionOptionReq> optionReqs) {
        String type = normalize(questionType);
        if (!CHOICE_TYPES.contains(type)) {
            if (!CollectionUtils.isEmpty(optionReqs)) {
                throw BusinessException.DateError.newInstance("非选择题不能维护选项");
            }
            return List.of();
        }
        if (CollectionUtils.isEmpty(optionReqs) || optionReqs.size() < 2) {
            throw BusinessException.DateError.newInstance("选择题至少需要两个选项");
        }

        Set<String> optionKeys = new HashSet<>();
        List<ExamQuestionOptionEntity> options = new ArrayList<>();
        for (int i = 0; i < optionReqs.size(); i++) {
            ExamQuestionOptionReq optionReq = optionReqs.get(i);
            String optionKey = normalize(optionReq.getOptionKey());
            if (!optionKeys.add(optionKey)) {
                throw BusinessException.DateError.newInstance("选项标识重复:" + optionKey);
            }
            ExamQuestionOptionEntity option = new ExamQuestionOptionEntity();
            option.setOptionKey(optionKey);
            option.setOptionContent(optionReq.getOptionContent().trim());
            options.add(option);
        }

        List<String> answerKeys = Arrays.stream(normalizeAnswer(type, answerContent).split(","))
                .filter(StringUtils::hasText)
                .toList();
        if ("SINGLE".equals(type) && answerKeys.size() != 1) {
            throw BusinessException.DateError.newInstance("单选题只能有一个正确答案");
        }
        if ("MULTIPLE".equals(type) && answerKeys.size() < 2) {
            throw BusinessException.DateError.newInstance("多选题至少需要两个正确答案");
        }
        for (String answerKey : answerKeys) {
            if (!optionKeys.contains(answerKey)) {
                throw BusinessException.DateError.newInstance("正确答案不在选项中:" + answerKey);
            }
        }
        return options;
    }

    /**
     * 构建课程阶段关联列表。
     * 去重处理, 同一课程同一阶段只保留一条记录。
     */
    private List<ExamQuestionCourseEntity> buildCourseStages(List<ExamQuestionCourseStageReq> courseStageReqs) {
        if (CollectionUtils.isEmpty(courseStageReqs)) {
            return List.of();
        }
        Set<String> relationKeys = new HashSet<>();
        List<ExamQuestionCourseEntity> relations = new ArrayList<>();
        for (ExamQuestionCourseStageReq req : courseStageReqs) {
            String stageName = req.getStageName().trim();
            String relationKey = req.getCourseId() + "::" + stageName;
            if (!relationKeys.add(relationKey)) {
                continue;
            }
            ExamQuestionCourseEntity relation = new ExamQuestionCourseEntity();
            relation.setCourseId(req.getCourseId());
            relation.setStageName(stageName);
            relations.add(relation);
        }
        return relations;
    }

    /**
     * 标准化答案内容。
     * 选择题: 去空格、转大写、去重、排序
     * 判断题: 只能是TRUE或FALSE
     * 其他题型: 只去首尾空格
     */
    private String normalizeAnswer(String questionType, String answerContent) {
        String type = normalize(questionType);
        if (!StringUtils.hasText(answerContent)) {
            throw BusinessException.DateError.newInstance("正确答案不能为空");
        }
        if (CHOICE_TYPES.contains(type)) {
            return String.join(",", Arrays.stream(answerContent.split(","))
                    .map(this::normalize)
                    .filter(StringUtils::hasText)
                    .distinct()
                    .toList());
        }
        if ("JUDGE".equals(type)) {
            String answer = normalize(answerContent);
            validateIn(answer, JUDGE_ANSWERS, "判断题答案只能是TRUE或FALSE");
            return answer;
        }
        return answerContent.trim();
    }

    /**
     * 校验值是否在允许的集合中。
     */
    private void validateIn(String value, Set<String> values, String message) {
        if (!values.contains(value)) {
            throw BusinessException.DateError.newInstance(message);
        }
    }

    /**
     * 标准化字符串: 去首尾空格、转大写。
     */
    private String normalize(String value) {
        return value == null ? null : value.trim().toUpperCase(Locale.ROOT);
    }
}
