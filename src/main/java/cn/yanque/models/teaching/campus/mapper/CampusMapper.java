package cn.yanque.models.teaching.campus.mapper;

import cn.yanque.models.teaching.campus.pojo.entity.CampusEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 校区表数据访问接口。
 */
public interface CampusMapper {

    /**
     * 插入校区。
     *
     * @param campus 校区实体
     */
    void insert(CampusEntity campus);

    /**
     * 按ID更新校区。
     *
     * @param campus 校区实体
     * @return 影响行数
     */
    int updateById(CampusEntity campus);

    /**
     * 按ID查询校区。
     *
     * @param id 校区ID
     * @return 校区实体
     */
    CampusEntity selectById(@Param("id") Long id);

    /**
     * 按ID集合批量查询校区。
     * <p>
     * 班级列表回填校区名称时会使用这个方法。
     *
     * @param ids 校区ID集合
     * @return 校区列表
     */
    List<CampusEntity> selectByIds(@Param("ids") List<Long> ids);

    /**
     * 查询校区列表，分页由PageHelper在Service层控制。
     *
     * @param keyword 校区地点、负责人或电话关键字
     * @return 校区列表
     */
    List<CampusEntity> selectPage(@Param("keyword") String keyword);

    /**
     * 按ID删除校区。
     *
     * @param id 校区ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
}
