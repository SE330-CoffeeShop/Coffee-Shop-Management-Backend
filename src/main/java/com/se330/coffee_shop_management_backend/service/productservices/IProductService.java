package com.se330.coffee_shop_management_backend.service.productservices;

import com.se330.coffee_shop_management_backend.entity.product.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IProductService {
    Optional<Product> findByIdProduct(UUID id);
    List<Product> findAllProducts();
    Product createProduct(Product product);
    Product updateProduct(Product product);
    void deleteProduct(UUID id);
}
