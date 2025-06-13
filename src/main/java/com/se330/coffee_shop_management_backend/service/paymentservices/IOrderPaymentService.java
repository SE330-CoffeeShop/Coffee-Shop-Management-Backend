package com.se330.coffee_shop_management_backend.service.paymentservices;

import com.se330.coffee_shop_management_backend.dto.request.payment.OrderPaymentCreateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.OrderPayment;
import com.se330.coffee_shop_management_backend.util.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IOrderPaymentService {
    OrderPayment createOrderPayment(OrderPaymentCreateRequestDTO orderPaymentCreateRequestDTO);
    OrderPayment executePaypalPayment(String paymentId, String payerId);
    OrderPayment updateOrderPaymentStatus(UUID orderPaymentId, Constants.PaymentStatusEnum newStatus);
    Page<OrderPayment> findAllOrderPayments(Pageable pageable);
    Page<OrderPayment> findAllOrderPaymentsByCustomerId(UUID customerId, Pageable pageable);
    OrderPayment findByIdOrder(UUID id);
}
