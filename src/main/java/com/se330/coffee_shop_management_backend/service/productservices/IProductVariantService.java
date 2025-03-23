package com.se330.coffee_shop_management_backend.service.productservices;

import com.se330.coffee_shop_management_backend.dto.request.product.ProductVariantRequestDTO;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IProductVariantService {
    ProductVariant findByIdProductVariant(UUID id);
    Page<ProductVariant> findAllProductVariants(Pageable pageable);
    ProductVariant createProductVariant(ProductVariantRequestDTO productVariantRequestDTO);
    ProductVariant updateProductVariant(ProductVariantRequestDTO productVariantRequestDTO);
    void deleteProductVariant(UUID id);
}
