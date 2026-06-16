package cn.yanque.models.exam.paper.mapper;

import cn.yanque.models.exam.paper.pojo.bo.QueryExamPaperBo;
import cn.yanque.models.exam.paper.pojo.entity.ExamPaperEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 试卷数据访问接口。
 */
public interface ExamPaperMapper {

    void insert(ExamPaperEntity paper);

    ExamPaperEntity selectById(@Param("id") Long id);

    List<ExamPaperEntity> selectByIds(@Param("ids") List<Long> ids);

    List<ExamPaperEntity> selectPage(QueryExamPaperBo query);

    int deleteById(@Param("id") Long id);
}
