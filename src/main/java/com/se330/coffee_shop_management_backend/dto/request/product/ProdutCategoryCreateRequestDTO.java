package com.se330.coffee_shop_management_backend.dto.request.product;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProdutCategoryCreateRequestDTO {
    private String categoryName;
    private String categoryDescription;
}
