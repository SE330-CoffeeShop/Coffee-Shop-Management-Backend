package com.se330.coffee_shop_management_backend.repository.productrepositories;

import com.se330.coffee_shop_management_backend.entity.product.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, UUID>, JpaSpecificationExecutor<ProductCategory> {
    @Override
    Page<ProductCategory> findAll(Pageable pageable);
}
