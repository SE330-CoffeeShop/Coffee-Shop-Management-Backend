package com.se330.coffee_shop_management_backend.service.productservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.product.ProductCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.product.ProductUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.product.Product;
import com.se330.coffee_shop_management_backend.entity.product.ProductCategory;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductCategoryRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductVariantRepository;
import com.se330.coffee_shop_management_backend.service.productservices.IProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    public Page<Product> findAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Product createProduct(ProductCreateRequestDTO productCreateRequestDTO) {
        ProductCategory category = productCategoryRepository.findById(productCreateRequestDTO.getProductCategory())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + productCreateRequestDTO.getProductCategory()));

        return productRepository.save(
                Product.builder()
                        .productCategory(category)
                        .productDescription(productCreateRequestDTO.getProductDescription())
                        .productPrice(productCreateRequestDTO.getProductPrice())
                        .productName(productCreateRequestDTO.getProductName())
                        .productThumb(productCreateRequestDTO.getProductThumb())
                        .productIsDeleted(false)
                        .productIsPublished(false)
                        .productSlug("") // TODO: slugify product name
                        .productCommentCount(0)
                        .productRatingsAverage(BigDecimal.valueOf(0))
                        .build()
        );
    }

    @Override
    public Product updateProduct(ProductUpdateRequestDTO productUpdateRequestDTO) {
        Product existingProduct = productRepository.findById(productUpdateRequestDTO.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productUpdateRequestDTO.getProductId()));

        ProductCategory category = productCategoryRepository.findById(productUpdateRequestDTO.getProductCategory())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + productUpdateRequestDTO.getProductCategory()));

        existingProduct.setProductCategory(category);
        existingProduct.setProductDescription(productUpdateRequestDTO.getProductDescription());
        existingProduct.setProductPrice(productUpdateRequestDTO.getProductPrice());
        existingProduct.setProductName(productUpdateRequestDTO.getProductName());
        existingProduct.setProductIsDeleted(productUpdateRequestDTO.getProductIsDeleted());
        existingProduct.setProductSlug(productUpdateRequestDTO.getProductSlug());
        existingProduct.setProductIsPublished(productUpdateRequestDTO.getProductIsPublished());
        existingProduct.setProductRatingsAverage(productUpdateRequestDTO.getProductRatingsAverage());
        existingProduct.setProductThumb(productUpdateRequestDTO.getProductThumb());

        List<UUID> productVariantIds = productUpdateRequestDTO.getProductVariants();
        List<ProductVariant> productVariants = productVariantRepository.findAllById(productVariantIds);
        existingProduct.setProductVariants(productVariants);

        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(UUID id) {
        productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + id));

        productRepository.deleteById(id);
    }
}
