package com.se330.coffee_shop_management_backend.controller.productcontrollers;

import com.se330.coffee_shop_management_backend.dto.request.product.ProductVariantRequestDTO;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import com.se330.coffee_shop_management_backend.service.productservices.IProductVariantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/product/variant")
public class ProductVariantController {

    private final IProductVariantService productVariantService;

    public ProductVariantController(
            IProductVariantService productVariantService
    ) {
        this.productVariantService = productVariantService;
    }

    @GetMapping("/{id}")
    public ProductVariant findByIdProductVariant(@PathVariable UUID id) {
        return productVariantService.findByIdProductVariant(id);
    }

    @GetMapping("/all")
    public List<ProductVariant> findAllProductVariants() {
        return productVariantService.findAllProductVariants();
    }

    @PostMapping("/")
    public ProductVariant createProductVariant(@RequestBody ProductVariantRequestDTO  productVariantRequestDTO) {
        return productVariantService.createProductVariant(productVariantRequestDTO);
    }

    @PatchMapping("/")
    public ProductVariant updateProductVariant(@RequestBody ProductVariantRequestDTO productVariantRequestDTO) {
        return productVariantService.updateProductVariant(productVariantRequestDTO);
    }

    @DeleteMapping("/{id}")
    public String deleteProductVariant(@PathVariable UUID id) {
        productVariantService.deleteProductVariant(id);
        return "Product variant deleted successfully";
    }
}
