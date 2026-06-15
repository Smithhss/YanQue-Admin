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

    List<StudentEntity> selectPage(QueryStudentBo queryStudentBo);
}
