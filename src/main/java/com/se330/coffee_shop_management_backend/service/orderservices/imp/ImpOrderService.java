package com.se330.coffee_shop_management_backend.service.orderservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.cart.CartDetailCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.order.OrderCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.order.OrderDetailCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.order.OrderUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.*;
import com.se330.coffee_shop_management_backend.repository.*;
import com.se330.coffee_shop_management_backend.service.discountservices.IDiscountService;
import com.se330.coffee_shop_management_backend.service.orderservices.IOrderDetailService;
import com.se330.coffee_shop_management_backend.service.orderservices.IOrderService;
import com.se330.coffee_shop_management_backend.util.Constants;
import com.se330.coffee_shop_management_backend.util.CreateTrackingNumber;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    private final IDiscountService discountService;
    private final CartRepository cartRepository;

    public ImpOrderService(
            OrderRepository orderRepository,
            EmployeeRepository employeeRepository,
            PaymentMethodsRepository paymentMethodsRepository,
            UserRepository userRepository,
            ShippingAddressesRepository shippingAddressesRepository,
            IOrderDetailService orderDetailService,
            IDiscountService discountService,
            CartRepository cartRepository
    ) {
        this.orderRepository = orderRepository;
        this.employeeRepository = employeeRepository;
        this.paymentMethodsRepository = paymentMethodsRepository;
        this.userRepository = userRepository;
        this.shippingAddressesRepository = shippingAddressesRepository;
        this.orderDetailService = orderDetailService;
        this.discountService = discountService;
        this.cartRepository = cartRepository;
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
    public Page<Order> findAllOrderByCustomerId(UUID customerId, Pageable pageable) {
        return orderRepository.findAllByUser_Id(customerId, pageable);
    }

    /**
     * Creates a new order with associated order details and applies available discounts.
     *
     * <p>This method performs the following steps:</p>
     * <ol>
     *   <li>Validates and retrieves required entities (employee, user, shipping address, payment method)</li>
     *   <li>Creates a new Order with initial total cost of zero</li>
     *   <li>Creates OrderDetail entities for each product in the request</li>
     *   <li>Calculates the initial total cost</li>
     *   <li>Applies the most valuable discount to each order detail based on total order value</li>
     *   <li>Recalculates the final total cost after discounts are applied</li>
     *   <li>Updates and returns the completed order</li>
     * </ol>
     *
     * @param orderCreateRequestDTO The data transfer object containing all information needed to create the order:
     *                             employee ID, user ID, shipping address ID, payment method ID, order status,
     *                             tracking number, and list of order details with product variants and quantities
     * @return The fully created and populated Order entity with all relationships established and total cost calculated
     * @throws EntityNotFoundException If any referenced entity (employee, user, shipping address, payment method) cannot be found
     */
    @Override
    @Transactional
    public Order createOrder(OrderCreateRequestDTO orderCreateRequestDTO) {
        Employee existingEmployee = employeeRepository.findById(orderCreateRequestDTO.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + orderCreateRequestDTO.getEmployeeId()));

        User existingUser = userRepository.findById(orderCreateRequestDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id:" + orderCreateRequestDTO.getUserId()));

        ShippingAddresses existingShippingAddress = shippingAddressesRepository.findById(orderCreateRequestDTO.getShippingAddressId())
                .orElseThrow(() -> new EntityNotFoundException("Shipping address not found with id:" + orderCreateRequestDTO.getShippingAddressId()));

        PaymentMethods existingPaymentMethod = paymentMethodsRepository.findById(orderCreateRequestDTO.getPaymentMethodId())
                .orElseThrow(() -> new EntityNotFoundException("Payment method not found with id:" + orderCreateRequestDTO.getPaymentMethodId()));
        
        Cart existingCart = cartRepository.findById(orderCreateRequestDTO.getCartId())
                .orElseThrow(() -> new EntityNotFoundException("Cart not found with id:" + orderCreateRequestDTO.getCartId()));

        // create order first
        Order newOrder = orderRepository.save(
            Order.builder()
                    .employee(existingEmployee)
                    .orderStatus(Constants.OrderStatusEnum.get(orderCreateRequestDTO.getOrderStatus()))
                    .orderTrackingNumber(CreateTrackingNumber.createTrackingNumber("ORDER"))
                    .paymentMethod(existingPaymentMethod)
                    .user(existingUser)
                    .shippingAddress(existingShippingAddress)
                    .orderTotalCost(BigDecimal.ZERO)
                    .orderDiscountCost(BigDecimal.ZERO)
                    .orderTotalCostAfterDiscount(BigDecimal.ZERO)
                    .build()
        );

        List<OrderDetailCreateRequestDTO> orderDetailDtos = new ArrayList<>();
        for (CartDetail cartDetailCreateRequestDTO : existingCart.getCartDetails()) {
            OrderDetailCreateRequestDTO orderDetailCreateRequestDTO = new OrderDetailCreateRequestDTO();
            orderDetailCreateRequestDTO.setOrderDetailQuantity(cartDetailCreateRequestDTO.getCartDetailQuantity());
            orderDetailCreateRequestDTO.setOrderDetailUnitPrice(cartDetailCreateRequestDTO.getCartDetailUnitPrice());
            orderDetailCreateRequestDTO.setProductVariantId(cartDetailCreateRequestDTO.getProductVariant().getId());
            orderDetailCreateRequestDTO.setOrderId(newOrder.getId());
            orderDetailDtos.add(orderDetailCreateRequestDTO);
        }

        // now then add order details
        for (OrderDetailCreateRequestDTO orderDetailCreateRequestDTO : orderDetailDtos) {
            orderDetailCreateRequestDTO.setOrderId(newOrder.getId());
            orderDetailService.createOrderDetail(orderDetailCreateRequestDTO);
        }

        // Calculate total cost
        BigDecimal totalCost = updateTotalCost(newOrder);
        newOrder.setOrderTotalCost(totalCost);

        // now we loop for each order detail to apply discount for them
        for (OrderDetail orderDetail : newOrder.getOrderDetails()) {
            discountService.applyMostValuableDiscountOfOrderDetail(orderDetail.getId(), totalCost);
        }

        // update again the new total cost since the unit price of some order details have been changed
        totalCost = updateTotalCost(newOrder);
        BigDecimal discountCost = newOrder.getOrderTotalCost().subtract(totalCost);

        // set the new total cost
        newOrder.setOrderDiscountCost(discountCost);
        newOrder.setOrderTotalCostAfterDiscount(totalCost);

        // now delete the cart since we have created the order
        if (existingCart.getUser() != null) {
            existingCart.getUser().getCarts().remove(existingCart);
        }
        cartRepository.delete(existingCart);

        // save order again to update total cost and payment method
        return orderRepository.save(newOrder);
    }

    @Override
    public Order updateOrder(OrderUpdateRequestDTO orderUpdateRequestDTO) {
//        Order existingOrder = orderRepository.findById(orderUpdateRequestDTO.getOrderId())
//                .orElseThrow(() -> new EntityNotFoundException("Order not found with id:" + orderUpdateRequestDTO.getOrderId()));
//
//        PaymentMethods existingPaymentMethod = paymentMethodsRepository.findById(existingOrder.getPaymentMethod().getId())
//                .orElseThrow(() -> new EntityNotFoundException("Payment method not found with id:" + existingOrder.getPaymentMethod().getId()));
//
//        User existingUser = userRepository.findById(orderUpdateRequestDTO.getUserId())
//                .orElseThrow(() -> new EntityNotFoundException("User not found with id:" + orderUpdateRequestDTO.getUserId()));
//
//        ShippingAddresses existingShippingAddress = shippingAddressesRepository.findById(orderUpdateRequestDTO.getShippingAddressId())
//                .orElseThrow(() -> new EntityNotFoundException("Shipping address not found with id:" + orderUpdateRequestDTO.getShippingAddressId()));
//
//        existingOrder.setPaymentMethod(existingPaymentMethod);
//        existingOrder.setUser(existingUser);
//        existingOrder.setShippingAddress(existingShippingAddress);
//        existingOrder.setOrderStatus(Constants.OrderStatusEnum.get(orderUpdateRequestDTO.getOrderStatus()));
//        existingOrder.setOrderTrackingNumber(CreateTrackingNumber.createTrackingNumber("ORDER"));
//
//        List<OrderDetail> orderDetailOlds = existingOrder.getOrderDetails();
//
//        for (OrderDetailCreateRequestDTO orderDetailCreateRequestDTO : orderUpdateRequestDTO.getOrderDetails()) {
//            for (OrderDetail oldOrderDetail : orderDetailOlds) {
//                if (
//                        oldOrderDetail.getOrderDetailQuantity() == orderDetailCreateRequestDTO.getOrderDetailQuantity() &&
//                                Objects.equals(oldOrderDetail.getOrderDetailUnitPrice(), orderDetailCreateRequestDTO.getOrderDetailUnitPrice()) &&
//                        oldOrderDetail.getProductVariant().getId() == orderDetailCreateRequestDTO.getProductVariantId()
//                ) {
//                    // which means this order detail have no change
//                    continue;
//                }
//
//                // remove old, create new
//                orderDetailService.deleteOrderDetail(oldOrderDetail.getId());
//                orderDetailCreateRequestDTO.setOrderId(existingOrder.getId());
//                orderDetailService.createOrderDetail(orderDetailCreateRequestDTO);
//            }
//        }
//
//        // update total cost
//        BigDecimal totalCost = updateTotalCost(existingOrder);
//
//        // then apply discount
//        for (OrderDetail orderDetail : existingOrder.getOrderDetails()) {
//            discountService.applyMostValuableDiscountOfOrderDetail(orderDetail.getId(), totalCost);
//        }
//
//        totalCost = updateTotalCost(existingOrder);
//
//        existingOrder.setOrderTotalCost(totalCost);
//
//        return orderRepository.save(existingOrder);

        deleteOrder(orderUpdateRequestDTO.getOrderId());
        return createOrder(OrderCreateRequestDTO.builder()
                .employeeId(orderUpdateRequestDTO.getEmployeeId())
                .userId(orderUpdateRequestDTO.getUserId())
                .shippingAddressId(orderUpdateRequestDTO.getShippingAddressId())
                .paymentMethodId(orderUpdateRequestDTO.getPaymentMethodId())
                .orderStatus(orderUpdateRequestDTO.getOrderStatus())
                .cartId(orderUpdateRequestDTO.getCartId())
                .build());
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
            BigDecimal itemCost = orderDetail.getOrderDetailUnitPrice().multiply(BigDecimal.valueOf(orderDetail.getOrderDetailQuantity()));
            totalCost = totalCost.add(itemCost);
        }

        return totalCost;
    }
}
