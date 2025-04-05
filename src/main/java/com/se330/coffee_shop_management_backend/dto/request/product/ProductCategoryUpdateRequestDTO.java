package com.se330.coffee_shop_management_backend.dto.request.product;

import com.se330.coffee_shop_management_backend.entity.AbstractBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ProductCategoryUpdateRequestDTO extends AbstractBaseEntity {
    private String categoryName;
    private String categoryDescription;
}

