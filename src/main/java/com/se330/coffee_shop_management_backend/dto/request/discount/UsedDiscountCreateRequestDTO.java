package com.se330.coffee_shop_management_backend.dto.request.discount;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UsedDiscountCreateRequestDTO {
    private UUID orderDetailId;
    private UUID discountId;
    private int timesUse;
}
