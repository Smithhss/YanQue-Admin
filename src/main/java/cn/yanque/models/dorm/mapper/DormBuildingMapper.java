package cn.yanque.models.dorm.mapper;

import cn.yanque.models.dorm.pojo.entity.DormBuildingEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 宿舍楼栋数据访问接口。
 */
public interface DormBuildingMapper {

    void insert(DormBuildingEntity building);

    int updateById(DormBuildingEntity building);

    DormBuildingEntity selectById(@Param("id") Long id);

    List<DormBuildingEntity> selectByIds(@Param("ids") List<Long> ids);

    List<DormBuildingEntity> selectPage(@Param("keyword") String keyword, @Param("campusId") Long campusId);

    int deleteById(@Param("id") Long id);
}
