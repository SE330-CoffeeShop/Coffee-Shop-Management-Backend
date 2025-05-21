package com.se330.coffee_shop_management_backend.service.orderservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.order.OrderCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.order.OrderUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.*;
import com.se330.coffee_shop_management_backend.repository.*;
import com.se330.coffee_shop_management_backend.service.orderservices.IOrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ImpOrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final EmployeeRepository employeeRepository;
    private final PaymentMethodsRepository paymentMethodsRepository;
    private final UserRepository userRepository;
    private final ShippingAddressesRepository shippingAddressesRepository;

    public ImpOrderService(
            OrderRepository orderRepository,
            EmployeeRepository employeeRepository,
            PaymentMethodsRepository paymentMethodsRepository,
            UserRepository userRepository,
            ShippingAddressesRepository shippingAddressesRepository
    ) {
        this.orderRepository = orderRepository;
        this.employeeRepository = employeeRepository;
        this.paymentMethodsRepository = paymentMethodsRepository;
        this.userRepository = userRepository;
        this.shippingAddressesRepository = shippingAddressesRepository;
    }

    @Override
    public Order findByIdOrder(UUID id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Order> findAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public Order createOrder(OrderCreateRequestDTO orderCreateRequestDTO) {
        Employee exisingEmployee = employeeRepository.findById(orderCreateRequestDTO.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id:" + orderCreateRequestDTO.getEmployeeId()));

        PaymentMethods existingPaymentMethod = paymentMethodsRepository.findById(orderCreateRequestDTO.getPaymentMethodId())
                .orElseThrow(() -> new EntityNotFoundException("Payment method not found with id:" + orderCreateRequestDTO.getPaymentMethodId()));

        User existingUser = userRepository.findById(orderCreateRequestDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id:" + orderCreateRequestDTO.getUserId()));

        ShippingAddresses existingShippingAddress = shippingAddressesRepository.findById(orderCreateRequestDTO.getShippingAddressId())
                .orElseThrow(() -> new EntityNotFoundException("Shipping address not found with id:" + orderCreateRequestDTO.getShippingAddressId()));

        return orderRepository.save(
                Order.builder()
                        .employee(exisingEmployee)
                        .orderStatus(orderCreateRequestDTO.isOrderStatus())
                        .orderTrackingNumber(orderCreateRequestDTO.getOrderTrackingNumber())
                        .paymentMethod(existingPaymentMethod)
                        .user(existingUser)
                        .shippingAddress(existingShippingAddress)
                        .build()
        );
    }

    @Override
    public Order updateOrder(OrderUpdateRequestDTO orderUpdateRequestDTO) {
        Order existingOrder = orderRepository.findById(orderUpdateRequestDTO.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id:" + orderUpdateRequestDTO.getOrderId()));

        PaymentMethods existingPaymentMethod = paymentMethodsRepository.findById(orderUpdateRequestDTO.getPaymentMethodId())
                .orElseThrow(() -> new EntityNotFoundException("Payment method not found with id:" + orderUpdateRequestDTO.getPaymentMethodId()));

        User existingUser = userRepository.findById(orderUpdateRequestDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id:" + orderUpdateRequestDTO.getUserId()));

        ShippingAddresses existingShippingAddress = shippingAddressesRepository.findById(orderUpdateRequestDTO.getShippingAddressId())
                .orElseThrow(() -> new EntityNotFoundException("Shipping address not found with id:" + orderUpdateRequestDTO.getShippingAddressId()));

        existingOrder.setPaymentMethod(existingPaymentMethod);
        existingOrder.setUser(existingUser);
        existingOrder.setShippingAddress(existingShippingAddress);
        existingOrder.setOrderStatus(orderUpdateRequestDTO.isOrderStatus());
        existingOrder.setOrderTrackingNumber(orderUpdateRequestDTO.getOrderTrackingNumber());

        return orderRepository.save(existingOrder);
    }

    @Override
    @Transactional
    public void deleteOrder(UUID id) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id:" + id));

        if (existingOrder.getUser() != null) {
            existingOrder.getUser().getOrders().remove(existingOrder);
        }

        if (existingOrder.getShippingAddress() != null) {
            existingOrder.setShippingAddress(null);
        }

        if (existingOrder.getPaymentMethod() != null) {
            existingOrder.setPaymentMethod(null);
        }

        if (existingOrder.getEmployee() != null) {
            existingOrder.setEmployee(null);
        }

        orderRepository.delete(existingOrder);
    }
}
