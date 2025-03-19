package com.se330.coffee_shop_management_backend.service.productservices;

import com.se330.coffee_shop_management_backend.dto.request.product.ProductVariantRequestDTO;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;

import java.util.List;
import java.util.UUID;

public interface IProductVariantService {
    ProductVariant findByIdProductVariant(UUID id);
    List<ProductVariant> findAllProductVariants();
    ProductVariant createProductVariant(ProductVariantRequestDTO productVariantRequestDTO);
    ProductVariant updateProductVariant(ProductVariantRequestDTO productVariantRequestDTO);
    void deleteProductVariant(UUID id);
}
