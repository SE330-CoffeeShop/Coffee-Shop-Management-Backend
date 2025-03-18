package com.se330.coffee_shop_management_backend.controller.productcontrollers;

import com.se330.coffee_shop_management_backend.entity.product.Product_ProductVariant;
import com.se330.coffee_shop_management_backend.service.productservices.IProduct_ProductVariantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("product/product_product-variant")
public class Product_ProductVariantController {

    private final IProduct_ProductVariantService product_ProductVariantService;

    public Product_ProductVariantController(IProduct_ProductVariantService product_ProductVariantService) {
        this.product_ProductVariantService = product_ProductVariantService;
    }

    @GetMapping("/{id}")
    public Optional<Product_ProductVariant> findByIdProduct_ProductVariant(@PathVariable UUID id) {
        return product_ProductVariantService.findByIdProduct_ProductVariant(id);
    }

    @GetMapping("/")
    public List<Product_ProductVariant> findAllProduct_ProductVariant() {
        return product_ProductVariantService.findAllProduct_ProductVariant();
    }

    @PostMapping("/")
    public Product_ProductVariant createProduct_ProductVariant(@RequestBody Product_ProductVariant product_ProductVariant) {
        return product_ProductVariantService.createProduct_ProductVariant(product_ProductVariant);
    }

    @PatchMapping("/")
    public Product_ProductVariant updateProduct_ProductVariant(@RequestBody Product_ProductVariant product_ProductVariant) {
        return product_ProductVariantService.updateProduct_ProductVariant(product_ProductVariant);
    }

    @DeleteMapping("/{id}")
    public String deleteProductCategory(@PathVariable UUID id) {
        product_ProductVariantService.deleteByIdProduct_ProductVariant(id);
        return "Product deleted successfully";
    }
}
