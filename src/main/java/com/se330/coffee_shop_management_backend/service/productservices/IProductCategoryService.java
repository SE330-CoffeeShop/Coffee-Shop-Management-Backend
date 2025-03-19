package com.se330.coffee_shop_management_backend.service.productservices;

import com.se330.coffee_shop_management_backend.entity.product.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IProductCategoryService {
    ProductCategory findByIdProductCategory(UUID id);
    Page<ProductCategory> findAllProductCategories(Pageable pageable);
    ProductCategory createProductCategory(ProductCategory productCategory);
    ProductCategory updateProductCategory(ProductCategory productCategory);
    void deleteProductCategory(UUID id);
}
