package com.se330.coffee_shop_management_backend.service.paymentservices.imp;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.se330.coffee_shop_management_backend.dto.request.payment.OrderPaymentCreateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Order;
import com.se330.coffee_shop_management_backend.entity.OrderPayment;
import com.se330.coffee_shop_management_backend.entity.PaymentMethods;
import com.se330.coffee_shop_management_backend.repository.OrderPaymentRepository;
import com.se330.coffee_shop_management_backend.repository.OrderRepository;
import com.se330.coffee_shop_management_backend.repository.PaymentMethodsRepository;
import com.se330.coffee_shop_management_backend.service.paymentservices.IOrderPaymentService;
import com.se330.coffee_shop_management_backend.service.paymentservices.imp.other.PaypalService;
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
    private final PaypalService paypalService;

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
                if (paymentMethod.getPaymentMethodName() == Constants.PaymentMethodEnum.PAYPAL) {
                    // 1 VND = 0.0395 USD
                    double amountInUSD = orderPaymentCreateRequestDTO.getAmount().doubleValue() * 0.0395;
                    try {
                        // Create PayPal payment
                        Payment payment = paypalService.createPaymentWithPayPal(
                                amountInUSD,
                                orderPaymentCreateRequestDTO.getOrderId().toString()
                        );

                        // Get approval URL to redirect user
                        String redirectUrl = payment.getLinks().stream()
                                .filter(link -> "approval_url".equals(link.getRel()))
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException("No approval URL found"))
                                .getHref();

                        return orderPaymentRepository.save(
                                OrderPayment.builder()
                                        .amount(orderPaymentCreateRequestDTO.getAmount())
                                        .paymentMethod(paymentMethod)
                                        .order(order)
                                        .paypalApprovalUrl(redirectUrl)
                                        .status(Constants.PaymentStatusEnum.PENDING)
                                        .transactionId(payment.getId())
                                        .build()
                        );
                    } catch (PayPalRESTException e) {
                        return orderPaymentRepository.save(
                                OrderPayment.builder()
                                        .amount(orderPaymentCreateRequestDTO.getAmount())
                                        .paymentMethod(paymentMethod)
                                        .order(order)
                                        .status(Constants.PaymentStatusEnum.FAILED)
                                        .failureReason(e.getMessage())
                                        .build()
                        );
                    }
                }
                else if (paymentMethod.getPaymentMethodName() == Constants.PaymentMethodEnum.MOMO) {}
                else if (paymentMethod.getPaymentMethodName() == Constants.PaymentMethodEnum.VNPAY) {}
                else if (paymentMethod.getPaymentMethodName() == Constants.PaymentMethodEnum.ZALOPAY) {}
                else throw new IllegalArgumentException("Invalid payment method: " + paymentMethod.getPaymentMethodName());
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

    @Override
    @Transactional
    public OrderPayment executePaypalPayment(String paymentId, String payerId) {
        try {
            // Find the payment record by transaction ID
            OrderPayment orderPayment = orderPaymentRepository.findByTransactionId(paymentId);

            // Execute the payment through PayPal
            Payment executedPayment = paypalService.executePayment(paymentId, payerId);

            // Update payment status based on PayPal response
            if ("approved".equals(executedPayment.getState())) {
                orderPayment.setStatus(Constants.PaymentStatusEnum.COMPLETED);
            } else {
                orderPayment.setStatus(Constants.PaymentStatusEnum.FAILED);
                orderPayment.setFailureReason("Payment not approved: " + executedPayment.getState());
            }

            return orderPaymentRepository.save(orderPayment);
        } catch (PayPalRESTException e) {
            OrderPayment orderPayment = orderPaymentRepository.findByTransactionId(paymentId);

            orderPayment.setStatus(Constants.PaymentStatusEnum.FAILED);
            orderPayment.setFailureReason(e.getMessage());
            return orderPaymentRepository.save(orderPayment);
        }
    }
}
