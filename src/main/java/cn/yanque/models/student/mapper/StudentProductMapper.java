package cn.yanque.models.student.mapper;

import cn.yanque.models.student.pojo.entity.StudentProductEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学生产品关联数据访问接口。
 */
public interface StudentProductMapper {

    int insert(StudentProductEntity studentProduct);

    List<StudentProductEntity> selectByStudentIds(@Param("studentIds") List<Long> studentIds);
}
