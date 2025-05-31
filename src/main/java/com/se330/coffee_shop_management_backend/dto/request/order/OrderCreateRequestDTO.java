package com.se330.coffee_shop_management_backend.dto.request.order;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@SuperBuilder
public class OrderCreateRequestDTO {
    private String orderStatus;
    private UUID employeeId;
    private UUID userId;
    private UUID shippingAddressId;
    private UUID paymentMethodId;
    private List<OrderDetailCreateRequestDTO> orderDetails;
}