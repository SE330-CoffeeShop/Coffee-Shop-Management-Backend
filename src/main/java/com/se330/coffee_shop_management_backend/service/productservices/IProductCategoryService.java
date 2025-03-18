package com.se330.coffee_shop_management_backend.service.productservices;

import com.se330.coffee_shop_management_backend.entity.product.ProductCategory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IProductCategoryService {
    Optional<ProductCategory> findByIdProductCategory(UUID id);
    List<ProductCategory> findAllProductCategories();
    ProductCategory createProductCategory(ProductCategory productCategory);
    ProductCategory updateProductCategory(ProductCategory productCategory);
    void deleteProductCategory(UUID id);
}
