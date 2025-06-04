package com.se330.coffee_shop_management_backend.dto.response.discount;

import com.se330.coffee_shop_management_backend.entity.Discount;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@SuperBuilder
public class DiscountResponseDTO {

    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;

    @Schema(
            name = "createdAt",
            description = "Date time field of discount creation",
            type = "LocalDateTime",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime createdAt;

    @Schema(
            name = "updatedAt",
            type = "LocalDateTime",
            description = "Date time field of discount update",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime updatedAt;

    private String discountName;
    private String discountDescription;
    private String discountType;
    private BigDecimal discountValue;
    private String discountCode;
    private LocalDateTime discountStartDate;
    private LocalDateTime discountEndDate;
    private int discountMaxUsers;
    private int discountUserCount;
    private int discountMaxPerUser;
    private BigDecimal discountMinOrderValue;
    private boolean discountIsActive;
    private String branchId;
    private List<String> productVariantIds;

    public static DiscountResponseDTO convert(Discount discount) {
        if (discount == null) {
            return null;
        }

        return DiscountResponseDTO.builder()
                .id(discount.getId().toString())
                .createdAt(discount.getCreatedAt())
                .updatedAt(discount.getUpdatedAt())
                .discountName(discount.getDiscountName())
                .discountDescription(discount.getDiscountDescription())
                .discountType(discount.getDiscountType().name())
                .discountValue(discount.getDiscountValue())
                .discountCode(discount.getDiscountCode())
                .discountStartDate(discount.getDiscountStartDate())
                .discountEndDate(discount.getDiscountEndDate())
                .discountMaxUsers(discount.getDiscountMaxUsers())
                .discountUserCount(discount.getDiscountUserCount())
                .discountMaxPerUser(discount.getDiscountMaxPerUser())
                .discountMinOrderValue(discount.getDiscountMinOrderValue())
                .discountIsActive(discount.isDiscountIsActive())
                .branchId(discount.getBranch() != null ? discount.getBranch().getId().toString() : null)
                .productVariantIds(discount.getProductVariants() != null ? discount.getProductVariants().stream()
                        .map(productVariant -> productVariant.getId().toString())
                        .collect(Collectors.toList()) : Collections.emptyList())
                .build();
    }

    public static List<DiscountResponseDTO> convert(List<Discount> discounts) {
        if (discounts == null || discounts.isEmpty()) {
            return Collections.emptyList();
        }

        return discounts.stream()
                .map(DiscountResponseDTO::convert)
                .collect(Collectors.toList());
    }
}