package com.se330.coffee_shop_management_backend.service.productservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.product.ProductVariantCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.product.ProductVariantUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.product.Product;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductVariantRepository;
import com.se330.coffee_shop_management_backend.service.productservices.IProductVariantService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public Page<ProductVariant> findAllProductVariants(Pageable pageable) {
        return productVariantRepository.findAll(pageable);
    }

    @Override
    public ProductVariant createProductVariant(ProductVariantCreateRequestDTO productVariantCreateRequestDTO) {
        Product product = productRepository.findById(productVariantCreateRequestDTO.getProduct())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productVariantCreateRequestDTO.getProduct()));

        return productVariantRepository.save(
                ProductVariant.builder()
                        .variantTierIdx(productVariantCreateRequestDTO.getVariantTierIdx())
                        .variantDefault(productVariantCreateRequestDTO.getVariantDefault())
                        .variantPrice(productVariantCreateRequestDTO.getVariantPrice())
                        .product(product)
                        .variantSlug("")
                        .variantSort(0)
                        .variantStock(0)
                        .variantIsPublished(false)
                        .variantIsDeleted(false)
                        .build()
        );
    }

    @Override
    public ProductVariant updateProductVariant(ProductVariantUpdateRequestDTO productVariantUpdateRequestDTO) {
        Product product = productRepository.findById(productVariantUpdateRequestDTO.getProduct())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productVariantUpdateRequestDTO.getProduct()));

        ProductVariant existingVariant = productVariantRepository.findById(productVariantUpdateRequestDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Product Variant not found with ID: " + productVariantUpdateRequestDTO.getId()));

        existingVariant.setVariantTierIdx(productVariantUpdateRequestDTO.getVariantTierIdx());
        existingVariant.setVariantDefault(productVariantUpdateRequestDTO.getVariantDefault());
        existingVariant.setVariantSlug(productVariantUpdateRequestDTO.getVariantSlug());
        existingVariant.setVariantSort(productVariantUpdateRequestDTO.getVariantSort());
        existingVariant.setVariantPrice(productVariantUpdateRequestDTO.getVariantPrice());
        existingVariant.setVariantStock(productVariantUpdateRequestDTO.getVariantStock());
        existingVariant.setVariantIsPublished(productVariantUpdateRequestDTO.getVariantIsPublished());
        existingVariant.setVariantIsDeleted(productVariantUpdateRequestDTO.getVariantIsDeleted());
        existingVariant.setProduct(product);

        return productVariantRepository.save(existingVariant);
    }

    @Override
    public void deleteProductVariant(UUID id) {
        productVariantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product Variant not found with ID: " + id));

        productVariantRepository.deleteById(id);
    }
}
