package com.se330.coffee_shop_management_backend.service.productservices.imp;

import com.se330.coffee_shop_management_backend.entity.product.Product_ProductVariant;
import com.se330.coffee_shop_management_backend.repository.productrepositories.Product_ProductVariantRepository;
import com.se330.coffee_shop_management_backend.service.productservices.IProduct_ProductVariantService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImpProductProductVariantServiceService implements IProduct_ProductVariantService {

    private final Product_ProductVariantRepository product_ProductVariantRepository;

    public ImpProductProductVariantServiceService(Product_ProductVariantRepository product_ProductVariantRepository) {
        this.product_ProductVariantRepository = product_ProductVariantRepository;
    }

    @Override
    public Optional<Product_ProductVariant> findByIdProduct_ProductVariant(UUID id) {
        return product_ProductVariantRepository.findById(id);
    }

    @Override
    public List<Product_ProductVariant> findAllProduct_ProductVariant() {
        return product_ProductVariantRepository.findAll();
    }

    @Override
    public Product_ProductVariant createProduct_ProductVariant(Product_ProductVariant product_ProductVariant) {
        return product_ProductVariantRepository.save(product_ProductVariant);
    }

    @Override
    public Product_ProductVariant updateProduct_ProductVariant(Product_ProductVariant product_ProductVariant) {
        return product_ProductVariantRepository.save(product_ProductVariant);
    }

    @Override
    public void deleteByIdProduct_ProductVariant(UUID id) {
        product_ProductVariantRepository.deleteById(id);
    }
}
