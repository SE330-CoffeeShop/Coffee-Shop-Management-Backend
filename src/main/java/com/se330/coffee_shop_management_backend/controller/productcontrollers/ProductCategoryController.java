package com.se330.coffee_shop_management_backend.controller.productcontrollers;

import com.se330.coffee_shop_management_backend.entity.product.ProductCategory;
import com.se330.coffee_shop_management_backend.service.productservices.IProductCategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/product/category")
public class ProductCategoryController {

    private final IProductCategoryService productCategoryService;

    public ProductCategoryController(IProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @GetMapping("/{id}")
    public ProductCategory findByIdProductCategory(@PathVariable UUID id) {
        return productCategoryService.findByIdProductCategory(id);
    }

    @GetMapping("/all")
    public List<ProductCategory> findAllProductCategories() {
        return productCategoryService.findAllProductCategories();
    }

    @PostMapping("/")
    public ProductCategory createProductCategory(@RequestBody ProductCategory product) {
        return productCategoryService.createProductCategory(product);
    }

    @PatchMapping("/")
    public ProductCategory updateProductCategory(@RequestBody ProductCategory product) {
        return productCategoryService.updateProductCategory(product);
    }

    @DeleteMapping("/{id}")
    public String deleteProductCategory(@PathVariable UUID id) {
        productCategoryService.deleteProductCategory(id);
        return "Product category deleted successfully";
    }
}