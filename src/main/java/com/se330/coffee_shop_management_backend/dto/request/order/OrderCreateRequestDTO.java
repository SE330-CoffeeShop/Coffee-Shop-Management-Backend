package com.se330.coffee_shop_management_backend.dto.request.order;

import com.se330.coffee_shop_management_backend.dto.request.paymentmethod.PaymentMethodCreateRequestDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class OrderCreateRequestDTO {
    private String orderStatus;
    private BigDecimal orderTrackingNumber;
    private UUID employeeId;
    private UUID userId;
    private UUID shippingAddressId;
    private PaymentMethodCreateRequestDTO paymentMethod;
    private List<OrderDetailCreateRequestDTO> orderDetails;
}