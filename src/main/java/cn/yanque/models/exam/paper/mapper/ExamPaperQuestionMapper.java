package cn.yanque.models.exam.paper.mapper;

import cn.yanque.models.exam.paper.pojo.entity.ExamPaperQuestionEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 试卷题目关联数据访问接口。
 */
public interface ExamPaperQuestionMapper {

    void insertBatch(@Param("questions") List<ExamPaperQuestionEntity> questions);

    List<ExamPaperQuestionEntity> selectByPaperId(@Param("paperId") Long paperId);

    List<ExamPaperQuestionEntity> selectByPaperIds(@Param("paperIds") List<Long> paperIds);

    int deleteByPaperId(@Param("paperId") Long paperId);
}
