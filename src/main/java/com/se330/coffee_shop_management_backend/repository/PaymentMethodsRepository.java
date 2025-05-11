package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.PaymentMethods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentMethodsRepository extends JpaRepository<PaymentMethods, UUID>, JpaSpecificationExecutor<PaymentMethods> {
}