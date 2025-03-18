package com.se330.coffee_shop_management_backend.service.productservices;

import com.se330.coffee_shop_management_backend.dto.request.product.ProductRequestDTO;
import com.se330.coffee_shop_management_backend.entity.product.Product;

import java.util.List;
import java.util.UUID;

public interface IProductService {
    Product findByIdProduct(UUID id);
    List<Product> findAllProducts();
    Product createProduct(ProductRequestDTO productRequestDTO);
    Product updateProduct(ProductRequestDTO productRequestDTO);
    void deleteProduct(UUID id);
}
