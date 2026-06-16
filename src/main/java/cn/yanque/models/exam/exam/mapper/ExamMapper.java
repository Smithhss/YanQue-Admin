package cn.yanque.models.exam.exam.mapper;

import cn.yanque.models.exam.exam.pojo.bo.QueryExamBo;
import cn.yanque.models.exam.exam.pojo.entity.ExamEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 考试安排数据访问接口。
 */
public interface ExamMapper {

    void insert(ExamEntity exam);

    int updateById(ExamEntity exam);

    int updateAnswerVisible(@Param("id") Long id, @Param("answerVisible") Boolean answerVisible);

    ExamEntity selectById(@Param("id") Long id);

    List<ExamEntity> selectPage(QueryExamBo query);

    List<ExamEntity> selectStudentPage(@Param("classId") Long classId);

    int countClassTimeOverlap(@Param("id") Long id,
                              @Param("classId") Long classId,
                              @Param("startTime") Date startTime,
                              @Param("endTime") Date endTime);

    int deleteById(@Param("id") Long id);
}
