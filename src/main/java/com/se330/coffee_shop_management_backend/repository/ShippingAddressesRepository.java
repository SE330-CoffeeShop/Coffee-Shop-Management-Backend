package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.ShippingAddresses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShippingAddressesRepository extends JpaRepository<ShippingAddresses, UUID>, JpaSpecificationExecutor<ShippingAddresses> {
    List<ShippingAddresses> id(UUID id);
}
