package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID>, JpaSpecificationExecutor<OrderDetail> {
}