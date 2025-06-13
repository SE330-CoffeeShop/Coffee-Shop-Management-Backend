package com.se330.coffee_shop_management_backend.service.paymentservices.imp.strategy.imp;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.se330.coffee_shop_management_backend.dto.request.payment.OrderPaymentCreateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Order;
import com.se330.coffee_shop_management_backend.entity.OrderPayment;
import com.se330.coffee_shop_management_backend.entity.PaymentMethods;
import com.se330.coffee_shop_management_backend.repository.OrderPaymentRepository;
import com.se330.coffee_shop_management_backend.repository.OrderRepository;
import com.se330.coffee_shop_management_backend.repository.PaymentMethodsRepository;
import com.se330.coffee_shop_management_backend.service.paymentservices.imp.strategy.services.PaypalService;
import com.se330.coffee_shop_management_backend.service.paymentservices.imp.strategy.PaymentStrategy;
import com.se330.coffee_shop_management_backend.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaypalPaymentStrategy implements PaymentStrategy {

    private final PaypalService paypalService;
    private final OrderRepository orderRepository;
    private final PaymentMethodsRepository paymentMethodsRepository;
    private final OrderPaymentRepository orderPaymentRepository;

    // USD conversion rate - 1 VND = 0.0395 USD
    private static final double VND_TO_USD = 0.0395;

    @Override
    public OrderPayment pay(OrderPaymentCreateRequestDTO paymentRequest) {
        Order order = orderRepository.findById(paymentRequest.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + paymentRequest.getOrderId()));

        PaymentMethods paymentMethod = paymentMethodsRepository.findById(paymentRequest.getPaymentMethodId())
                .orElseThrow(() -> new IllegalArgumentException("Payment method not found with id: " + paymentRequest.getPaymentMethodId()));

        double amountInUSD = paymentRequest.getAmount().doubleValue() * VND_TO_USD;

        try {
            // Create PayPal payment
            Payment payment = paypalService.createPaymentWithPayPal(
                    amountInUSD,
                    paymentRequest.getOrderId().toString()
            );

            // Get approval URL to redirect user
            String redirectUrl = payment.getLinks().stream()
                    .filter(link -> "approval_url".equals(link.getRel()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No approval URL found"))
                    .getHref();

            return orderPaymentRepository.save(
                    OrderPayment.builder()
                            .amount(paymentRequest.getAmount())
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
                            .amount(paymentRequest.getAmount())
                            .paymentMethod(paymentMethod)
                            .order(order)
                            .status(Constants.PaymentStatusEnum.FAILED)
                            .failureReason(e.getMessage())
                            .build()
            );
        }
    }

    @Override
    public OrderPayment executePayment(String paymentId, String payerId) {
        try {
            // Find the payment record by transaction ID
            OrderPayment orderPayment = orderPaymentRepository.findByTransactionId(paymentId);

            if (orderPayment == null) {
                throw new IllegalArgumentException("Payment not found with transaction ID: " + paymentId);
            }

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

            if (orderPayment != null) {
                orderPayment.setStatus(Constants.PaymentStatusEnum.FAILED);
                orderPayment.setFailureReason(e.getMessage());
                return orderPaymentRepository.save(orderPayment);
            }
            throw new RuntimeException("Failed to execute PayPal payment", e);
        }
    }
}