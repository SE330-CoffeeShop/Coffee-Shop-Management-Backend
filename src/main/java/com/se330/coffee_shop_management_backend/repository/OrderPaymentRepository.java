package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.OrderPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderPaymentRepository extends JpaRepository<OrderPayment, UUID>, JpaSpecificationExecutor<OrderPayment> {
    Page<OrderPayment> findAllByOrder_User_Id(UUID orderUserId, Pageable pageable);

    OrderPayment findByTransactionId(String transactionId);

    OrderPayment findByOrder_Id(UUID orderId);
}