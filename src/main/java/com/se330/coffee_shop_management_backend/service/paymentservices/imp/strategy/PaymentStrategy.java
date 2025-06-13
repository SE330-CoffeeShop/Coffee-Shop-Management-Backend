package com.se330.coffee_shop_management_backend.service.paymentservices.imp.strategy;

import com.se330.coffee_shop_management_backend.dto.request.payment.OrderPaymentCreateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.OrderPayment;

public interface PaymentStrategy {
    OrderPayment pay(OrderPaymentCreateRequestDTO paymentRequest);
    default OrderPayment executePayment(String paymentId, String payerId) {
        throw new UnsupportedOperationException("This payment method doesn't support payment execution");
    }
}