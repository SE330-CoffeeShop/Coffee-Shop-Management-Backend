package com.se330.coffee_shop_management_backend.repository.productrepositories;

import com.se330.coffee_shop_management_backend.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ProductRepository  extends JpaRepository<Product,UUID>, JpaSpecificationExecutor<Product> {
}
