package com.se330.coffee_shop_management_backend.dto.request.product;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ProductRequestDTO {
    private UUID id;
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
