package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, UUID>, JpaSpecificationExecutor<Inventory> {
    Page<Inventory> findAllByBranch_Id(UUID branchId, Pageable pageable);

    @Query("SELECT i FROM Inventory i WHERE i.branch.id = :branchId AND i.inventoryExpireDate >= CURRENT_TIMESTAMP ORDER BY i.inventoryExpireDate ASC")
    List<Inventory> findAllByBranch_IdSortedByExpiredDay(UUID branchId);
}