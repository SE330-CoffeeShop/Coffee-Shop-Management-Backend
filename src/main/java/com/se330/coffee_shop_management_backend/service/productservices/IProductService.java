package com.se330.coffee_shop_management_backend.service.productservices;

import com.se330.coffee_shop_management_backend.dto.request.product.ProductCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.product.ProductUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IProductService {
    Product findByIdProduct(UUID id);
    Page<Product> findAllProducts(Pageable pageable);
    Product createProduct(ProductCreateRequestDTO productCreateRequestDTO);
    Product updateProduct(ProductUpdateRequestDTO productUpdateRequestDTORequestDTO);
    void deleteProduct(UUID id);
}
