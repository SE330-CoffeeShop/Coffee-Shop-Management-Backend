package com.se330.coffee_shop_management_backend.dto.request.product;

import com.se330.coffee_shop_management_backend.entity.AbstractBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ProductUpdateRequestDTO {
    private UUID productId;
    private String productName;
    private String productThumb;
    private String productDescription;
    private BigDecimal productPrice;
    private String productSlug;
    private int productCommentCount;
    private BigDecimal productRatingsAverage;
    private Boolean productIsPublished;
    private Boolean productIsDeleted;
    private UUID productCategory;
}