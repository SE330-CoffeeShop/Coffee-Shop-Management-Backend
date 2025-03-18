package com.se330.coffee_shop_management_backend.service.productservices;

import com.se330.coffee_shop_management_backend.entity.product.Product_ProductVariant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IProduct_ProductVariantService {
    Optional<Product_ProductVariant> findByIdProduct_ProductVariant(UUID id);
    List<Product_ProductVariant> findAllProduct_ProductVariant();
    Product_ProductVariant createProduct_ProductVariant(Product_ProductVariant product_ProductVariant);
    Product_ProductVariant updateProduct_ProductVariant(Product_ProductVariant product_ProductVariant);
    void  deleteByIdProduct_ProductVariant(UUID id);
}
