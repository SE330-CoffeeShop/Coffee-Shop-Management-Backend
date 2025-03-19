package com.se330.coffee_shop_management_backend.dto.response.product;

import com.se330.coffee_shop_management_backend.dto.response.AbstractBaseResponse;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
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
public class ProductVariantResponseDTO extends AbstractBaseResponse {

    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;

    @Schema(
            name = "createdAt",
            description = "Date time field of product variant creation",
            type = "LocalDateTime",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime createdAt;

    @Schema(
            name = "updatedAt",
            type = "LocalDateTime",
            description = "Date time field of product variant update",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime updatedAt;

    private String varTierIdx;
    private Boolean varDefault;
    private String varSlug;
    private int varSort;
    private Long varPrice;
    private int varStock;
    private Boolean varIsPublished;
    private Boolean varIsDeleted;
    private ProductResponseDTO product;

    public static ProductVariantResponseDTO convert(ProductVariant productVariant) {
        return ProductVariantResponseDTO.builder()
                .id(productVariant.getId().toString())
                .createdAt(productVariant.getCreatedAt())
                .updatedAt(productVariant.getUpdatedAt())
                .varTierIdx(productVariant.getVarTierIdx())
                .varDefault(productVariant.getVarDefault())
                .varSlug(productVariant.getVarSlug())
                .varSort(productVariant.getVarSort())
                .varPrice(productVariant.getVarPrice())
                .varStock(productVariant.getVarStock())
                .varIsPublished(productVariant.getVarIsPublished())
                .varIsDeleted(productVariant.getVarIsDeleted())
                .product(ProductResponseDTO.convert(productVariant.getProduct()))
                .build();
    }

    public static List<ProductVariantResponseDTO> convert(List<ProductVariant> productVariants) {
        if (productVariants == null || productVariants.isEmpty()) {
            return Collections.emptyList();
        }
        return productVariants.stream()
                .map(ProductVariantResponseDTO::convert)
                .collect(Collectors.toList());
    }
}