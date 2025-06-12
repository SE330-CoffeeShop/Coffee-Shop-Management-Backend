package com.se330.coffee_shop_management_backend.service.paymentservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.payment.OrderPaymentCreateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Order;
import com.se330.coffee_shop_management_backend.entity.OrderPayment;
import com.se330.coffee_shop_management_backend.entity.PaymentMethods;
import com.se330.coffee_shop_management_backend.repository.OrderPaymentRepository;
import com.se330.coffee_shop_management_backend.repository.OrderRepository;
import com.se330.coffee_shop_management_backend.repository.PaymentMethodsRepository;
import com.se330.coffee_shop_management_backend.service.paymentservices.IOrderPaymentService;
import com.se330.coffee_shop_management_backend.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImpOrderPaymentService implements IOrderPaymentService {

    private final OrderRepository orderRepository;
    private final PaymentMethodsRepository paymentMethodsRepository;
    private final OrderPaymentRepository orderPaymentRepository;

    @Override
    @Transactional
    public OrderPayment createOrderPayment(OrderPaymentCreateRequestDTO orderPaymentCreateRequestDTO) {
        Order order = orderRepository.findById(orderPaymentCreateRequestDTO.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderPaymentCreateRequestDTO.getOrderId()));

        PaymentMethods paymentMethod = paymentMethodsRepository.findById(orderPaymentCreateRequestDTO.getPaymentMethodId())
                .orElseThrow(() -> new IllegalArgumentException("Payment method not found with id: " + orderPaymentCreateRequestDTO.getPaymentMethodId()));

        // TODO: Implement logic to handle exchange money
        Constants.PaymentStatusEnum paymentStatusEnum;
        if (paymentMethod.getPaymentMethodName() == Constants.PaymentMethodEnum.CASH) {
            // TODO: Every cash payment is only available for in-store orders
            paymentStatusEnum = Constants.PaymentStatusEnum.COMPLETED;
        } else {
            try {
                // TODO: Implement actual payment processing logic here

                paymentStatusEnum = Constants.PaymentStatusEnum.COMPLETED;
            } catch (Exception e) {
                // If payment processing fails, set the status to FAILED
                paymentStatusEnum = Constants.PaymentStatusEnum.FAILED;
            }
        }

        return orderPaymentRepository.save(
                OrderPayment.builder()
                        .amount(orderPaymentCreateRequestDTO.getAmount())
                        .paymentMethod(paymentMethod)
                        .order(order)
                        .status(paymentStatusEnum)
                        .transactionId(orderPaymentCreateRequestDTO.getTransactionId())
                        .failureReason(orderPaymentCreateRequestDTO.getFailureReason())
                        .build()
        );
    }

    @Override
    @Transactional
    public OrderPayment updateOrderPaymentStatus(UUID orderPaymentId, Constants.PaymentStatusEnum newStatus) {
        OrderPayment orderPayment = orderPaymentRepository.findById(orderPaymentId)
                .orElseThrow(() -> new IllegalArgumentException("Order payment not found with id: " + orderPaymentId));

        // Update the status of the order payment
        orderPayment.setStatus(newStatus);

        if (newStatus == Constants.PaymentStatusEnum.COMPLETED) {
            // TODO: Send completed noti
        }
        else if (newStatus == Constants.PaymentStatusEnum.FAILED) {
            // TODO: Send failed noti
        }
        else if (newStatus == Constants.PaymentStatusEnum.REFUNDED) {
            // TODO: Send refunded noti
        }

        return orderPaymentRepository.save(orderPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderPayment> findAllOrderPayments(Pageable pageable) {
        return orderPaymentRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderPayment> findAllOrderPaymentsByCustomerId(UUID customerId, Pageable pageable) {
        return orderPaymentRepository.findAllByOrder_User_Id(customerId, pageable);
    }
}
