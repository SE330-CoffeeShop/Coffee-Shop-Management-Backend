package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.UsedDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface UsedDiscountRepository extends JpaRepository<UsedDiscount, UUID>, JpaSpecificationExecutor<UsedDiscount> {
    @Query("SELECT COALESCE(SUM(ud.timesUse), 0) FROM UsedDiscount ud " +
            "JOIN ud.orderDetail od " +
            "JOIN od.order o " +
            "JOIN o.user u " +
            "WHERE ud.discount.id = :discountId AND u.id = :userId")
    int sumTimesUsedByUserAndDiscount(@Param("discountId") UUID discountId,
                                      @Param("userId") UUID userId);
}
