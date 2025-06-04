package com.se330.coffee_shop_management_backend.dto.request.product;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ProductVariantUpdateRequestDTO {
    private UUID variantId;
    private String variantTierIdx;
    private Boolean variantDefault;
    private String variantSlug;
    private int variantSort;
    private Long variantPrice;
    private Boolean variantIsPublished;
    private Boolean variantIsDeleted;
    private UUID product;
}
