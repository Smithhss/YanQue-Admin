package cn.yanque.models.teaching.course.mapper;

import cn.yanque.models.teaching.course.pojo.entity.CourseEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 课程表数据访问接口。
 */
public interface CourseMapper {

    /**
     * 插入课程。
     *
     * @param course 课程实体
     */
    void insert(CourseEntity course);

    /**
     * 按ID更新课程。
     *
     * @param course 课程实体
     * @return 影响行数
     */
    int updateById(CourseEntity course);

    /**
     * 按ID查询课程。
     *
     * @param id 课程ID
     * @return 课程实体
     */
    CourseEntity selectById(@Param("id") Long id);

    /**
     * 按ID集合批量查询课程。
     * <p>
     * 班级列表回填课程名称时会使用这个方法。
     *
     * @param ids 课程ID集合
     * @return 课程列表
     */
    List<CourseEntity> selectByIds(@Param("ids") List<Long> ids);

    /**
     * 查询课程列表,分页由PageHelper在Service层控制。
     *
     * @param keyword 课程名称或资料路径关键字
     * @return 课程列表
     */
    List<CourseEntity> selectPage(@Param("keyword") String keyword);

    /**
     * 按ID删除课程。
     *
     * @param id 课程ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
}
