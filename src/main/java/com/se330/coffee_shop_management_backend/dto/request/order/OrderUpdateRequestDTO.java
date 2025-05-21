package com.se330.coffee_shop_management_backend.dto.request.order;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
public class OrderUpdateRequestDTO {
    private UUID orderId;
    private boolean orderStatus;
    private BigDecimal orderTrackingNumber;
    private UUID employeeId;
    private UUID paymentMethodId;
    private UUID userId;
    private UUID shippingAddressId;
}