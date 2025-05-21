package com.se330.coffee_shop_management_backend.service.orderservices;

import com.se330.coffee_shop_management_backend.dto.request.order.OrderCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.order.OrderUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IOrderService {
    Order findByIdOrder(UUID id);
    Page<Order> findAllOrders(Pageable pageable);
    Order createOrder(OrderCreateRequestDTO orderCreateRequestDTO);
    Order updateOrder(OrderUpdateRequestDTO orderUpdateRequestDTO);
    void deleteOrder(UUID id);
}