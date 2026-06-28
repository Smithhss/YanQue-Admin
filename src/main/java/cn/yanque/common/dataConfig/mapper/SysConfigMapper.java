package cn.yanque.common.dataConfig.mapper;

import cn.yanque.common.dataConfig.entity.SysConfigEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统配置表数据访问接口。
 */
public interface SysConfigMapper {

    /**
     * 插入系统配置。
     *
     * @param config 系统配置实体
     */
    void insert(SysConfigEntity config);

    /**
     * 按ID更新系统配置。
     *
     * @param config 系统配置实体
     * @return 影响行数
     */
    int updateById(SysConfigEntity config);

    /**
     * 按配置key更新配置值。
     *
     * @param key   配置key
     * @param value 配置值
     * @return 影响行数
     */
    int updateByKey(@Param("key") String key, @Param("value") String value);

    /**
     * 按ID查询系统配置。
     *
     * @param id 配置ID
     * @return 系统配置实体
     */
    SysConfigEntity selectById(@Param("id") Long id);

    /**
     * 按配置key查询系统配置。
     *
     * @param key 配置key
     * @return 系统配置实体
     */
    SysConfigEntity selectByKey(@Param("key") String key);

    /**
     * 查询配置列表,分页由PageHelper在Service层控制。
     *
     * @param keyword 配置key或配置值关键字
     * @return 配置列表
     */
    List<SysConfigEntity> selectPage(@Param("keyword") String keyword);

    /**
     * 查询全部系统配置。
     *
     * @return 配置列表
     */
    List<SysConfigEntity> selectList();

    /**
     * 按ID删除系统配置。
     *
     * @param id 配置ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
}
