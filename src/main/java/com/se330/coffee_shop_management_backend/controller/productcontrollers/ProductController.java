package com.se330.coffee_shop_management_backend.controller.productcontrollers;

import com.se330.coffee_shop_management_backend.dto.request.product.ProductRequestDTO;
import com.se330.coffee_shop_management_backend.entity.product.Product;
import com.se330.coffee_shop_management_backend.service.productservices.IProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public Product findByIdProduct(@PathVariable UUID id) {
        return productService.findByIdProduct(id);
    }

    @GetMapping("/all")
    public List<Product> findAllProducts() {
        return productService.findAllProducts();
    }

    @PostMapping("/")
    public Product createProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        return productService.createProduct(productRequestDTO);
    }

    @PatchMapping("/")
    public Product updateProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        return productService.updateProduct(productRequestDTO);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return "Product deleted successfully";
    }
}
