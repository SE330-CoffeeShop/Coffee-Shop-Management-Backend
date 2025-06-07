package com.se330.coffee_shop_management_backend.dto.request.product;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ProductVariantCreateRequestDTO {
    private String variantTierIdx;
    private Boolean variantDefault;
    private BigDecimal variantPrice;
    private UUID product;
}