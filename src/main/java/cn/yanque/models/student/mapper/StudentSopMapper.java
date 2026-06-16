package cn.yanque.models.student.mapper;

import cn.yanque.models.student.pojo.entity.StudentSopEntity;
import cn.yanque.models.student.pojo.bo.QueryStudentSopBo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学生入学SOP数据访问接口。
 */
public interface StudentSopMapper {

    int insert(StudentSopEntity studentSop);

    StudentSopEntity selectById(@Param("id") Long id);

    StudentSopEntity selectActiveByStudentId(@Param("studentId") Long studentId);

    List<StudentSopEntity> selectActiveByStudentIds(@Param("studentIds") List<Long> studentIds);

    List<StudentSopEntity> selectPage(QueryStudentSopBo query);

    int completeSop(StudentSopEntity studentSop);
}
