package cn.yanque.models.student.mapper;

import cn.yanque.models.student.followup.pojo.bo.QueryStudentFollowupTagBo;
import cn.yanque.models.student.followup.pojo.entity.StudentFollowupTagEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StudentFollowupTagMapper {

    void insert(StudentFollowupTagEntity entity);

    int updateById(StudentFollowupTagEntity entity);

    int deleteById(@Param("id") Long id);

    StudentFollowupTagEntity selectById(@Param("id") Long id);

    StudentFollowupTagEntity selectByStudentTag(@Param("studentTag") String studentTag);

    StudentFollowupTagEntity selectByStudentTagExcludeId(@Param("studentTag") String studentTag, @Param("id") Long id);

    List<StudentFollowupTagEntity> selectActiveList();

    List<StudentFollowupTagEntity> selectPage(QueryStudentFollowupTagBo query);
}
