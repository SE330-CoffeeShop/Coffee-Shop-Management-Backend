package com.se330.coffee_shop_management_backend.dto.request.order;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class OrderUpdateRequestDTO {
    private UUID orderId;
    private String orderStatus;
    private UUID employeeId;
    private UUID userId;
    private UUID shippingAddressId;
    private UUID paymentMethodId;
    private UUID cartId;
}