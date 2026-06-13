package cn.yanque.models.order.product.service.impl;

import cn.yanque.common.api.PageResult;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.order.product.mapper.ProductMapper;
import cn.yanque.models.order.product.pojo.entity.ProductEntity;
import cn.yanque.models.order.product.pojo.vo.req.ProductCreateReq;
import cn.yanque.models.order.product.pojo.vo.req.ProductPageReq;
import cn.yanque.models.order.product.pojo.vo.req.ProductUpdateReq;
import cn.yanque.models.order.product.pojo.vo.res.ProductCreateRes;
import cn.yanque.models.order.product.pojo.vo.res.ProductDeleteRes;
import cn.yanque.models.order.product.pojo.vo.res.ProductDetailRes;
import cn.yanque.models.order.product.pojo.vo.res.ProductPageRes;
import cn.yanque.models.order.product.pojo.vo.res.ProductUpdateRes;
import cn.yanque.models.order.product.service.ProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductCreateRes addProduct(ProductCreateReq req) {
        ProductEntity product = new ProductEntity();
        product.setCourseContent(req.getCourseContent());
        product.setTeachingMode(req.getTeachingMode());
        product.setPrice(req.getPrice());
        product.setCreatedAt(new Date());
        product.setUpdatedAt(new Date());
        productMapper.insert(product);

        ProductCreateRes res = new ProductCreateRes();
        res.setId(product.getId());
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductUpdateRes updateProduct(ProductUpdateReq req) {
        ProductEntity product = new ProductEntity();
        product.setId(req.getId());
        product.setCourseContent(req.getCourseContent());
        product.setTeachingMode(req.getTeachingMode());
        product.setPrice(req.getPrice());
        product.setUpdatedAt(new Date());

        int rows = productMapper.updateById(product);
        if (rows == 0) {
            throw BusinessException.ProductNotExist;
        }

        ProductUpdateRes res = new ProductUpdateRes();
        res.setId(req.getId());
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductDeleteRes deleteProduct(Long id) {
        int rows = productMapper.deleteById(id);
        if (rows == 0) {
            throw BusinessException.ProductNotExist;
        }

        ProductDeleteRes res = new ProductDeleteRes();
        res.setId(id);
        return res;
    }

    @Override
    public ProductDetailRes getProductById(Long id) {
        ProductEntity product = productMapper.selectById(id);
        if (product == null) {
            throw BusinessException.ProductNotExist;
        }
        ProductDetailRes res = new ProductDetailRes();
        BeanUtils.copyProperties(product, res);
        return res;
    }

    @Override
    public PageResult<ProductPageRes> pageProduct(ProductPageReq req) {
        int pageNum = req.getPageNum() == null ? 1 : req.getPageNum();
        int pageSize = req.getPageSize() == null ? 10 : req.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<ProductEntity> list = productMapper.selectPage(req.getKeyword());
        PageInfo<ProductEntity> pageInfo = new PageInfo<>(list);
        List<ProductPageRes> records = list.stream().map(this::buildProductPageRes).toList();
        return new PageResult<>(pageInfo.getTotal(), pageNum, pageSize, records);
    }

    private ProductPageRes buildProductPageRes(ProductEntity product) {
        ProductPageRes res = new ProductPageRes();
        BeanUtils.copyProperties(product, res);
        return res;
    }
}
