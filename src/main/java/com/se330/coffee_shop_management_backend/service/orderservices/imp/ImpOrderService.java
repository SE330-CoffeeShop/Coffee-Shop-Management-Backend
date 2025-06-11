package com.se330.coffee_shop_management_backend.service.orderservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.order.OrderCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.order.OrderDetailCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.order.OrderUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.*;
import com.se330.coffee_shop_management_backend.repository.*;
import com.se330.coffee_shop_management_backend.service.discountservices.IDiscountService;
import com.se330.coffee_shop_management_backend.service.notificationservices.INotificationService;
import com.se330.coffee_shop_management_backend.service.orderservices.IOrderDetailService;
import com.se330.coffee_shop_management_backend.service.orderservices.IOrderService;
import com.se330.coffee_shop_management_backend.util.Constants;
import com.se330.coffee_shop_management_backend.util.CreateNotiContentHelper;
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
    private final OrderDetailRepository orderDetailRepository;
    private final CartRepository cartRepository;
    private final BranchRepository branchRepository;
    private final INotificationService notificationService;

    public ImpOrderService(
            OrderRepository orderRepository,
            EmployeeRepository employeeRepository,
            PaymentMethodsRepository paymentMethodsRepository,
            UserRepository userRepository,
            ShippingAddressesRepository shippingAddressesRepository,
            OrderDetailRepository orderDetailRepository,
            IOrderDetailService orderDetailService,
            IDiscountService discountService,
            BranchRepository branchRepository,
            CartRepository cartRepository,
            INotificationService notificationService
    ) {
        this.orderRepository = orderRepository;
        this.employeeRepository = employeeRepository;
        this.paymentMethodsRepository = paymentMethodsRepository;
        this.userRepository = userRepository;
        this.shippingAddressesRepository = shippingAddressesRepository;
        this.orderDetailService = orderDetailService;
        this.orderDetailRepository = orderDetailRepository;
        this.discountService = discountService;
        this.cartRepository = cartRepository;
        this.notificationService = notificationService;
        this.branchRepository = branchRepository;
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

    @Override
    public Page<Order> findAllOrderByStatusAndBranchId(Constants.OrderStatusEnum status, UUID branchId, Pageable pageable) {
        return orderRepository.findAllByOrderStatusAndBranch_Id(status, branchId, pageable);
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
        Employee existingEmployee = null;

        if (orderCreateRequestDTO.getEmployeeId() != null) {
            existingEmployee = employeeRepository.findById(orderCreateRequestDTO.getEmployeeId())
                    .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + orderCreateRequestDTO.getEmployeeId()));
        }

        Branch existingBranch = null;

        if (orderCreateRequestDTO.getBranchId() != null) {
            existingBranch = branchRepository.findById(orderCreateRequestDTO.getBranchId())
                    .orElseThrow(() -> new EntityNotFoundException("Branch not found with id: " + orderCreateRequestDTO.getBranchId()));
        }

        User existingUser = userRepository.findById(orderCreateRequestDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id:" + orderCreateRequestDTO.getUserId()));

        ShippingAddresses existingShippingAddress = null;
        if (orderCreateRequestDTO.getShippingAddressId() != null) {
            existingShippingAddress = shippingAddressesRepository.findById(orderCreateRequestDTO.getShippingAddressId())
                    .orElseThrow(() -> new EntityNotFoundException("Shipping address not found with id:" + orderCreateRequestDTO.getShippingAddressId()));
        }

        PaymentMethods existingPaymentMethod = paymentMethodsRepository.findById(orderCreateRequestDTO.getPaymentMethodId())
                .orElseThrow(() -> new EntityNotFoundException("Payment method not found with id:" + orderCreateRequestDTO.getPaymentMethodId()));
        
        Cart existingCart = cartRepository.findByUser_Id(orderCreateRequestDTO.getUserId());

        // create order first
        Order newOrder = orderRepository.save(
            Order.builder()
                    .employee(existingEmployee)
                    .orderStatus(orderCreateRequestDTO.getOrderStatus())
                    .orderTrackingNumber(CreateTrackingNumber.createTrackingNumber("ORDER"))
                    .paymentMethod(existingPaymentMethod)
                    .user(existingUser)
                    .shippingAddress(existingShippingAddress)
                    .orderTotalCost(BigDecimal.ZERO)
                    .orderDiscountCost(BigDecimal.ZERO)
                    .orderTotalCostAfterDiscount(BigDecimal.ZERO)
                    .branch(existingBranch)
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
            orderDetailCreateRequestDTO.setBranchId(orderCreateRequestDTO.getBranchId() != null ? orderCreateRequestDTO.getBranchId() : existingBranch.getId());
            orderDetailService.createOrderDetail(orderDetailCreateRequestDTO);
        }

        // Calculate total cost
        BigDecimal totalCost = updateTotalCost(newOrder.getId());
        newOrder.setOrderTotalCost(totalCost);

        // now we loop for each order detail to apply discount for them
        for (OrderDetail orderDetail : newOrder.getOrderDetails()) {
            discountService.applyMostValuableDiscountOfOrderDetail(orderDetail.getId(), totalCost);
        }

        // update again the new total cost since the unit price of some order details have been changed
        totalCost = updateTotalCost(newOrder.getId());
        BigDecimal discountCost = newOrder.getOrderTotalCost().subtract(totalCost);

        // set the new total cost
        newOrder.setOrderDiscountCost(discountCost);
        newOrder.setOrderTotalCostAfterDiscount(totalCost);

        // save order again to update total cost and payment method
        orderRepository.save(newOrder);

        // now create notification
        if (existingShippingAddress != null) {
            notificationService.createNotification(
                    NotificationCreateRequestDTO.builder()
                            .notificationType(Constants.NotificationTypeEnum.ORDER)
                            .notificationContent(CreateNotiContentHelper.createOrderSuccessContentCustomer(newOrder.getId()))
                            .senderId(null)
                            .receiverId(newOrder.getUser().getId())
                            .isRead(false)
                            .build());
        } else {
            notificationService.createNotification(
                    NotificationCreateRequestDTO.builder()
                            .notificationType(Constants.NotificationTypeEnum.ORDER)
                            .notificationContent(CreateNotiContentHelper.createInStorePurchaseContent(newOrder.getId()))
                            .senderId(null)
                            .receiverId(newOrder.getUser().getId())
                            .isRead(false)
                            .build());
        }

        return orderRepository.findById(newOrder.getId()).orElseThrow();
    }

    @Override
    public Order updateOrder(OrderUpdateRequestDTO orderUpdateRequestDTO) {
        Order existingOrder = orderRepository.findById(orderUpdateRequestDTO.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id:" + orderUpdateRequestDTO.getOrderId()));

        PaymentMethods existingPaymentMethod = paymentMethodsRepository.findById(existingOrder.getPaymentMethod().getId())
                .orElseThrow(() -> new EntityNotFoundException("Payment method not found with id:" + existingOrder.getPaymentMethod().getId()));

        User existingUser = userRepository.findById(orderUpdateRequestDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id:" + orderUpdateRequestDTO.getUserId()));

        ShippingAddresses existingShippingAddress = shippingAddressesRepository.findById(orderUpdateRequestDTO.getShippingAddressId())
                .orElseThrow(() -> new EntityNotFoundException("Shipping address not found with id:" + orderUpdateRequestDTO.getShippingAddressId()));

        if (orderUpdateRequestDTO.getBranchId() != null) {
            Branch existingBranch = branchRepository.findById(orderUpdateRequestDTO.getBranchId())
                    .orElseThrow(() -> new EntityNotFoundException("Branch not found with id:" + orderUpdateRequestDTO.getBranchId()));
            existingOrder.setBranch(existingBranch);
        }
        
        if (existingOrder.getEmployee() == null) {
            if (orderUpdateRequestDTO.getEmployeeId() != null) {
                Employee existingEmployee = employeeRepository.findById(orderUpdateRequestDTO.getEmployeeId())
                        .orElseThrow(() -> new EntityNotFoundException("Employee not found with id:" + orderUpdateRequestDTO.getEmployeeId()));
                
                existingOrder.setEmployee(existingEmployee);
                existingOrder.setOrderStatus(Constants.OrderStatusEnum.PROCESSING);
                
                notificationService.createNotification(NotificationCreateRequestDTO.builder()
                                .notificationType(Constants.NotificationTypeEnum.ORDER)
                                .receiverId(existingEmployee.getUser().getId())
                                .notificationContent(CreateNotiContentHelper.createOrderReceivedContent(existingOrder.getId()))
                                .senderId(null)
                                .isRead(false)
                        .build());
            }
        }

        if (orderUpdateRequestDTO.getOrderStatus() == Constants.OrderStatusEnum.COMPLETED && existingOrder.getOrderStatus() != Constants.OrderStatusEnum.COMPLETED) {
            notificationService.createNotification(NotificationCreateRequestDTO.builder()
                            .notificationType(Constants.NotificationTypeEnum.ORDER)
                            .receiverId(existingUser.getId())
                            .notificationContent(CreateNotiContentHelper.createOrderCompletedContent(existingOrder.getId()))
                            .senderId(null)
                            .isRead(false)
                    .build());
        }

        if (existingOrder.getOrderStatus() != Constants.OrderStatusEnum.DELIVERING && orderUpdateRequestDTO.getOrderStatus() == Constants.OrderStatusEnum.DELIVERING) {
            notificationService.createNotification(NotificationCreateRequestDTO.builder()
                    .notificationType(Constants.NotificationTypeEnum.ORDER)
                    .receiverId(existingUser.getId())
                    .notificationContent(CreateNotiContentHelper.orderDeliveringContent(existingOrder.getId()))
                    .senderId(null)
                    .isRead(false)
                    .build());
        }

        if (existingOrder.getOrderStatus() != Constants.OrderStatusEnum.DELIVERED && orderUpdateRequestDTO.getOrderStatus() == Constants.OrderStatusEnum.DELIVERED) {
            notificationService.createNotification(NotificationCreateRequestDTO.builder()
                    .notificationType(Constants.NotificationTypeEnum.ORDER)
                    .receiverId(existingUser.getId())
                    .notificationContent(CreateNotiContentHelper.orderDeliveredContent(existingOrder.getId()))
                    .senderId(null)
                    .isRead(false)
                    .build());
        }

        if (existingOrder.getOrderStatus() != Constants.OrderStatusEnum.CANCELLED && orderUpdateRequestDTO.getOrderStatus() == Constants.OrderStatusEnum.CANCELLED) {
            notificationService.createNotification(NotificationCreateRequestDTO.builder()
                            .notificationType(Constants.NotificationTypeEnum.ORDER)
                            .receiverId(existingUser.getId())
                            .notificationContent(CreateNotiContentHelper.createOrderCancelledContent(existingOrder.getId()))
                            .senderId(null)
                            .isRead(false)
                    .build());
        }

        existingOrder.setPaymentMethod(existingPaymentMethod);
        existingOrder.setUser(existingUser);
        existingOrder.setShippingAddress(existingShippingAddress);
        existingOrder.setOrderStatus(orderUpdateRequestDTO.getOrderStatus());
        existingOrder.setOrderTrackingNumber(CreateTrackingNumber.createTrackingNumber("ORDER"));
        
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
            existingOrder.getShippingAddress().getOrders().remove(existingOrder);
        }

        if (existingOrder.getPaymentMethod() != null) {
            existingOrder.setPaymentMethod(null);
        }

        if (existingOrder.getEmployee() != null) {
            existingOrder.setEmployee(null);
        }

        orderRepository.delete(existingOrder);
    }


    private BigDecimal updateTotalCost(UUID orderId) {
        BigDecimal totalCost = BigDecimal.ZERO;

        // Truy vấn trực tiếp tất cả OrderDetails của Order này
        List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrder_Id(orderId);

        for (OrderDetail orderDetail : orderDetails) {
            BigDecimal itemCost = orderDetail.getOrderDetailUnitPrice().multiply(BigDecimal.valueOf(orderDetail.getOrderDetailQuantity()));
            totalCost = totalCost.add(itemCost);
        }

        return totalCost;
    }
}
