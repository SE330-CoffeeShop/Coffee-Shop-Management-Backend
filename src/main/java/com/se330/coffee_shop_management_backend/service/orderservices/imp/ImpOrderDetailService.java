package com.se330.coffee_shop_management_backend.service.orderservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.order.OrderDetailCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.order.OrderDetailUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Order;
import com.se330.coffee_shop_management_backend.entity.OrderDetail;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import com.se330.coffee_shop_management_backend.repository.OrderDetailRepository;
import com.se330.coffee_shop_management_backend.repository.OrderRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductVariantRepository;
import com.se330.coffee_shop_management_backend.service.orderservices.IOrderDetailService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ImpOrderDetailService implements IOrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final ProductVariantRepository productVariantRepository;
    private final OrderRepository orderRepository;

    public ImpOrderDetailService(
            OrderDetailRepository orderDetailRepository,
            ProductVariantRepository productVariantRepository,
            OrderRepository orderRepository
    ) {
        this.orderDetailRepository = orderDetailRepository;
        this.productVariantRepository = productVariantRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderDetail findByIdOrderDetail(UUID id) {
        return orderDetailRepository.findById(id).orElse(null);
    }

    @Override
    public Page<OrderDetail> findAllOrderDetails(Pageable pageable) {
        return orderDetailRepository.findAll(pageable);
    }

    @Override
    public OrderDetail createOrderDetail(OrderDetailCreateRequestDTO orderDetailCreateRequestDTO) {
        ProductVariant existingProductVariant = productVariantRepository.findById(orderDetailCreateRequestDTO.getProductVariantId())
                .orElseThrow(() -> new EntityNotFoundException("Product variant not found with id: " + orderDetailCreateRequestDTO.getProductVariantId()));

        Order existingOrder = orderRepository.findById(orderDetailCreateRequestDTO.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderDetailCreateRequestDTO.getOrderId()));

        return orderDetailRepository.save(
                OrderDetail.builder()
                        .orderDetailQuantity(orderDetailCreateRequestDTO.getOrderDetailQuantity())
                        .orderDetailUnitPrice(orderDetailCreateRequestDTO.getOrderDetailUnitPrice())
                        .productVariant(existingProductVariant)
                        .order(existingOrder)
                        .build()
        );
    }

    @Override
    public OrderDetail updateOrderDetail(OrderDetailUpdateRequestDTO orderDetailUpdateRequestDTO) {
        OrderDetail existingOrderDetail = orderDetailRepository.findById(orderDetailUpdateRequestDTO.getOrderDetailId())
                .orElseThrow(() -> new EntityNotFoundException("Order detail not found with id: " + orderDetailUpdateRequestDTO.getOrderDetailId()));

        ProductVariant existingProductVariant = productVariantRepository.findById(orderDetailUpdateRequestDTO.getProductVariantId())
                .orElseThrow(() -> new EntityNotFoundException("Product variant not found with id: " + orderDetailUpdateRequestDTO.getProductVariantId()));

        Order existingOrder = orderRepository.findById(orderDetailUpdateRequestDTO.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderDetailUpdateRequestDTO.getOrderId()));

        existingOrderDetail.setOrderDetailQuantity(orderDetailUpdateRequestDTO.getOrderDetailQuantity());
        existingOrderDetail.setOrderDetailUnitPrice(orderDetailUpdateRequestDTO.getOrderDetailUnitPrice());
        existingOrderDetail.setProductVariant(existingProductVariant);
        existingOrderDetail.setOrder(existingOrder);

        return orderDetailRepository.save(existingOrderDetail);
    }

    @Override
    @Transactional
    public void deleteOrderDetail(UUID id) {
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order detail not found with id: " + id));

        if (existingOrderDetail.getOrder() != null) {
            existingOrderDetail.getOrder().getOrderDetails().remove(existingOrderDetail);
        }

        if (existingOrderDetail.getProductVariant() != null) {
            existingOrderDetail.getProductVariant().getOrderDetails().remove(existingOrderDetail);
        }

        orderDetailRepository.delete(existingOrderDetail);
    }
}
