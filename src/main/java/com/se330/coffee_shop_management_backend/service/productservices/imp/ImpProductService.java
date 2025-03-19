package com.se330.coffee_shop_management_backend.service.productservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.product.ProductRequestDTO;
import com.se330.coffee_shop_management_backend.entity.product.Product;
import com.se330.coffee_shop_management_backend.entity.product.ProductCategory;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductCategoryRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductVariantRepository;
import com.se330.coffee_shop_management_backend.service.productservices.IProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ImpProductService implements IProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductVariantRepository productVariantRepository;

    public ImpProductService(
            ProductRepository productRepository,
            ProductCategoryRepository productCategoryRepository,
            ProductVariantRepository productVariantRepository
    ) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productVariantRepository = productVariantRepository;
    }

    @Override
    public Product findByIdProduct(UUID id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(ProductRequestDTO productRequestDTO) {
        ProductCategory category = productCategoryRepository.findById(productRequestDTO.getProductCategory())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + productRequestDTO.getProductCategory()));

        return productRepository.save(
                Product.builder()
                        .productCategory(category)
                        .proDescription(productRequestDTO.getProDescription())
                        .proPrice(productRequestDTO.getProPrice())
                        .proName(productRequestDTO.getProName())
                        .proIsDeleted(productRequestDTO.getProIsDeleted())
                        .proSlug(productRequestDTO.getProSlug())
                        .proIsPublished(productRequestDTO.getProIsPublished())
                        .proRatingsAverage(productRequestDTO.getProRatingsAverage())
                        .proThumb(productRequestDTO.getProThumb())
                        .build()
        );
    }

    @Override
    public Product updateProduct(ProductRequestDTO productRequestDTO) {
        Product existingProduct = productRepository.findById(productRequestDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productRequestDTO.getId()));

        ProductCategory category = productCategoryRepository.findById(productRequestDTO.getProductCategory())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + productRequestDTO.getProductCategory()));

        existingProduct.setProductCategory(category);
        existingProduct.setProDescription(productRequestDTO.getProDescription());
        existingProduct.setProPrice(productRequestDTO.getProPrice());
        existingProduct.setProName(productRequestDTO.getProName());
        existingProduct.setProIsDeleted(productRequestDTO.getProIsDeleted());
        existingProduct.setProSlug(productRequestDTO.getProSlug());
        existingProduct.setProIsPublished(productRequestDTO.getProIsPublished());
        existingProduct.setProRatingsAverage(productRequestDTO.getProRatingsAverage());
        existingProduct.setProThumb(productRequestDTO.getProThumb());

        List<UUID> productVariantIds = productRequestDTO.getProductVariants();
        List<ProductVariant> productVariants = productVariantRepository.findAllById(productVariantIds);
        existingProduct.setProductVariants(productVariants);

        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }
}
