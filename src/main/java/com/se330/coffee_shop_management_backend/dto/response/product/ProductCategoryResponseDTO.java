package com.se330.coffee_shop_management_backend.dto.response.product;

import com.se330.coffee_shop_management_backend.dto.response.AbstractBaseResponse;
import com.se330.coffee_shop_management_backend.entity.product.ProductCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
public class ProductCategoryResponseDTO extends AbstractBaseResponse {

    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;

    @Schema(
            name = "createdAt",
            description = "Date time field of product category creation",
            type = "LocalDateTime",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime createdAt;

    @Schema(
            name = "updatedAt",
            type = "LocalDateTime",
            description = "Date time field of product category update",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime updatedAt;

    private String categoryName;
    private String categoryDescription;
    private Integer catalogId;

    private List<String> products;

    public static ProductCategoryResponseDTO convert(ProductCategory productCategory) {

        List<String> productIds;

        if (productCategory.getProducts() == null ||  productCategory.getProducts().isEmpty()) {
            productIds = Collections.emptyList();
        } else {
            productIds = productCategory.getProducts().stream()
                    .map(entity -> entity.getId().toString())
                    .collect(Collectors.toList());
        }

        return ProductCategoryResponseDTO.builder()
                .id(productCategory.getId().toString())
                .createdAt(productCategory.getCreatedAt())
                .updatedAt(productCategory.getUpdatedAt())
                .categoryName(productCategory.getCategoryName())
                .categoryDescription(productCategory.getCategoryDescription())
                .products(productIds)
                .catalogId(productCategory.getCatalog() != null ? productCategory.getCatalog().getId() : null)
                .build();
    }

    public static List<ProductCategoryResponseDTO> convert(List<ProductCategory> productCategories) {
        if (productCategories == null || productCategories.isEmpty()) {
            return Collections.emptyList();
        }

        return productCategories.stream()
                .map(ProductCategoryResponseDTO::convert)
                .collect(Collectors.toList());
    }
}
