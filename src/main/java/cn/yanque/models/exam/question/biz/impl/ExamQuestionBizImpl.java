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

@Service
public class ExamQuestionBizImpl implements ExamQuestionBiz {

    private static final Set<String> QUESTION_TYPES = Set.of("SINGLE", "MULTIPLE", "JUDGE", "FILL", "SHORT", "PROGRAMMING");
    private static final Set<String> CHOICE_TYPES = Set.of("SINGLE", "MULTIPLE");
    private static final Set<String> DIFFICULTIES = Set.of("VERY_EASY", "EASY", "NORMAL", "HARD", "VERY_HARD");
    private static final Set<String> STATUSES = Set.of("ENABLED", "DISABLED");
    private static final Set<String> JUDGE_ANSWERS = Set.of("TRUE", "FALSE");

    @Autowired
    private ExamQuestionService examQuestionService;

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

    @Override
    public ExamQuestionDeleteRes deleteQuestion(Long id) {
        ExamQuestionDeleteRes res = new ExamQuestionDeleteRes();
        res.setId(examQuestionService.deleteQuestion(id));
        return res;
    }

    @Override
    public ExamQuestionDetailRes getQuestionById(Long id) {
        return examQuestionService.getQuestionById(id);
    }

    @Override
    public PageResult<ExamQuestionPageRes> pageQuestion(ExamQuestionPageReq req) {
        QueryExamQuestionBo query = new QueryExamQuestionBo();
        BeanUtils.copyProperties(req, query);
        return examQuestionService.pageQuestion(query, req.getPageNum(), req.getPageSize());
    }

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
                throw BusinessException.DateError.newInstance("选项标识重复：" + optionKey);
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
                throw BusinessException.DateError.newInstance("正确答案不在选项中：" + answerKey);
            }
        }
        return options;
    }

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

    private void validateIn(String value, Set<String> values, String message) {
        if (!values.contains(value)) {
            throw BusinessException.DateError.newInstance(message);
        }
    }

    private String normalize(String value) {
        return value == null ? null : value.trim().toUpperCase(Locale.ROOT);
    }
}
