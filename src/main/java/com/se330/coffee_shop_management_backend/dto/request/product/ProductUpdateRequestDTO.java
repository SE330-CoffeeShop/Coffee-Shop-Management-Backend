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
public class ProductUpdateRequestDTO extends AbstractBaseEntity {
    private String productName;
    private String productThumb;
    private String productDescription;
    private BigDecimal productPrice;
    private String productSlug;
    private int productCommentCount;
    private String productRatingsAverage;
    private Boolean productIsPublished;
    private Boolean productIsDeleted;
    private UUID productCategory;
    private List<UUID> productVariants;
}