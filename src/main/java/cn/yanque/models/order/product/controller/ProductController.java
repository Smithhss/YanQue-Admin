package cn.yanque.models.order.product.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.api.PageResult;
import cn.yanque.models.order.product.pojo.vo.req.ProductCreateReq;
import cn.yanque.models.order.product.pojo.vo.req.ProductPageReq;
import cn.yanque.models.order.product.pojo.vo.req.ProductUpdateReq;
import cn.yanque.models.order.product.pojo.vo.res.ProductCreateRes;
import cn.yanque.models.order.product.pojo.vo.res.ProductDeleteRes;
import cn.yanque.models.order.product.pojo.vo.res.ProductDetailRes;
import cn.yanque.models.order.product.pojo.vo.res.ProductPageRes;
import cn.yanque.models.order.product.pojo.vo.res.ProductUpdateRes;
import cn.yanque.models.order.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 产品管理接口。
 */
@RestController
@RequestMapping("/api/products")
@Tag(name = "ProductController", description = "产品管理")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 新增产品。
     *
     * @param req 产品新增请求
     * @return 新增后的产品ID
     */
    @PostMapping
    @Operation(description = "添加产品")
    public ApiResponse<ProductCreateRes> addProduct(@Valid @RequestBody ProductCreateReq req) {
        return ApiResponse.success(productService.addProduct(req));
    }

    /**
     * 修改产品。
     *
     * @param id  产品ID
     * @param req 产品修改请求
     * @return 被修改的产品ID
     */
    @PutMapping("{id}")
    @Operation(description = "修改产品")
    public ApiResponse<ProductUpdateRes> updateProduct(@Parameter(description = "产品ID") @PathVariable Long id,
                                                       @Valid @RequestBody ProductUpdateReq req) {
        req.setId(id);
        return ApiResponse.success(productService.updateProduct(req));
    }

    /**
     * 删除产品。
     *
     * @param id 产品ID
     * @return 被删除的产品ID
     */
    @DeleteMapping("{id}")
    @Operation(description = "删除产品")
    public ApiResponse<ProductDeleteRes> deleteProduct(@Parameter(description = "产品ID") @PathVariable Long id) {
        return ApiResponse.success(productService.deleteProduct(id));
    }

    /**
     * 根据ID查询产品。
     *
     * @param id 产品ID
     * @return 产品详情
     */
    @GetMapping("{id}")
    @Operation(description = "根据ID查询产品")
    public ApiResponse<ProductDetailRes> getProductById(@Parameter(description = "产品ID") @PathVariable Long id) {
        return ApiResponse.success(productService.getProductById(id));
    }

    /**
     * 分页查询产品。
     *
     * @param req 分页和搜索条件
     * @return 产品分页结果
     */
    @GetMapping
    @Operation(description = "分页查询产品")
    public ApiResponse<PageResult<ProductPageRes>> pageProduct(@Valid @ModelAttribute ProductPageReq req) {
        return ApiResponse.success(productService.pageProduct(req));
    }
}
