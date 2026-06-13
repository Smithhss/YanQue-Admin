package cn.yanque.models.order.product.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.order.product.pojo.vo.req.ProductCreateReq;
import cn.yanque.models.order.product.pojo.vo.req.ProductPageReq;
import cn.yanque.models.order.product.pojo.vo.req.ProductUpdateReq;
import cn.yanque.models.order.product.pojo.vo.res.ProductCreateRes;
import cn.yanque.models.order.product.pojo.vo.res.ProductDeleteRes;
import cn.yanque.models.order.product.pojo.vo.res.ProductDetailRes;
import cn.yanque.models.order.product.pojo.vo.res.ProductPageRes;
import cn.yanque.models.order.product.pojo.vo.res.ProductUpdateRes;

/**
 * 产品管理服务。
 */
public interface ProductService {

    ProductCreateRes addProduct(ProductCreateReq req);

    ProductUpdateRes updateProduct(ProductUpdateReq req);

    ProductDeleteRes deleteProduct(Long id);

    ProductDetailRes getProductById(Long id);

    PageResult<ProductPageRes> pageProduct(ProductPageReq req);
}
