package com.se330.coffee_shop_management_backend.service.productservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.product.ProductVariantRequestDTO;
import com.se330.coffee_shop_management_backend.entity.product.Product;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductCategoryRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductVariantRepository;
import com.se330.coffee_shop_management_backend.service.productservices.IProductVariantService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<ProductVariant> findAllProductVariants(Pageable pageable) {
        return productVariantRepository.findAll(pageable);
    }

    @Override
    public ProductVariant createProductVariant(ProductVariantRequestDTO productVariantRequestDTO) {
        Product product = productRepository.findById(productVariantRequestDTO.getProduct())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productVariantRequestDTO.getProduct()));

        return productVariantRepository.save(
                ProductVariant.builder()
                        .variantTierIdx(productVariantRequestDTO.getVariantTierIdx())
                        .variantDefault(productVariantRequestDTO.getVariantDefault())
                        .variantSlug(productVariantRequestDTO.getVariantSlug())
                        .variantSort(productVariantRequestDTO.getVariantSort())
                        .variantPrice(productVariantRequestDTO.getVariantPrice())
                        .variantStock(productVariantRequestDTO.getVariantStock())
                        .variantIsPublished(productVariantRequestDTO.getVariantIsPublished())
                        .variantIsDeleted(productVariantRequestDTO.getVariantIsDeleted())
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

        existingVariant.setVariantTierIdx(productVariantRequestDTO.getVariantTierIdx());
        existingVariant.setVariantDefault(productVariantRequestDTO.getVariantDefault());
        existingVariant.setVariantSlug(productVariantRequestDTO.getVariantSlug());
        existingVariant.setVariantSort(productVariantRequestDTO.getVariantSort());
        existingVariant.setVariantPrice(productVariantRequestDTO.getVariantPrice());
        existingVariant.setVariantStock(productVariantRequestDTO.getVariantStock());
        existingVariant.setVariantIsPublished(productVariantRequestDTO.getVariantIsPublished());
        existingVariant.setVariantIsDeleted(productVariantRequestDTO.getVariantIsDeleted());
        existingVariant.setProduct(product);

        return productVariantRepository.save(existingVariant);
    }

    @Override
    public void deleteProductVariant(UUID id) {
        productVariantRepository.deleteById(id);
    }
}
