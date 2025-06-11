package com.se330.coffee_shop_management_backend.dto.request.order;

import com.se330.coffee_shop_management_backend.util.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@NoArgsConstructor
@SuperBuilder
public class OrderCreateRequestDTO {
    private Constants.OrderStatusEnum orderStatus;
    private UUID employeeId;
    private UUID userId;
    private UUID shippingAddressId;
    private UUID paymentMethodId;
    private UUID branchId;
}