package com.se330.coffee_shop_management_backend.service.orderservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.invoice.InvoiceCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.order.OrderCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.order.OrderDetailCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.order.OrderUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.*;
import com.se330.coffee_shop_management_backend.repository.*;
import com.se330.coffee_shop_management_backend.service.invoiceservices.IInvoiceService;
import com.se330.coffee_shop_management_backend.service.orderservices.IOrderDetailService;
import com.se330.coffee_shop_management_backend.service.orderservices.IOrderService;
import com.se330.coffee_shop_management_backend.util.Constants;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class ImpOrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final EmployeeRepository employeeRepository;
    private final PaymentMethodsRepository paymentMethodsRepository;
    private final UserRepository userRepository;
    private final ShippingAddressesRepository shippingAddressesRepository;
    private final IOrderDetailService orderDetailService;

    public ImpOrderService(
            OrderRepository orderRepository,
            EmployeeRepository employeeRepository,
            PaymentMethodsRepository paymentMethodsRepository,
            UserRepository userRepository,
            ShippingAddressesRepository shippingAddressesRepository,
            IOrderDetailService orderDetailService
    ) {
        this.orderRepository = orderRepository;
        this.employeeRepository = employeeRepository;
        this.paymentMethodsRepository = paymentMethodsRepository;
        this.userRepository = userRepository;
        this.shippingAddressesRepository = shippingAddressesRepository;
        this.orderDetailService = orderDetailService;
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
    @Transactional
    public Order createOrder(OrderCreateRequestDTO orderCreateRequestDTO) {
        Employee existingEmployee = employeeRepository.findById(orderCreateRequestDTO.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + orderCreateRequestDTO.getEmployeeId()));

        PaymentMethods existingPaymentMethod = paymentMethodsRepository.findById(orderCreateRequestDTO.getPaymentMethodId()).orElse(null);

        User existingUser = userRepository.findById(orderCreateRequestDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id:" + orderCreateRequestDTO.getUserId()));

        ShippingAddresses existingShippingAddress = shippingAddressesRepository.findById(orderCreateRequestDTO.getShippingAddressId())
                .orElseThrow(() -> new EntityNotFoundException("Shipping address not found with id:" + orderCreateRequestDTO.getShippingAddressId()));

        // create order first

        Order newOrder = orderRepository.save(
            Order.builder()
                    .employee(existingEmployee)
                    .orderStatus(Constants.OrderStatusEnum.get(orderCreateRequestDTO.getOrderStatus()))
                    .orderTrackingNumber(orderCreateRequestDTO.getOrderTrackingNumber())
                    .paymentMethod(existingPaymentMethod)
                    .user(existingUser)
                    .shippingAddress(existingShippingAddress)
                    .orderTotalCost(BigDecimal.ZERO)
                    .build()
        );

        // now then add order details
        for (OrderDetailCreateRequestDTO orderDetailCreateRequestDTO : orderCreateRequestDTO.getOrderDetails()) {
            orderDetailCreateRequestDTO.setOrderId(newOrder.getId());
            orderDetailService.createOrderDetail(orderDetailCreateRequestDTO);
        }

        // update total cost
        BigDecimal totalCost = updateTotalCost(newOrder);
        newOrder.setOrderTotalCost(totalCost);
        orderRepository.save(newOrder);

        return orderRepository.findById(newOrder.getId())
            .orElseThrow(() -> new EntityNotFoundException("HOW THE FUCK CAN YOU EVEN GET HERE"));
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
        existingOrder.setOrderStatus(Constants.OrderStatusEnum.get(orderUpdateRequestDTO.getOrderStatus()));
        existingOrder.setOrderTrackingNumber(orderUpdateRequestDTO.getOrderTrackingNumber());

        List<OrderDetail> orderDetailOlds = existingOrder.getOrderDetails();

        for (OrderDetailCreateRequestDTO orderDetailCreateRequestDTO : orderUpdateRequestDTO.getOrderDetails()) {
            for (OrderDetail oldOrderDetail : orderDetailOlds) {
                if (
                        oldOrderDetail.getOrderDetailQuantity() == orderDetailCreateRequestDTO.getOrderDetailQuantity() &&
                        oldOrderDetail.getOrderDetailUnitPrice() == orderDetailCreateRequestDTO.getOrderDetailUnitPrice() &&
                        oldOrderDetail.getProductVariant().getId() == orderDetailCreateRequestDTO.getProductVariantId()
                ) {
                    // which means this order detail have no change
                    continue;
                }

                // remove old, create new
                orderDetailService.deleteOrderDetail(oldOrderDetail.getId());
                orderDetailCreateRequestDTO.setOrderId(existingOrder.getId());
                orderDetailService.createOrderDetail(orderDetailCreateRequestDTO);
            }
        }

        // update total cost
        BigDecimal totalCost = updateTotalCost(existingOrder);
        existingOrder.setOrderTotalCost(totalCost);

        return orderRepository.findById(existingOrder.getId())
                .orElseThrow(() -> new EntityNotFoundException("HOW THE FUCK CAN YOU EVEN GET HERE"));
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


    private BigDecimal updateTotalCost(Order order) {
        BigDecimal totalCost = BigDecimal.ZERO;

        for (OrderDetail orderDetail : order.getOrderDetails()) {
            BigDecimal itemCost = BigDecimal.valueOf((long) orderDetail.getOrderDetailUnitPrice() * orderDetail.getOrderDetailQuantity());
            totalCost = totalCost.add(itemCost);
        }

        return totalCost;
    }
}
