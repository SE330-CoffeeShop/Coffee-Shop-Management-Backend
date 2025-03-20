package com.se330.coffee_shop_management_backend.dto.request.product;

import com.se330.coffee_shop_management_backend.entity.AbstractBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ProductVariantRequestDTO extends AbstractBaseEntity {
    private String variantTierIdx;
    private Boolean variantDefault;
    private String variantSlug;
    private int variantSort;
    private Long variantPrice;
    private int variantStock;
    private Boolean variantIsPublished;
    private Boolean variantIsDeleted;
    private UUID product;
}
