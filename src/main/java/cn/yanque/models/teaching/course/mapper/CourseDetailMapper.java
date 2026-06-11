package cn.yanque.models.teaching.course.mapper;

import cn.yanque.models.teaching.course.pojo.entity.CourseDetailEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 课程详情表数据访问接口。
 */
public interface CourseDetailMapper {

    /**
     * 插入课程详情。
     *
     * @param detail 课程详情实体
     */
    void insert(CourseDetailEntity detail);

    /**
     * 按ID更新课程详情。
     *
     * @param detail 课程详情实体
     * @return 影响行数
     */
    int updateById(CourseDetailEntity detail);

    /**
     * 按ID查询课程详情。
     *
     * @param id 课程详情ID
     * @return 课程详情实体
     */
    CourseDetailEntity selectById(@Param("id") Long id);

    /**
     * 查询某个课程下的全部详情。
     *
     * @param courseId 课程ID
     * @return 课程详情列表
     */
    List<CourseDetailEntity> selectByCourseId(@Param("courseId") Long courseId);

    /**
     * 按ID删除课程详情。
     *
     * @param id 课程详情ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 删除某个课程下的全部详情。
     *
     * @param courseId 课程ID
     * @return 影响行数
     */
    int deleteByCourseId(@Param("courseId") Long courseId);

    /**
     * 批量插入
     */
    void batchInsert(List<CourseDetailEntity> courseDetailEntityList);

}
