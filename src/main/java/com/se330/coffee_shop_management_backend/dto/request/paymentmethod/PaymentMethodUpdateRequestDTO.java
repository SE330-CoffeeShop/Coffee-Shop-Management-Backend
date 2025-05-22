package com.se330.coffee_shop_management_backend.dto.request.paymentmethod;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class PaymentMethodUpdateRequestDTO {
    private UUID paymentMethodId;
    private String methodType;
    private String methodDetails;
    private boolean methodIsDefault;
    private UUID userId;
    private UUID orderId;
}