package cn.yanque.models.order.product.mapper;

import cn.yanque.models.order.product.pojo.entity.ProductEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品表数据访问接口。
 */
public interface ProductMapper {

    /**
     * 插入产品。
     *
     * @param product 产品实体
     */
    void insert(ProductEntity product);

    /**
     * 按ID更新产品。
     *
     * @param product 产品实体
     * @return 影响行数
     */
    int updateById(ProductEntity product);

    /**
     * 按ID查询产品。
     *
     * @param id 产品ID
     * @return 产品实体
     */
    ProductEntity selectById(@Param("id") Long id);

    /**
     * 查询产品列表，分页由PageHelper在Service层控制。
     *
     * @param keyword 课程内容关键字
     * @return 产品列表
     */
    List<ProductEntity> selectPage(@Param("keyword") String keyword);

    /**
     * 按ID删除产品。
     *
     * @param id 产品ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
}
