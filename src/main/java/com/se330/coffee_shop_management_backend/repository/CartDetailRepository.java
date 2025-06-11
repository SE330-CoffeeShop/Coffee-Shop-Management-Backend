package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.CartDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, UUID>, JpaSpecificationExecutor<CartDetail> {
    Page<CartDetail> findAllByCart_Id(UUID cartId, Pageable pageable);

    List<CartDetail> findAllByCart_Id(UUID cartId);

    void deleteAllByCart_Id(UUID cartId);

    CartDetail findByCart_IdAndProductVariant_Id(UUID cartId, UUID productVariantId);

    Page<CartDetail> findAllByCart_User_Id(UUID cartUserId, Pageable pageable);
}
