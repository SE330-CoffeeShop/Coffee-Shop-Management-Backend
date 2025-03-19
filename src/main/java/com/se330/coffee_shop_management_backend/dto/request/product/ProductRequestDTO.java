package com.se330.coffee_shop_management_backend.dto.request.product;
import com.se330.coffee_shop_management_backend.entity.AbstractBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ProductRequestDTO extends AbstractBaseEntity {
    private String proName;
    private String proThumb;
    private String proDescription;
    private BigDecimal proPrice;
    private String proSlug;
    private String proRatingsAverage;
    private Boolean proIsPublished;
    private Boolean proIsDeleted;
    private UUID productCategory;
    private List<UUID> productVariants;
}
