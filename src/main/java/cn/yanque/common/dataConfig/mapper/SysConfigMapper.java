package cn.yanque.common.dataConfig.mapper;

import cn.yanque.common.dataConfig.entity.SysConfigEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysConfigMapper {

    void insert(SysConfigEntity config);

    int updateById(SysConfigEntity config);

    int updateByKey(@Param("key") String key, @Param("value") String value);

    SysConfigEntity selectById(@Param("id") Long id);

    SysConfigEntity selectByKey(@Param("key") String key);

    List<SysConfigEntity> selectPage(@Param("keyword") String keyword);

    List<SysConfigEntity> selectList();

    int deleteById(@Param("id") Long id);
}
