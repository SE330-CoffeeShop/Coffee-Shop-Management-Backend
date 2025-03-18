package com.se330.coffee_shop_management_backend.repository.productrepositories;

import com.se330.coffee_shop_management_backend.entity.product.Product_ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface Product_ProductVariantRepository extends JpaRepository<Product_ProductVariant, UUID>, JpaSpecificationExecutor<Product_ProductVariant> {
}
