package com.se330.coffee_shop_management_backend.service.productservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.product.ProductVariantRequestDTO;
import com.se330.coffee_shop_management_backend.entity.product.Product;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductCategoryRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductVariantRepository;
import com.se330.coffee_shop_management_backend.service.productservices.IProductVariantService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ImpProductVariantService implements IProductVariantService {

    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository;

    public ImpProductVariantService(
            ProductVariantRepository productVariantRepository,
            ProductRepository productRepository
    ) {
        this.productVariantRepository = productVariantRepository;
        this.productRepository = productRepository;
    }

    @Override
    public ProductVariant findByIdProductVariant(UUID id) {
        return productVariantRepository.findById(id).orElse(null);
    }

    @Override
    public List<ProductVariant> findAllProductVariants() {
        return productVariantRepository.findAll();
    }

    @Override
    public ProductVariant createProductVariant(ProductVariantRequestDTO productVariantRequestDTO) {
        Product product = productRepository.findById(productVariantRequestDTO.getProduct())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productVariantRequestDTO.getProduct()));

        return productVariantRepository.save(
                ProductVariant.builder()
                        .varTierIdx(productVariantRequestDTO.getVarTierIdx())
                        .varDefault(productVariantRequestDTO.getVarDefault())
                        .varSlug(productVariantRequestDTO.getVarSlug())
                        .varSort(productVariantRequestDTO.getVarSort())
                        .varPrice(productVariantRequestDTO.getVarPrice())
                        .varStock(productVariantRequestDTO.getVarStock())
                        .varIsPublished(productVariantRequestDTO.getVarIsPublished())
                        .varIsDeleted(productVariantRequestDTO.getVarIsDeleted())
                        .product(product)
                        .build()
        );
    }

    @Override
    public ProductVariant updateProductVariant(ProductVariantRequestDTO productVariantRequestDTO) {
        Product product = productRepository.findById(productVariantRequestDTO.getProduct())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productVariantRequestDTO.getProduct()));

        ProductVariant existingVariant = productVariantRepository.findById(productVariantRequestDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Product Variant not found with ID: " + productVariantRequestDTO.getId()));

        existingVariant.setVarTierIdx(productVariantRequestDTO.getVarTierIdx());
        existingVariant.setVarDefault(productVariantRequestDTO.getVarDefault());
        existingVariant.setVarSlug(productVariantRequestDTO.getVarSlug());
        existingVariant.setVarSort(productVariantRequestDTO.getVarSort());
        existingVariant.setVarPrice(productVariantRequestDTO.getVarPrice());
        existingVariant.setVarStock(productVariantRequestDTO.getVarStock());
        existingVariant.setVarIsPublished(productVariantRequestDTO.getVarIsPublished());
        existingVariant.setVarIsDeleted(productVariantRequestDTO.getVarIsDeleted());
        existingVariant.setProduct(product);

        return productVariantRepository.save(existingVariant);
    }

    @Override
    public void deleteProductVariant(UUID id) {
        productVariantRepository.deleteById(id);
    }
}
