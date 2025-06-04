package com.se330.coffee_shop_management_backend.dto.request.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartCreateRequestDTO {
    private UUID userId;
    private UUID branchId;
    private List<CartDetailCreateRequestDTO> cartDetails;
}
