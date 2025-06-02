package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.Branch;
import com.se330.coffee_shop_management_backend.entity.Discount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, UUID>, JpaSpecificationExecutor<Discount> {
    Page<Discount> findAllByBranch(Branch existingBranch, Pageable pageable);
    Page<Discount> findAllByProductVariants_Id(UUID productVariantId, Pageable pageable);
}