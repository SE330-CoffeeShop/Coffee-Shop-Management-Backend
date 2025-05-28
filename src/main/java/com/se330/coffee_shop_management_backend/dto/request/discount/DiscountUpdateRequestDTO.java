package com.se330.coffee_shop_management_backend.dto.request.discount;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class DiscountUpdateRequestDTO {
    private UUID discountId;
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
    private UUID branchId;
    private List<UUID> productVariantIds;
}