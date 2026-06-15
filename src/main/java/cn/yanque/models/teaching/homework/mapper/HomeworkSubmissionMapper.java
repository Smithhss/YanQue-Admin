package cn.yanque.models.teaching.homework.mapper;

import cn.yanque.models.teaching.homework.pojo.entity.HomeworkSubmissionEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学生作业提交数据访问接口。
 */
public interface HomeworkSubmissionMapper {

    int insert(HomeworkSubmissionEntity submission);

    int updateSubmit(HomeworkSubmissionEntity submission);

    int updateGrade(HomeworkSubmissionEntity submission);

    HomeworkSubmissionEntity selectById(@Param("id") Long id);

    HomeworkSubmissionEntity selectByHomeworkIdAndStudentId(@Param("homeworkId") Long homeworkId,
                                                            @Param("studentId") Long studentId);

    List<HomeworkSubmissionEntity> selectByHomeworkIdsAndStudentId(@Param("homeworkIds") List<Long> homeworkIds,
                                                                   @Param("studentId") Long studentId);

    List<HomeworkSubmissionEntity> selectByHomeworkId(@Param("homeworkId") Long homeworkId);
}
