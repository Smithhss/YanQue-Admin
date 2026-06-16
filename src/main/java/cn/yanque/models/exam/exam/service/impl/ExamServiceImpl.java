package cn.yanque.models.exam.exam.service.impl;

import cn.yanque.common.api.PageResult;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.exam.exam.mapper.ExamMapper;
import cn.yanque.models.exam.exam.mapper.StudentExamAnswerMapper;
import cn.yanque.models.exam.exam.mapper.StudentExamRecordMapper;
import cn.yanque.models.exam.exam.pojo.bo.QueryExamBo;
import cn.yanque.models.exam.exam.pojo.entity.ExamEntity;
import cn.yanque.models.exam.exam.pojo.entity.StudentExamAnswerEntity;
import cn.yanque.models.exam.exam.pojo.entity.StudentExamRecordEntity;
import cn.yanque.models.exam.exam.pojo.vo.req.ExamSubmissionGradeAnswerReq;
import cn.yanque.models.exam.exam.pojo.vo.req.ExamSubmissionGradeReq;
import cn.yanque.models.exam.exam.pojo.vo.res.ExamDetailRes;
import cn.yanque.models.exam.exam.pojo.vo.res.ExamPageRes;
import cn.yanque.models.exam.exam.pojo.vo.res.ExamSubmissionDetailRes;
import cn.yanque.models.exam.exam.pojo.vo.res.ExamSubmissionGradeRes;
import cn.yanque.models.exam.exam.pojo.vo.res.ExamSubmissionOptionRes;
import cn.yanque.models.exam.exam.pojo.vo.res.ExamSubmissionPageRes;
import cn.yanque.models.exam.exam.pojo.vo.res.ExamSubmissionQuestionRes;
import cn.yanque.models.exam.exam.service.ExamService;
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
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ExamServiceImpl implements ExamService {

    private static final String STATUS_IN_PROGRESS = "IN_PROGRESS";

    private static final String STATUS_SUBMITTED = "SUBMITTED";

    private static final String GRADING_STATUS_PENDING = "PENDING";

    private static final String GRADING_STATUS_COMPLETED = "COMPLETED";

    private static final List<String> OBJECTIVE_QUESTION_TYPES = List.of("SINGLE", "MULTIPLE", "JUDGE");

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
    private StudentMapper studentMapper;

    @Autowired
    private ClazzMapper clazzMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createExam(ExamEntity exam) {
        validateExam(exam);
        Date now = new Date();
        if (exam.getAnswerVisible() == null) {
            exam.setAnswerVisible(false);
        }
        exam.setCreatedAt(now);
        exam.setUpdatedAt(now);
        examMapper.insert(exam);
        return exam.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long updateExam(ExamEntity exam) {
        validateExam(exam);
        exam.setUpdatedAt(new Date());
        int rows = examMapper.updateById(exam);
        if (rows == 0) {
            throw BusinessException.DateError.newInstance("考试不存在");
        }
        return exam.getId();
    }

    @Override
    public PageResult<ExamPageRes> pageExam(QueryExamBo query, Integer pageNum, Integer pageSize) {
        int currentPage = pageNum == null ? 1 : pageNum;
        int currentSize = pageSize == null ? 10 : pageSize;
        PageHelper.startPage(currentPage, currentSize);
        List<ExamEntity> list = examMapper.selectPage(query);
        PageInfo<ExamEntity> pageInfo = new PageInfo<>(list);
        List<ExamPageRes> records = list.stream().map(this::buildPageRes).toList();
        fillNames(records);
        return new PageResult<>(pageInfo.getTotal(), currentPage, currentSize, records);
    }

    @Override
    public ExamDetailRes getExamById(Long id) {
        ExamEntity exam = examMapper.selectById(id);
        if (exam == null) {
            throw BusinessException.DateError.newInstance("考试不存在");
        }
        ExamDetailRes res = new ExamDetailRes();
        BeanUtils.copyProperties(exam, res);
        fillNames(List.of(res));
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long deleteExam(Long id) {
        int rows = examMapper.deleteById(id);
        if (rows == 0) {
            throw BusinessException.DateError.newInstance("考试不存在");
        }
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long updateAnswerVisible(Long id, Boolean answerVisible) {
        int rows = examMapper.updateAnswerVisible(id, answerVisible);
        if (rows == 0) {
            throw BusinessException.DateError.newInstance("考试不存在");
        }
        return id;
    }

    @Override
    public PageResult<ExamSubmissionPageRes> pageSubmissions(Long examId, Integer pageNum, Integer pageSize) {

        // 校验考试信息
        ExamEntity exam = getRequiredExam(examId);
        int currentPage = pageNum == null ? 1 : pageNum;
        int currentSize = pageSize == null ? 10 : pageSize;

        PageHelper.startPage(currentPage, currentSize);
        // 分页查询学生信息
        List<StudentEntity> students = studentMapper.selectByClassId(exam.getClassId());
        PageInfo<StudentEntity> pageInfo = new PageInfo<>(students);

        // 查询学生考试记录
        Map<Long, StudentExamRecordEntity> recordMap = studentExamRecordMapper.selectByExamId(examId).stream()
                .collect(Collectors.toMap(StudentExamRecordEntity::getStudentId, Function.identity()));
        Date now = new Date();
        List<ExamSubmissionPageRes> records = students.stream()
                .map(student -> buildSubmissionPageRes(exam, student, recordMap.get(student.getId()), now))
                .toList();
        return new PageResult<>(pageInfo.getTotal(), currentPage, currentSize, records);
    }

    @Override
    public ExamSubmissionDetailRes getSubmissionDetail(Long recordId) {

        // 根据学生考试记录查询记录信息
        StudentExamRecordEntity record = getRequiredRecord(recordId);
        if (!STATUS_SUBMITTED.equals(record.getStatus())) {
            throw BusinessException.DateError.newInstance("考试尚未提交，不能查看答卷");
        }

        // 查看考试信息
        ExamEntity exam = getRequiredExam(record.getExamId());

        // 查询试卷信息
        ExamPaperEntity paper = getRequiredPaper(exam.getPaperId());

        // 查询学生信息
        StudentEntity student = studentMapper.selectById(record.getStudentId());

        // 查询班级信息
        ClazzEntity clazz = clazzMapper.selectById(exam.getClassId());

        ExamSubmissionDetailRes res = new ExamSubmissionDetailRes();
        res.setRecordId(record.getId());
        res.setExamId(exam.getId());
        res.setPaperId(paper.getId());
        res.setPaperName(paper.getPaperName());
        res.setClassPeriod(clazz == null ? null : clazz.getClassPeriod());
        res.setStageName(paper.getStageName());
        res.setStudentId(record.getStudentId());
        if (student != null) {
            res.setStudentName(student.getStudentName());
            res.setStudentPhone(student.getStudentPhone());
        }
        res.setTotalScore(paper.getTotalScore());
        res.setScore(record.getScore());
        res.setRecordStatus(record.getStatus());
        res.setGradingStatus(record.getGradingStatus());
        res.setStartTime(record.getStartTime());
        res.setDeadlineTime(record.getDeadlineTime());
        res.setSubmitTime(record.getSubmitTime());
        res.setQuestions(buildSubmissionQuestions(record.getId(), paper.getId()));
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExamSubmissionGradeRes gradeSubmission(Long recordId, ExamSubmissionGradeReq req) {
        StudentExamRecordEntity record = getRequiredRecord(recordId);
        if (!STATUS_SUBMITTED.equals(record.getStatus())) {
            throw BusinessException.DateError.newInstance("考试尚未提交，不能批改");
        }
        List<StudentExamAnswerEntity> answers = studentExamAnswerMapper.selectByRecordId(recordId);
        Map<Long, StudentExamAnswerEntity> answerMap = answers.stream()
                .collect(Collectors.toMap(StudentExamAnswerEntity::getId, Function.identity()));
        Date now = new Date();
        for (ExamSubmissionGradeAnswerReq answerReq : req.getAnswers()) {
            StudentExamAnswerEntity answer = answerMap.get(answerReq.getAnswerId());
            if (answer == null) {
                throw BusinessException.DateError.newInstance("答案记录不存在：" + answerReq.getAnswerId());
            }
            if (OBJECTIVE_QUESTION_TYPES.contains(answer.getQuestionType())) {
                throw BusinessException.DateError.newInstance("客观题由系统自动判分，不能手动批改");
            }
            if (answerReq.getScore().compareTo(answer.getQuestionScore()) > 0) {
                throw BusinessException.DateError.newInstance("题目得分不能超过题目分值");
            }
            answer.setScore(answerReq.getScore());
            answer.setUpdatedAt(now);
            studentExamAnswerMapper.updateScore(answer);
        }

        List<StudentExamAnswerEntity> latestAnswers = studentExamAnswerMapper.selectByRecordId(recordId);
        BigDecimal totalScore = latestAnswers.stream()
                .map(StudentExamAnswerEntity::getScore)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        String gradingStatus = latestAnswers.stream().allMatch(answer -> answer.getScore() != null)
                ? GRADING_STATUS_COMPLETED
                : GRADING_STATUS_PENDING;

        record.setScore(totalScore);
        record.setGradingStatus(gradingStatus);
        record.setUpdatedAt(now);
        studentExamRecordMapper.updateGrade(record);

        ExamSubmissionGradeRes res = new ExamSubmissionGradeRes();
        res.setRecordId(recordId);
        res.setScore(totalScore);
        res.setGradingStatus(gradingStatus);
        return res;
    }

    private ExamSubmissionPageRes buildSubmissionPageRes(ExamEntity exam,
                                                         StudentEntity student,
                                                         StudentExamRecordEntity record,
                                                         Date now) {
        ExamSubmissionPageRes res = new ExamSubmissionPageRes();
        res.setExamId(exam.getId());
        res.setStudentId(student.getId());
        res.setStudentName(student.getStudentName());
        res.setStudentPhone(student.getStudentPhone());
        if (record == null) {
            res.setRecordStatus("NOT_STARTED");
            res.setRecordStatusText(exam.getEndTime().before(now) ? "未参加" : "未进入");
            return res;
        }
        res.setRecordId(record.getId());
        res.setRecordStatus(record.getStatus());
        res.setRecordStatusText(buildRecordStatusText(record, now));
        res.setGradingStatus(record.getGradingStatus());
        res.setScore(record.getScore());
        res.setStartTime(record.getStartTime());
        res.setDeadlineTime(record.getDeadlineTime());
        res.setSubmitTime(record.getSubmitTime());
        return res;
    }

    private String buildRecordStatusText(StudentExamRecordEntity record, Date now) {
        if (STATUS_SUBMITTED.equals(record.getStatus())) {
            return GRADING_STATUS_COMPLETED.equals(record.getGradingStatus()) ? "已批改" : "待批改";
        }
        if (STATUS_IN_PROGRESS.equals(record.getStatus()) && record.getDeadlineTime() != null && record.getDeadlineTime().before(now)) {
            return "已超时";
        }
        if (STATUS_IN_PROGRESS.equals(record.getStatus())) {
            return "进行中";
        }
        return record.getStatus();
    }

    private List<ExamSubmissionQuestionRes> buildSubmissionQuestions(Long recordId, Long paperId) {
        List<StudentExamAnswerEntity> answers = studentExamAnswerMapper.selectByRecordId(recordId);
        Map<Long, StudentExamAnswerEntity> answerMap = answers.stream()
                .collect(Collectors.toMap(StudentExamAnswerEntity::getQuestionId, Function.identity()));
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
            StudentExamAnswerEntity answer = answerMap.get(paperQuestion.getQuestionId());
            ExamQuestionEntity question = questionMap.get(paperQuestion.getQuestionId());
            ExamSubmissionQuestionRes res = new ExamSubmissionQuestionRes();
            res.setQuestionId(paperQuestion.getQuestionId());
            res.setQuestionScore(paperQuestion.getQuestionScore());
            if (question != null) {
                res.setQuestionContent(question.getQuestionContent());
                res.setQuestionType(question.getQuestionType());
                res.setCorrectAnswer(question.getAnswerContent());
                res.setAnalysisContent(question.getAnalysisContent());
                res.setOptions(buildSubmissionOptions(optionMap.getOrDefault(question.getId(), List.of())));
            } else {
                res.setOptions(List.of());
            }
            if (answer != null) {
                res.setAnswerId(answer.getId());
                res.setAnswerContent(answer.getAnswerContent());
                res.setCorrect(answer.getCorrect());
                res.setScore(answer.getScore());
            }
            return res;
        }).toList();
    }

    private List<ExamSubmissionOptionRes> buildSubmissionOptions(List<ExamQuestionOptionEntity> options) {
        return options.stream().map(option -> {
            ExamSubmissionOptionRes res = new ExamSubmissionOptionRes();
            res.setId(option.getId());
            res.setQuestionId(option.getQuestionId());
            res.setOptionKey(option.getOptionKey());
            res.setOptionContent(option.getOptionContent());
            return res;
        }).toList();
    }

    private ExamEntity getRequiredExam(Long id) {
        ExamEntity exam = examMapper.selectById(id);
        if (exam == null) {
            throw BusinessException.DateError.newInstance("考试不存在");
        }
        return exam;
    }

    private StudentExamRecordEntity getRequiredRecord(Long id) {
        StudentExamRecordEntity record = studentExamRecordMapper.selectById(id);
        if (record == null) {
            throw BusinessException.DateError.newInstance("考试记录不存在");
        }
        return record;
    }

    private ExamPaperEntity getRequiredPaper(Long id) {
        ExamPaperEntity paper = examPaperMapper.selectById(id);
        if (paper == null) {
            throw BusinessException.DateError.newInstance("试卷不存在");
        }
        return paper;
    }

    private void validateExam(ExamEntity exam) {
        if (examPaperMapper.selectById(exam.getPaperId()) == null) {
            throw BusinessException.DateError.newInstance("试卷不存在");
        }
        if (clazzMapper.selectById(exam.getClassId()) == null) {
            throw BusinessException.ClazzNotExist;
        }
        if (sysUserMapper.selectById(exam.getInvigilatorUserId()) == null) {
            throw BusinessException.UserNotExist;
        }
        if (examMapper.countClassTimeOverlap(exam.getId(), exam.getClassId(), exam.getStartTime(), exam.getEndTime()) > 0) {
            throw BusinessException.DateError.newInstance("该班级考试时间窗口存在重叠");
        }
    }

    private ExamPageRes buildPageRes(ExamEntity exam) {
        ExamPageRes res = new ExamPageRes();
        BeanUtils.copyProperties(exam, res);
        return res;
    }

    private void fillNames(List<? extends ExamPageRes> records) {
        List<Long> paperIds = records.stream().map(ExamPageRes::getPaperId).distinct().toList();
        List<Long> classIds = records.stream().map(ExamPageRes::getClassId).distinct().toList();
        List<Long> userIds = records.stream().map(ExamPageRes::getInvigilatorUserId).distinct().toList();
        Map<Long, ExamPaperEntity> paperMap = paperIds.isEmpty() ? Map.of() : paperIds.stream()
                .map(examPaperMapper::selectById)
                .filter(paper -> paper != null)
                .collect(Collectors.toMap(ExamPaperEntity::getId, Function.identity()));
        Map<Long, ClazzEntity> classMap = classIds.isEmpty() ? Map.of() : clazzMapper.selectByIds(classIds).stream()
                .collect(Collectors.toMap(ClazzEntity::getId, Function.identity()));
        Map<Long, SysUserEntity> userMap = userIds.isEmpty() ? Map.of() : sysUserMapper.selectByIds(userIds).stream()
                .collect(Collectors.toMap(SysUserEntity::getId, Function.identity()));
        records.forEach(record -> {
            ExamPaperEntity paper = paperMap.get(record.getPaperId());
            ClazzEntity clazz = classMap.get(record.getClassId());
            SysUserEntity user = userMap.get(record.getInvigilatorUserId());
            record.setPaperName(paper == null ? null : paper.getPaperName());
            record.setClassPeriod(clazz == null ? null : clazz.getClassPeriod());
            record.setInvigilatorName(user == null ? null : buildUserName(user));
        });
    }

    private String buildUserName(SysUserEntity user) {
        if (StringUtils.hasText(user.getRealName())) {
            return user.getRealName();
        }
        if (StringUtils.hasText(user.getNickname())) {
            return user.getNickname();
        }
        return user.getUsername();
    }
}
