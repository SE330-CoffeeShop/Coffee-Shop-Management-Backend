package com.se330.coffee_shop_management_backend.service.dummydataservices.domain;

import com.se330.coffee_shop_management_backend.entity.*;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import com.se330.coffee_shop_management_backend.repository.*;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductVariantRepository;
import com.se330.coffee_shop_management_backend.util.Constants;
import com.se330.coffee_shop_management_backend.util.CreateTrackingNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class Logistics {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final ProductVariantRepository productVariantRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final ShippingAddressesRepository shippingAddressesRepository;
    private final PaymentMethodsRepository paymentMethodsRepository;

    @Transactional
    public void create() {
        createOrders();
    }

    private void createOrders() {
        log.info("Creating orders...");

        // Get all necessary data
        List<User> customers = userRepository.findAllByRoleName(Constants.RoleEnum.CUSTOMER);
        List<Employee> employees = employeeRepository.findAll();
        List<ProductVariant> productVariants = productVariantRepository.findAll();
        List<ShippingAddresses> shippingAddresses = shippingAddressesRepository.findAll();
        PaymentMethods cashPaymentMethod = paymentMethodsRepository.findByMethodType("Tiền mặt");
        List<PaymentMethods> userPaymentMethods = paymentMethodsRepository.findAllByUserIsNotNull();

        if (customers.isEmpty() || employees.isEmpty() || productVariants.isEmpty()) {
            log.error("Cannot create orders: Missing required data");
            return;
        }

        Random random = new Random();
        List<Order> orders = new ArrayList<>();
        int orderCount = 10000;

        // Create orders
        for (int i = 0; i < orderCount; i++) {
            User customer = customers.get(random.nextInt(customers.size()));
            Employee employee = employees.get(random.nextInt(employees.size()));

            // Select payment method
            PaymentMethods paymentMethod;
            boolean isOnline = random.nextBoolean();

            if (isOnline) {
                // Filter payment methods for this customer
                List<PaymentMethods> customerPaymentMethods = userPaymentMethods.stream()
                        .filter(pm -> pm.getUser() != null && pm.getUser().getId().equals(customer.getId()))
                        .toList();

                if (!customerPaymentMethods.isEmpty()) {
                    paymentMethod = customerPaymentMethods.get(random.nextInt(customerPaymentMethods.size()));
                } else {
                    paymentMethod = cashPaymentMethod;
                    isOnline = false;
                }
            } else {
                paymentMethod = cashPaymentMethod;
            }

            // Select shipping address for online orders
            ShippingAddresses shippingAddress = null;
            if (isOnline) {
                List<ShippingAddresses> customerAddresses = shippingAddresses.stream()
                        .filter(addr -> addr.getUser() != null && addr.getUser().getId().equals(customer.getId()))
                        .toList();

                if (!customerAddresses.isEmpty()) {
                    shippingAddress = customerAddresses.get(random.nextInt(customerAddresses.size()));
                }
            }

            // Generate order status with realistic distribution
            Constants.OrderStatusEnum status;
            int statusRandom = random.nextInt(100);
            if (statusRandom < 20) {
                status = Constants.OrderStatusEnum.PENDING;
            } else if (statusRandom < 40) {
                status = Constants.OrderStatusEnum.PROCESSING;
            } else if (statusRandom < 90) {
                status = Constants.OrderStatusEnum.COMPLETED;
            } else {
                status = Constants.OrderStatusEnum.CANCELLED;
            }

            // Create order
            Order order = Order.builder()
                    .orderStatus(status)
                    .orderTrackingNumber(CreateTrackingNumber.createTrackingNumber("ORD"))
                    .orderTotalCost(BigDecimal.ZERO) // Will be updated after adding items
                    .orderDiscountCost(BigDecimal.ZERO)
                    .orderTotalCostAfterDiscount(BigDecimal.ZERO)
                    .user(customer)
                    .employee(employee)
                    .paymentMethod(paymentMethod)
                    .shippingAddress(shippingAddress)
                    .build();

            orders.add(order);
        }

        orderRepository.saveAll(orders);
        log.info("Created {} orders", orders.size());

        createOrderDetails(orders, productVariants);
    }

    private void createOrderDetails(List<Order> orders, List<ProductVariant> productVariants) {
        log.info("Creating order details...");

        Random random = new Random();
        List<OrderDetail> allOrderDetails = new ArrayList<>();

        for (Order order : orders) {
            int itemCount = random.nextInt(10) + 1;
            List<OrderDetail> orderDetails = new ArrayList<>();
            BigDecimal orderTotal = BigDecimal.ZERO;
            BigDecimal orderDiscount = BigDecimal.ZERO;

            // Create random unique items for this order
            List<ProductVariant> availableVariants = new ArrayList<>(productVariants);
            for (int i = 0; i < itemCount && !availableVariants.isEmpty(); i++) {
                int variantIndex = random.nextInt(availableVariants.size());
                ProductVariant variant = availableVariants.get(variantIndex);
                availableVariants.remove(variantIndex); // Ensure no duplicates

                int quantity = random.nextInt(3) + 1;
                BigDecimal unitPrice = variant.getVariantPrice();

                // Apply random discount (0-15%)
                int discountPercent = random.nextInt(16);
                BigDecimal discountAmount = unitPrice.multiply(BigDecimal.valueOf(discountPercent))
                        .divide(BigDecimal.valueOf(100));
                BigDecimal unitPriceAfterDiscount = unitPrice.subtract(discountAmount);

                OrderDetail detail = OrderDetail.builder()
                        .order(order)
                        .productVariant(variant)
                        .orderDetailQuantity(quantity)
                        .orderDetailUnitPrice(unitPrice)
                        .orderDetailDiscountCost(discountAmount.multiply(BigDecimal.valueOf(quantity)))
                        .orderDetailUnitPriceAfterDiscount(unitPriceAfterDiscount)
                        .build();

                orderDetails.add(detail);

                // Update order totals
                BigDecimal itemTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
                BigDecimal itemDiscount = discountAmount.multiply(BigDecimal.valueOf(quantity));

                orderTotal = orderTotal.add(itemTotal);
                orderDiscount = orderDiscount.add(itemDiscount);
            }

            // Update order with calculated totals
            order.setOrderTotalCost(orderTotal);
            order.setOrderDiscountCost(orderDiscount);
            order.setOrderTotalCostAfterDiscount(orderTotal.subtract(orderDiscount));
            order.setOrderDetails(orderDetails);

            allOrderDetails.addAll(orderDetails);
        }

        orderDetailRepository.saveAll(allOrderDetails);
        orderRepository.saveAll(orders);

        log.info("Created {} order details across {} orders", allOrderDetails.size(), orders.size());
    }
}