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
public class ProductResponseDTO extends AbstractBaseResponse {

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

    private String proName;
    private String proThumb;
    private String proDescription;
    private BigDecimal proPrice;
    private String proSlug;
    private String proRatingsAverage;
    private Boolean proIsPublished;
    private Boolean proIsDeleted;
    private String productCategory;
    private List<String> productVariants;
    private ProductCategoryResponseDTO productCategoryResponseDTO;

    public static ProductResponseDTO convert(Product product) {

        List<String> productVariantIds;

        List<ProductVariant> tmpList = product.getProductVariants();
        if (tmpList == null || tmpList.isEmpty()) {
            productVariantIds = Collections.emptyList();
        } else {
            productVariantIds = product.getProductVariants().stream()
                    .map(entity -> entity.getId().toString())
                    .collect(Collectors.toList());
        }

        return ProductResponseDTO.builder()
                .id(product.getId().toString())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .proName(product.getProName())
                .proThumb(product.getProThumb())
                .proDescription(product.getProDescription())
                .proPrice(product.getProPrice())
                .proSlug(product.getProSlug())
                .proRatingsAverage(product.getProRatingsAverage())
                .proIsPublished(product.getProIsPublished())
                .proIsDeleted(product.getProIsDeleted())
                .productVariants(productVariantIds)
                .productCategoryResponseDTO(ProductCategoryResponseDTO.convert(product.getProductCategory()))
                .build();
    }

    public static List<ProductResponseDTO> convert(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return Collections.emptyList();
        }

        return products.stream()
                .map(ProductResponseDTO::convert)
                .collect(Collectors.toList());
    }

}
