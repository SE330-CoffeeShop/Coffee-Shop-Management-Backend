package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID>, JpaSpecificationExecutor<OrderDetail> {
    @EntityGraph(attributePaths = {"order", "productVariant"})
    List<OrderDetail> findAllByOrder_Id(UUID orderId);

    @Override
    @EntityGraph(attributePaths = {"order", "productVariant"})
    Optional<OrderDetail> findById(UUID id);

    @Override
    @EntityGraph(attributePaths = {"order", "productVariant"})
    Page<OrderDetail> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"order", "productVariant"})
    OrderDetail save(OrderDetail orderDetail);

    // Added EntityGraph to additional common repository methods
    @Override
    @EntityGraph(attributePaths = {"order", "productVariant"})
    List<OrderDetail> findAll();

    @Override
    @EntityGraph(attributePaths = {"order", "productVariant"})
    List<OrderDetail> findAllById(Iterable<UUID> ids);
}