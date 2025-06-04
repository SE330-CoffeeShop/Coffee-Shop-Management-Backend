package com.se330.coffee_shop_management_backend.dto.response.product;

import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@SuperBuilder
public class ProductVariantResponseDTO{

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

    private String variantTierIdx;
    private Boolean variantDefault;
    private String variantSlug;
    private int variantSort;
    private Long variantPrice;
    private int variantStock;
    private Boolean variantIsPublished;
    private Boolean variantIsDeleted;

    private String productId;
    private List<String> discountIds;
    private List<String> orderDetailIds;

    public static ProductVariantResponseDTO convert(ProductVariant productVariant) {
        return ProductVariantResponseDTO.builder()
                .id(productVariant.getId().toString())
                .createdAt(productVariant.getCreatedAt())
                .updatedAt(productVariant.getUpdatedAt())
                .variantTierIdx(productVariant.getVariantTierIdx())
                .variantDefault(productVariant.getVariantDefault())
                .variantSlug(productVariant.getVariantSlug())
                .variantSort(productVariant.getVariantSort())
                .variantPrice(productVariant.getVariantPrice())
                .variantStock(productVariant.getVariantStock())
                .variantIsPublished(productVariant.getVariantIsPublished())
                .variantIsDeleted(productVariant.getVariantIsDeleted())
                .productId(productVariant.getProduct().getId().toString())
                .discountIds(productVariant.getDiscounts() != null ? productVariant.getDiscounts().stream()
                        .map(discount -> discount.getId().toString())
                        .collect(Collectors.toList()) : Collections.emptyList())
                .orderDetailIds(productVariant.getOrderDetails() != null ? productVariant.getOrderDetails().stream()
                        .map(orderDetail -> orderDetail.getId().toString())
                        .collect(Collectors.toList()) : Collections.emptyList())
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