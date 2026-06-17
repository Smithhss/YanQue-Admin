package cn.yanque.models.student.mapper;

import cn.yanque.models.student.pojo.bo.QueryStudentBo;
import cn.yanque.models.student.pojo.entity.StudentEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学生数据访问接口。
 */
public interface StudentMapper {

    int insert(StudentEntity student);

    StudentEntity selectById(@Param("id") Long id);

    StudentEntity selectByPhone(@Param("studentPhone") String studentPhone);

    List<StudentEntity> selectByIds(@Param("ids") List<Long> ids);

    List<StudentEntity> selectByClassId(@Param("classId") Long classId);

    List<StudentEntity> selectFollowupCandidates();

    List<StudentEntity> selectPage(QueryStudentBo queryStudentBo);

    int updateClassId(@Param("id") Long id, @Param("classId") Long classId);

    int updateStudentTag(@Param("id") Long id, @Param("studentTag") String studentTag);
}
