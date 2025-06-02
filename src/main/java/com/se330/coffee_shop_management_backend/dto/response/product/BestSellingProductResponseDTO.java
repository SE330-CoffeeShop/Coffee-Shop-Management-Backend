package com.se330.coffee_shop_management_backend.dto.response.product;

import com.se330.coffee_shop_management_backend.dto.response.AbstractBaseResponse;
import com.se330.coffee_shop_management_backend.entity.product.Product;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
public class BestSellingProductResponseDTO extends AbstractBaseResponse {

    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;

    @Schema(
            name = "createdAt",
            description = "Date time field of product creation",
            type = "LocalDateTime",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime createdAt;

    @Schema(
            name = "updatedAt",
            type = "LocalDateTime",
            description = "Date time field of product update",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime updatedAt;

    private String productName;
    private String productThumb;
    private String productDescription;
    private BigDecimal productPrice;
    private String productSlug;
    private BigDecimal productRatingsAverage;
    private Boolean productIsPublished;
    private Boolean productIsDeleted;

    private List<String> productVariants;
    private String productCategoryId;
    private List<String> commentIds;

    private int productSoldCount;

    public static BestSellingProductResponseDTO convert(Product product, int productSoldCount) {

        List<String> productVariantIds;

        List<ProductVariant> tmpList = product.getProductVariants();
        if (tmpList == null || tmpList.isEmpty()) {
            productVariantIds = Collections.emptyList();
        } else {
            productVariantIds = product.getProductVariants().stream()
                    .map(entity -> entity.getId().toString())
                    .collect(Collectors.toList());
        }

        return BestSellingProductResponseDTO.builder()
                .id(product.getId().toString())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .productName(product.getProductName())
                .productThumb(product.getProductThumb())
                .productDescription(product.getProductDescription())
                .productPrice(product.getProductPrice())
                .productSlug(product.getProductSlug())
                .productRatingsAverage(product.getProductRatingsAverage())
                .productIsPublished(product.getProductIsPublished())
                .productIsDeleted(product.getProductIsDeleted())
                .productVariants(productVariantIds)
                .productCategoryId(product.getProductCategory().getId().toString())
                .commentIds(product.getComments().stream()
                        .map(entity -> entity.getCommentId() + "") // Cast int to String
                        .collect(Collectors.toList()))
                .productSoldCount(productSoldCount)
                .build();
    }

    public static List<BestSellingProductResponseDTO> convert(List<Object[]> productData) {
        if (productData == null || productData.isEmpty()) {
            return Collections.emptyList();
        }

        return productData.stream()
                .map(data -> {
                    Product product = (Product) data[0];
                    int totalQuantity = ((Number) data[1]).intValue();
                    return convert(product, totalQuantity);
                })
                .collect(Collectors.toList());
    }

}
