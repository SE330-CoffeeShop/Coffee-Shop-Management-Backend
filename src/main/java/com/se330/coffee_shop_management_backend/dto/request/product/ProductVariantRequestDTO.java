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
    private String varTierIdx;
    private Boolean varDefault;
    private String varSlug;
    private int varSort;
    private Long varPrice;
    private int varStock;
    private Boolean varIsPublished;
    private Boolean varIsDeleted;
    private UUID product;
}
