package cn.yanque.models.teaching.clazz.mapper;

import cn.yanque.models.teaching.clazz.pojo.entity.ClazzEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 班级表数据访问接口。
 */
public interface ClazzMapper {

    /**
     * 插入班级。
     *
     * @param clazz 班级实体
     */
    void insert(ClazzEntity clazz);

    /**
     * 按ID更新班级。
     *
     * @param clazz 班级实体
     * @return 影响行数
     */
    int updateById(ClazzEntity clazz);

    /**
     * 按ID查询班级。
     *
     * @param id 班级ID
     * @return 班级实体
     */
    ClazzEntity selectById(@Param("id") Long id);

    /**
     * 按ID集合批量查询班级。
     *
     * @param ids 班级ID集合
     * @return 班级列表
     */
    List<ClazzEntity> selectByIds(@Param("ids") List<Long> ids);

    /**
     * 查询班级列表，分页由PageHelper在Service层控制。
     *
     * @param keyword       班级期数关键字
     * @param headTeacherId 班主任ID
     * @param campusId      校区ID
     * @param courseId      课程ID
     * @return 班级列表
     */
    List<ClazzEntity> selectPage(@Param("keyword") String keyword,
                                 @Param("headTeacherId") Long headTeacherId,
                                 @Param("campusId") Long campusId,
                                 @Param("courseId") Long courseId);

    /**
     * 按ID删除班级。
     *
     * @param id 班级ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
}
