package com.se330.coffee_shop_management_backend.service.productservices.imp;

import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductCategoryRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductVariantRepository;
import com.se330.coffee_shop_management_backend.service.productservices.IProductVariantService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImpProductVariantService implements IProductVariantService {

    private final ProductVariantRepository productVariantRepository;

    public ImpProductVariantService(ProductVariantRepository productVariantRepository, ProductCategoryRepository productCategoryRepository) {
        this.productVariantRepository = productVariantRepository;
    }

    @Override
    public Optional<ProductVariant> findByIdProductVariant(UUID id) {
        return productVariantRepository.findById(id);
    }

    @Override
    public List<ProductVariant> findAllProductVariants() {
        return productVariantRepository.findAll();
    }

    @Override
    public ProductVariant createProductVariant(ProductVariant productVariant) {
        return productVariantRepository.save(productVariant);
    }

    @Override
    public ProductVariant updateProductVariant(ProductVariant productVariant) {
        return productVariantRepository.save(productVariant);
    }

    @Override
    public void deleteProductVariant(UUID id) {
        productVariantRepository.deleteById(id);
    }
}
