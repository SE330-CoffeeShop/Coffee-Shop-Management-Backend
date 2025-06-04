package com.se330.coffee_shop_management_backend.dto.request.cart;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class CartUpdateRequestDTO {
    private UUID cartId;
    private UUID userId;
    private UUID branchId;
    private List<CartDetailCreateRequestDTO> cartDetails;
}
