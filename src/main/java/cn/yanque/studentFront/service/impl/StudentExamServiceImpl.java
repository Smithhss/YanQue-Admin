package cn.yanque.studentFront.service.impl;

import cn.yanque.common.api.PageResult;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.common.threadlocal.StudentThreadLocal;
import cn.yanque.models.exam.exam.mapper.ExamMapper;
import cn.yanque.models.exam.exam.mapper.StudentExamAnswerMapper;
import cn.yanque.models.exam.exam.mapper.StudentExamRecordMapper;
import cn.yanque.models.exam.exam.pojo.entity.ExamEntity;
import cn.yanque.models.exam.exam.pojo.entity.StudentExamAnswerEntity;
import cn.yanque.models.exam.exam.pojo.entity.StudentExamRecordEntity;
import cn.yanque.models.exam.paper.mapper.ExamPaperMapper;
import cn.yanque.models.exam.paper.mapper.ExamPaperQuestionMapper;
import cn.yanque.models.exam.paper.pojo.entity.ExamPaperEntity;
import cn.yanque.models.exam.paper.pojo.entity.ExamPaperQuestionEntity;
import cn.yanque.models.exam.question.mapper.ExamQuestionMapper;
import cn.yanque.models.exam.question.mapper.ExamQuestionOptionMapper;
import cn.yanque.models.exam.question.pojo.entity.ExamQuestionEntity;
import cn.yanque.models.exam.question.pojo.entity.ExamQuestionOptionEntity;
import cn.yanque.models.student.mapper.StudentMapper;
import cn.yanque.models.student.pojo.entity.StudentEntity;
import cn.yanque.models.teaching.clazz.mapper.ClazzMapper;
import cn.yanque.models.teaching.clazz.pojo.entity.ClazzEntity;
import cn.yanque.models.users.mapper.SysUserMapper;
import cn.yanque.models.users.pojo.entity.SysUserEntity;
import cn.yanque.studentFront.pojo.req.StudentExamAnswerReq;
import cn.yanque.studentFront.pojo.req.StudentExamPageReq;
import cn.yanque.studentFront.pojo.req.StudentExamSubmitReq;
import cn.yanque.studentFront.pojo.res.StudentExamPageRes;
import cn.yanque.studentFront.pojo.res.StudentExamPaperOptionRes;
import cn.yanque.studentFront.pojo.res.StudentExamPaperQuestionRes;
import cn.yanque.studentFront.pojo.res.StudentExamPaperRes;
import cn.yanque.studentFront.pojo.res.StudentExamStartRes;
import cn.yanque.studentFront.pojo.res.StudentExamSubmissionQuestionRes;
import cn.yanque.studentFront.pojo.res.StudentExamSubmissionRes;
import cn.yanque.studentFront.pojo.res.StudentExamSubmitRes;
import cn.yanque.studentFront.service.StudentExamService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StudentExamServiceImpl implements StudentExamService {

    private static final String STATUS_IN_PROGRESS = "IN_PROGRESS";

    private static final String STATUS_SUBMITTED = "SUBMITTED";

    private static final String GRADING_STATUS_PENDING = "PENDING";

    private static final String GRADING_STATUS_COMPLETED = "COMPLETED";

    private static final List<String> OBJECTIVE_QUESTION_TYPES = List.of("SINGLE", "MULTIPLE", "JUDGE");

    private static final String VIEW_STATUS_NOT_STARTED = "NOT_STARTED";

    private static final String VIEW_STATUS_AVAILABLE = "AVAILABLE";

    private static final String VIEW_STATUS_IN_PROGRESS = "IN_PROGRESS";

    private static final String VIEW_STATUS_SUBMITTED = "SUBMITTED";

    private static final String VIEW_STATUS_TIMEOUT = "TIMEOUT";

    private static final String VIEW_STATUS_ENDED = "ENDED";

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private StudentExamRecordMapper studentExamRecordMapper;

    @Autowired
    private StudentExamAnswerMapper studentExamAnswerMapper;

    @Autowired
    private ExamPaperMapper examPaperMapper;

    @Autowired
    private ExamPaperQuestionMapper examPaperQuestionMapper;

    @Autowired
    private ExamQuestionMapper examQuestionMapper;

    @Autowired
    private ExamQuestionOptionMapper examQuestionOptionMapper;

    @Autowired
    private ClazzMapper clazzMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public PageResult<StudentExamPageRes> pageExam(StudentExamPageReq req) {
        StudentEntity student = validateStudent(StudentThreadLocal.get().getId());
        if (student.getClassId() == null) {
            return new PageResult<>(0L, req.getPageNum(), req.getPageSize(), List.of());
        }

        int pageNum = req.getPageNum() == null ? 1 : req.getPageNum();
        int pageSize = req.getPageSize() == null ? 10 : req.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<ExamEntity> exams = examMapper.selectStudentPage(student.getClassId());
        PageInfo<ExamEntity> pageInfo = new PageInfo<>(exams);
        List<StudentExamPageRes> records = buildPageRecords(exams, student);
        return new PageResult<>(pageInfo.getTotal(), pageNum, pageSize, records);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentExamStartRes startExam(Long examId) {

        // 查询学生信息
        StudentEntity student = validateStudent(StudentThreadLocal.get().getId());

        // 查询考试信息
        ExamEntity exam = validateStudentExam(student, examId);
        Date now = new Date();
        if (exam.getStartTime().after(now)) {
            throw BusinessException.DateError.newInstance("考试暂未开始");
        }
        if (exam.getEndTime().before(now)) {
            throw BusinessException.DateError.newInstance("考试进入时间已结束");
        }

        // 查询学生提交信息
        StudentExamRecordEntity record = studentExamRecordMapper.selectByExamIdAndStudentId(examId, student.getId());
        if (record != null) {
            if (STATUS_SUBMITTED.equals(record.getStatus())) {
                throw BusinessException.DateError.newInstance("考试已提交");
            }
            if (record.getDeadlineTime() != null && record.getDeadlineTime().before(now)) {
                throw BusinessException.DateError.newInstance("考试已超时");
            }
            return buildStartRes(exam, record);
        }

        record = new StudentExamRecordEntity();
        record.setExamId(examId);
        record.setStudentId(student.getId());
        record.setStartTime(now);
        record.setDeadlineTime(buildDeadlineTime(now, exam));
        record.setStatus(STATUS_IN_PROGRESS);
        record.setGradingStatus(GRADING_STATUS_PENDING);
        record.setCreatedAt(now);
        record.setUpdatedAt(now);
        studentExamRecordMapper.insert(record);
        return buildStartRes(exam, record);
    }

    @Override
    public StudentExamPaperRes getExamPaper(Long recordId) {
        StudentEntity student = validateStudent(StudentThreadLocal.get().getId());
        StudentExamRecordEntity record = studentExamRecordMapper.selectById(recordId);
        if (record == null || !student.getId().equals(record.getStudentId())) {
            throw BusinessException.DateError.newInstance("考试记录不存在");
        }
        if (STATUS_SUBMITTED.equals(record.getStatus())) {
            throw BusinessException.DateError.newInstance("考试已提交");
        }
        Date now = new Date();
        if (record.getDeadlineTime() != null && record.getDeadlineTime().before(now)) {
            throw BusinessException.DateError.newInstance("考试已超时");
        }

        ExamEntity exam = validateStudentExam(student, record.getExamId());
        ExamPaperEntity paper = examPaperMapper.selectById(exam.getPaperId());
        if (paper == null) {
            throw BusinessException.DateError.newInstance("试卷不存在");
        }

        StudentExamPaperRes res = new StudentExamPaperRes();
        res.setRecordId(record.getId());
        res.setExamId(exam.getId());
        res.setPaperId(paper.getId());
        res.setPaperName(paper.getPaperName());
        res.setClassId(exam.getClassId());
        ClazzEntity clazz = clazzMapper.selectById(exam.getClassId());
        res.setClassPeriod(clazz == null ? null : clazz.getClassPeriod());
        res.setCourseId(paper.getCourseId());
        res.setStageName(paper.getStageName());
        res.setTotalScore(paper.getTotalScore());
        res.setStartTime(record.getStartTime());
        res.setDeadlineTime(record.getDeadlineTime());
        res.setRecordStatus(record.getStatus());
        res.setQuestions(buildPaperQuestions(paper.getId()));
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentExamSubmitRes submitExam(Long recordId, StudentExamSubmitReq req) {

        // 校验学生信息
        StudentEntity student = validateStudent(StudentThreadLocal.get().getId());
        StudentExamRecordEntity record = studentExamRecordMapper.selectById(recordId);
        if (record == null || !student.getId().equals(record.getStudentId())) {
            throw BusinessException.DateError.newInstance("考试记录不存在");
        }
        if (STATUS_SUBMITTED.equals(record.getStatus())) {
            throw BusinessException.DateError.newInstance("考试已提交");
        }


        Date now = new Date();
        if (record.getDeadlineTime() != null && record.getDeadlineTime().before(now)) {
            throw BusinessException.DateError.newInstance("考试已超时,不能交卷");
        }

        // 查询考试信息
        ExamEntity exam = validateStudentExam(student, record.getExamId());

        // 查询试卷题目
        List<ExamPaperQuestionEntity> paperQuestions = examPaperQuestionMapper.selectByPaperId(exam.getPaperId());
        if (paperQuestions.isEmpty()) {
            throw BusinessException.DateError.newInstance("试卷题目为空");
        }

        // 试卷题目分组 key题目id value题目
        Map<Long, ExamPaperQuestionEntity> paperQuestionMap = paperQuestions.stream()
                .collect(Collectors.toMap(ExamPaperQuestionEntity::getQuestionId, Function.identity()));

        // 交卷答题分组 key题目id value 回答
        Map<Long, String> answerMap = req.getAnswers().stream()
                .collect(Collectors.toMap(StudentExamAnswerReq::getQuestionId, item -> normalizeBlank(item.getAnswerContent()), (oldValue, newValue) -> newValue));

        // 校验题目是否都属于试卷题目
        validateSubmitQuestions(answerMap, paperQuestionMap);

        // 查询题目详情 key 题目id value 题目详情
        Map<Long, ExamQuestionEntity> questionMap = examQuestionMapper.selectByIds(paperQuestionMap.keySet().stream().toList()).stream()
                .collect(Collectors.toMap(ExamQuestionEntity::getId, Function.identity()));

        // 组装学生答题表
        List<StudentExamAnswerEntity> answers = paperQuestions.stream()
                .map(paperQuestion -> buildExamAnswer(record, exam, paperQuestion, questionMap.get(paperQuestion.getQuestionId()),
                        answerMap.get(paperQuestion.getQuestionId()), now))
                .toList();
        BigDecimal totalScore = answers.stream()
                .map(StudentExamAnswerEntity::getScore)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        studentExamAnswerMapper.deleteByRecordId(recordId);
        studentExamAnswerMapper.insertBatch(answers);

        record.setStatus(STATUS_SUBMITTED);
        record.setGradingStatus(buildGradingStatus(answers));
        record.setSubmitTime(now);
        record.setScore(totalScore);
        record.setUpdatedAt(now);
        int rows = studentExamRecordMapper.updateSubmit(record);
        if (rows == 0) {
            throw BusinessException.DateError.newInstance("考试状态已变化,请刷新后重试");
        }

        StudentExamSubmitRes res = new StudentExamSubmitRes();
        res.setRecordId(record.getId());
        res.setExamId(record.getExamId());
        res.setStatus(record.getStatus());
        res.setGradingStatus(record.getGradingStatus());
        res.setSubmitTime(record.getSubmitTime());
        res.setScore(Boolean.TRUE.equals(exam.getAnswerVisible()) ? record.getScore() : null);
        return res;
    }

    @Override
    public StudentExamSubmissionRes getSubmission(Long recordId) {
        StudentEntity student = validateStudent(StudentThreadLocal.get().getId());
        StudentExamRecordEntity record = studentExamRecordMapper.selectById(recordId);
        if (record == null || !student.getId().equals(record.getStudentId())) {
            throw BusinessException.DateError.newInstance("考试记录不存在");
        }
        if (!STATUS_SUBMITTED.equals(record.getStatus())) {
            throw BusinessException.DateError.newInstance("考试尚未提交");
        }
        ExamEntity exam = validateStudentExam(student, record.getExamId());
        ExamPaperEntity paper = examPaperMapper.selectById(exam.getPaperId());
        if (paper == null) {
            throw BusinessException.DateError.newInstance("试卷不存在");
        }

        StudentExamSubmissionRes res = new StudentExamSubmissionRes();
        res.setRecordId(record.getId());
        res.setExamId(exam.getId());
        res.setPaperId(paper.getId());
        res.setPaperName(paper.getPaperName());
        ClazzEntity clazz = clazzMapper.selectById(exam.getClassId());
        res.setClassPeriod(clazz == null ? null : clazz.getClassPeriod());
        res.setStageName(paper.getStageName());
        res.setTotalScore(paper.getTotalScore());
        boolean answerVisible = Boolean.TRUE.equals(exam.getAnswerVisible());
        res.setScore(answerVisible ? record.getScore() : null);
        res.setRecordStatus(record.getStatus());
        res.setGradingStatus(record.getGradingStatus());
        res.setAnswerVisible(answerVisible);
        res.setSubmitTime(record.getSubmitTime());
        res.setQuestions(buildSubmissionQuestions(record.getId(), paper.getId(), answerVisible));
        return res;
    }

    private List<StudentExamPageRes> buildPageRecords(List<ExamEntity> exams, StudentEntity student) {
        List<Long> examIds = exams.stream().map(ExamEntity::getId).toList();
        Map<Long, StudentExamRecordEntity> recordMap = examIds.isEmpty() ? Map.of() :
                studentExamRecordMapper.selectByExamIdsAndStudentId(examIds, student.getId()).stream()
                        .collect(Collectors.toMap(StudentExamRecordEntity::getExamId, Function.identity()));
        Map<Long, ExamPaperEntity> paperMap = buildPaperMap(exams);
        Map<Long, SysUserEntity> userMap = buildUserMap(exams);
        ClazzEntity clazz = clazzMapper.selectById(student.getClassId());
        Date now = new Date();

        return exams.stream().map(exam -> {
            ExamPaperEntity paper = paperMap.get(exam.getPaperId());
            StudentExamRecordEntity record = recordMap.get(exam.getId());
        StudentExamPageRes res = new StudentExamPageRes();
            res.setId(exam.getId());
            res.setPaperId(exam.getPaperId());
            res.setPaperName(paper == null ? null : paper.getPaperName());
            res.setClassId(exam.getClassId());
            res.setClassPeriod(clazz == null ? null : clazz.getClassPeriod());
            res.setCourseId(paper == null ? null : paper.getCourseId());
            res.setStageName(paper == null ? null : paper.getStageName());
            res.setStartTime(exam.getStartTime());
            res.setEndTime(exam.getEndTime());
            res.setDurationMinutes(exam.getDurationMinutes());
            res.setInvigilatorUserId(exam.getInvigilatorUserId());
            res.setInvigilatorName(buildUserName(userMap.get(exam.getInvigilatorUserId())));
            res.setTotalScore(paper == null ? null : paper.getTotalScore());
            fillRecord(res, record);
            if (record != null && STATUS_SUBMITTED.equals(record.getStatus()) && !Boolean.TRUE.equals(exam.getAnswerVisible())) {
                res.setScore(null);
            }
            fillViewStatus(res, exam, record, now);
            return res;
        }).toList();
    }

    private Map<Long, ExamPaperEntity> buildPaperMap(List<ExamEntity> exams) {
        List<Long> paperIds = exams.stream().map(ExamEntity::getPaperId).distinct().toList();
        if (paperIds.isEmpty()) {
            return Map.of();
        }
        return examPaperMapper.selectByIds(paperIds).stream()
                .collect(Collectors.toMap(ExamPaperEntity::getId, Function.identity()));
    }

    private Map<Long, SysUserEntity> buildUserMap(List<ExamEntity> exams) {
        List<Long> userIds = exams.stream().map(ExamEntity::getInvigilatorUserId).distinct().toList();
        if (userIds.isEmpty()) {
            return Map.of();
        }
        return sysUserMapper.selectByIds(userIds).stream()
                .collect(Collectors.toMap(SysUserEntity::getId, Function.identity()));
    }

    private void fillRecord(StudentExamPageRes res, StudentExamRecordEntity record) {
        if (record == null) {
            return;
        }
        res.setRecordId(record.getId());
        res.setRecordStatus(record.getStatus());
        res.setRecordStartTime(record.getStartTime());
        res.setDeadlineTime(record.getDeadlineTime());
        res.setSubmitTime(record.getSubmitTime());
    }

    private void fillViewStatus(StudentExamPageRes res, ExamEntity exam, StudentExamRecordEntity record, Date now) {
        if (record != null && STATUS_SUBMITTED.equals(record.getStatus())) {
            String text = Boolean.TRUE.equals(exam.getAnswerVisible())
                    ? (GRADING_STATUS_COMPLETED.equals(record.getGradingStatus()) ? "已批改" : "待批改")
                    : "已提交";
            setViewStatus(res, VIEW_STATUS_SUBMITTED, text, false);
            return;
        }
        if (record != null && record.getDeadlineTime() != null && record.getDeadlineTime().before(now)) {
            setViewStatus(res, VIEW_STATUS_TIMEOUT, "已超时", false);
            return;
        }
        if (record != null) {
            setViewStatus(res, VIEW_STATUS_IN_PROGRESS, "进行中", true);
            return;
        }
        if (exam.getStartTime().after(now)) {
            setViewStatus(res, VIEW_STATUS_NOT_STARTED, "未开始", false);
            return;
        }
        if (exam.getEndTime().before(now)) {
            setViewStatus(res, VIEW_STATUS_ENDED, "已结束", false);
            return;
        }
        setViewStatus(res, VIEW_STATUS_AVAILABLE, "可开始", true);
    }

    private void setViewStatus(StudentExamPageRes res, String status, String text, boolean canStart) {
        res.setExamStatus(status);
        res.setExamStatusText(text);
        res.setCanStart(canStart);
    }

    private StudentExamStartRes buildStartRes(ExamEntity exam, StudentExamRecordEntity record) {
        StudentExamStartRes res = new StudentExamStartRes();
        res.setRecordId(record.getId());
        res.setExamId(exam.getId());
        res.setPaperId(exam.getPaperId());
        res.setStartTime(record.getStartTime());
        res.setDeadlineTime(record.getDeadlineTime());
        res.setStatus(record.getStatus());
        return res;
    }

    private List<StudentExamPaperQuestionRes> buildPaperQuestions(Long paperId) {
        List<ExamPaperQuestionEntity> paperQuestions = examPaperQuestionMapper.selectByPaperId(paperId);
        List<Long> questionIds = paperQuestions.stream().map(ExamPaperQuestionEntity::getQuestionId).distinct().toList();
        if (questionIds.isEmpty()) {
            return List.of();
        }
        Map<Long, ExamQuestionEntity> questionMap = examQuestionMapper.selectByIds(questionIds).stream()
                .collect(Collectors.toMap(ExamQuestionEntity::getId, Function.identity()));
        Map<Long, List<ExamQuestionOptionEntity>> optionMap = examQuestionOptionMapper.selectByQuestionIds(questionIds).stream()
                .collect(Collectors.groupingBy(ExamQuestionOptionEntity::getQuestionId));
        return paperQuestions.stream().map(paperQuestion -> {
            ExamQuestionEntity question = questionMap.get(paperQuestion.getQuestionId());
            StudentExamPaperQuestionRes res = new StudentExamPaperQuestionRes();
            res.setId(paperQuestion.getId());
            res.setQuestionId(paperQuestion.getQuestionId());
            res.setQuestionScore(paperQuestion.getQuestionScore());
            if (question != null) {
                res.setQuestionContent(question.getQuestionContent());
                res.setQuestionType(question.getQuestionType());
                res.setDifficulty(question.getDifficulty());
                res.setOptions(buildQuestionOptions(optionMap.getOrDefault(question.getId(), List.of())));
            } else {
                res.setOptions(List.of());
            }
            return res;
        }).toList();
    }

    private List<StudentExamPaperOptionRes> buildQuestionOptions(List<ExamQuestionOptionEntity> options) {
        return options.stream().map(option -> {
            StudentExamPaperOptionRes res = new StudentExamPaperOptionRes();
            res.setId(option.getId());
            res.setQuestionId(option.getQuestionId());
            res.setOptionKey(option.getOptionKey());
            res.setOptionContent(option.getOptionContent());
            return res;
        }).toList();
    }

    private List<StudentExamSubmissionQuestionRes> buildSubmissionQuestions(Long recordId, Long paperId, boolean answerVisible) {
        List<StudentExamAnswerEntity> answers = studentExamAnswerMapper.selectByRecordId(recordId);
        if (answers.isEmpty()) {
            return List.of();
        }
        Map<Long, StudentExamAnswerEntity> answerMap = answers.stream()
                .collect(Collectors.toMap(StudentExamAnswerEntity::getQuestionId, Function.identity()));
        List<ExamPaperQuestionEntity> paperQuestions = examPaperQuestionMapper.selectByPaperId(paperId);
        List<Long> questionIds = paperQuestions.stream().map(ExamPaperQuestionEntity::getQuestionId).distinct().toList();
        Map<Long, ExamQuestionEntity> questionMap = examQuestionMapper.selectByIds(questionIds).stream()
                .collect(Collectors.toMap(ExamQuestionEntity::getId, Function.identity()));
        Map<Long, List<ExamQuestionOptionEntity>> optionMap = examQuestionOptionMapper.selectByQuestionIds(questionIds).stream()
                .collect(Collectors.groupingBy(ExamQuestionOptionEntity::getQuestionId));
        return paperQuestions.stream().map(paperQuestion -> {
            StudentExamAnswerEntity answer = answerMap.get(paperQuestion.getQuestionId());
            ExamQuestionEntity question = questionMap.get(paperQuestion.getQuestionId());
            StudentExamSubmissionQuestionRes res = new StudentExamSubmissionQuestionRes();
            res.setId(paperQuestion.getId());
            res.setQuestionId(paperQuestion.getQuestionId());
            res.setQuestionScore(paperQuestion.getQuestionScore());
            if (question != null) {
                res.setQuestionContent(question.getQuestionContent());
                res.setQuestionType(question.getQuestionType());
                res.setOptions(buildQuestionOptions(optionMap.getOrDefault(question.getId(), List.of())));
            } else {
                res.setOptions(List.of());
            }
            if (answer != null) {
                res.setAnswerContent(answer.getAnswerContent());
                if (answerVisible) {
                    res.setCorrect(answer.getCorrect());
                    res.setScore(answer.getScore());
                }
            }
            return res;
        }).toList();
    }

    private String buildGradingStatus(List<StudentExamAnswerEntity> answers) {
        boolean allObjective = answers.stream()
                .allMatch(answer -> OBJECTIVE_QUESTION_TYPES.contains(answer.getQuestionType()));
        return allObjective ? GRADING_STATUS_COMPLETED : GRADING_STATUS_PENDING;
    }

    private void validateSubmitQuestions(Map<Long, String> answerMap, Map<Long, ExamPaperQuestionEntity> paperQuestionMap) {
        List<Long> invalidQuestionIds = answerMap.keySet().stream()
                .filter(questionId -> !paperQuestionMap.containsKey(questionId))
                .toList();
        if (!invalidQuestionIds.isEmpty()) {
            throw BusinessException.DateError.newInstance("提交了不属于当前试卷的题目:" + invalidQuestionIds.get(0));
        }
    }

    private StudentExamAnswerEntity buildExamAnswer(StudentExamRecordEntity record,
                                                    ExamEntity exam,
                                                    ExamPaperQuestionEntity paperQuestion,
                                                    ExamQuestionEntity question,
                                                    String answerContent,
                                                    Date now) {
        if (question == null) {
            throw BusinessException.DateError.newInstance("题目不存在:" + paperQuestion.getQuestionId());
        }
        StudentExamAnswerEntity answer = new StudentExamAnswerEntity();
        answer.setRecordId(record.getId());
        answer.setExamId(exam.getId());
        answer.setPaperId(exam.getPaperId());
        answer.setQuestionId(paperQuestion.getQuestionId());
        answer.setQuestionType(question.getQuestionType());
        answer.setQuestionScore(paperQuestion.getQuestionScore());
        answer.setAnswerContent(answerContent);
        fillAutoScore(answer, question);
        answer.setCreatedAt(now);
        answer.setUpdatedAt(now);
        return answer;
    }

    private void fillAutoScore(StudentExamAnswerEntity answer, ExamQuestionEntity question) {
        if (!OBJECTIVE_QUESTION_TYPES.contains(question.getQuestionType())) {
            answer.setCorrect(null);
            answer.setScore(null);
            return;
        }
        boolean correct = normalizeAnswer(answer.getAnswerContent()).equals(normalizeAnswer(question.getAnswerContent()));
        answer.setCorrect(correct);
        answer.setScore(correct ? answer.getQuestionScore() : BigDecimal.ZERO);
    }

    private String normalizeAnswer(String value) {
        if (!StringUtils.hasText(value)) {
            return "";
        }
        return Arrays.stream(value.split("[,,]"))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.joining(","));
    }

    private String normalizeBlank(String value) {
        return StringUtils.hasText(value) ? value.trim() : "";
    }

    private Date buildDeadlineTime(Date startTime, ExamEntity exam) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.MINUTE, exam.getDurationMinutes());
        Date personalDeadline = calendar.getTime();
        return personalDeadline.after(exam.getEndTime()) ? exam.getEndTime() : personalDeadline;
    }

    private StudentEntity validateStudent(Long studentId) {
        StudentEntity student = studentMapper.selectById(studentId);
        if (student == null) {
            throw BusinessException.DateError.newInstance("学生不存在");
        }
        return student;
    }

    private ExamEntity validateStudentExam(StudentEntity student, Long examId) {
        ExamEntity exam = examMapper.selectById(examId);
        if (exam == null || student.getClassId() == null || !student.getClassId().equals(exam.getClassId())) {
            throw BusinessException.DateError.newInstance("考试不存在");
        }
        return exam;
    }

    private String buildUserName(SysUserEntity user) {
        if (user == null) {
            return null;
        }
        if (StringUtils.hasText(user.getRealName())) {
            return user.getRealName();
        }
        if (StringUtils.hasText(user.getNickname())) {
            return user.getNickname();
        }
        return user.getUsername();
    }
}
