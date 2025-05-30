package com.se330.coffee_shop_management_backend.service.productservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.product.ProductCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.product.ProductUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.response.product.BestSellingProductResponseDTO;
import com.se330.coffee_shop_management_backend.entity.product.Product;
import com.se330.coffee_shop_management_backend.entity.product.ProductCategory;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductCategoryRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductRepository;
import com.se330.coffee_shop_management_backend.service.productservices.IProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;
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

    public ImpProductService(
            ProductRepository productRepository,
            ProductCategoryRepository productCategoryRepository
    ) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
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

    @Transactional
    @Override
    public Product updateProduct(ProductUpdateRequestDTO productUpdateRequestDTO) {
        Product existingProduct = productRepository.findById(productUpdateRequestDTO.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productUpdateRequestDTO.getProductId()));

        ProductCategory category = productCategoryRepository.findById(productUpdateRequestDTO.getProductCategory())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + productUpdateRequestDTO.getProductCategory()));

        if (existingProduct.getProductCategory() != null) {
            existingProduct.getProductCategory().getProducts().remove(existingProduct);
            existingProduct.setProductCategory(category);
            category.getProducts().add(existingProduct);
        }

        existingProduct.setProductDescription(productUpdateRequestDTO.getProductDescription());
        existingProduct.setProductPrice(productUpdateRequestDTO.getProductPrice());
        existingProduct.setProductName(productUpdateRequestDTO.getProductName());
        existingProduct.setProductIsDeleted(productUpdateRequestDTO.getProductIsDeleted());
        existingProduct.setProductSlug(productUpdateRequestDTO.getProductSlug());
        existingProduct.setProductIsPublished(productUpdateRequestDTO.getProductIsPublished());
        existingProduct.setProductRatingsAverage(productUpdateRequestDTO.getProductRatingsAverage());
        existingProduct.setProductThumb(productUpdateRequestDTO.getProductThumb());

        return productRepository.save(existingProduct);
    }

    @Transactional
    @Override
    public void deleteProduct(UUID id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + id));

        if (existingProduct.getProductCategory() != null) {
            existingProduct.getProductCategory().getProducts().remove(existingProduct);
            existingProduct.setProductCategory(null);
        }

        productRepository.deleteById(id);
    }

    @Override
    public Page<BestSellingProductResponseDTO> findAllBestSellingProducts(Pageable pageable) {
        Page<Object[]> productData = productRepository.findAllBestSellingProducts(pageable);
        List<BestSellingProductResponseDTO> bestSellingProducts = BestSellingProductResponseDTO.convert(productData.getContent());
        return new PageImpl<>(bestSellingProducts, pageable, productData.getTotalElements());
    }

    @Override
    public Page<BestSellingProductResponseDTO> findBestSellingProductsByYear(int year, Pageable pageable) {
        Page<Object[]> productData = productRepository.findBestSellingProductsByYear(year, pageable);
        List<BestSellingProductResponseDTO> bestSellingProducts = BestSellingProductResponseDTO.convert(productData.getContent());
        return new PageImpl<>(bestSellingProducts, pageable, productData.getTotalElements());
    }

    @Override
    public Page<BestSellingProductResponseDTO> findBestSellingProductsByMonthAndYear(int month, int year, Pageable pageable) {
        Page<Object[]> productData = productRepository.findBestSellingProductsByMonthAndYear(month, year, pageable);
        List<BestSellingProductResponseDTO> bestSellingProducts = BestSellingProductResponseDTO.convert(productData.getContent());
        return new PageImpl<>(bestSellingProducts, pageable, productData.getTotalElements());
    }

    @Override
    public Page<BestSellingProductResponseDTO> findBestSellingProductsByDayAndMonthAndYear(int day, int month, int year, Pageable pageable) {
        Page<Object[]> productData = productRepository.findBestSellingProductsByDayAndMonthAndYear(day, month, year, pageable);
        List<BestSellingProductResponseDTO> bestSellingProducts = BestSellingProductResponseDTO.convert(productData.getContent());
        return new PageImpl<>(bestSellingProducts, pageable, productData.getTotalElements());
    }

    @Override
    public Page<BestSellingProductResponseDTO> findBestSellingProductsByBranch(UUID branchId, Pageable pageable) {
        Page<Object[]> productData = productRepository.findBestSellingProductsByBranch(branchId, pageable);
        List<BestSellingProductResponseDTO> bestSellingProducts = BestSellingProductResponseDTO.convert(productData.getContent());
        return new PageImpl<>(bestSellingProducts, pageable, productData.getTotalElements());
    }

    @Override
    public Page<BestSellingProductResponseDTO> findBestSellingProductsByBranchAndYear(UUID branchId, int year, Pageable pageable) {
        Page<Object[]> productData = productRepository.findBestSellingProductsByBranchAndYear(branchId, year, pageable);
        List<BestSellingProductResponseDTO> bestSellingProducts = BestSellingProductResponseDTO.convert(productData.getContent());
        return new PageImpl<>(bestSellingProducts, pageable, productData.getTotalElements());
    }

    @Override
    public Page<BestSellingProductResponseDTO> findBestSellingProductsByBranchAndMonthAndYear(UUID branchId, int month, int year, Pageable pageable) {
        Page<Object[]> productData = productRepository.findBestSellingProductsByBranchAndMonthAndYear(branchId, month, year, pageable);
        List<BestSellingProductResponseDTO> bestSellingProducts = BestSellingProductResponseDTO.convert(productData.getContent());
        return new PageImpl<>(bestSellingProducts, pageable, productData.getTotalElements());
    }

    @Override
    public Page<BestSellingProductResponseDTO> findBestSellingProductsByBranchAndDayAndMonthAndYear(UUID branchId, int day, int month, int year, Pageable pageable) {
        Page<Object[]> productData = productRepository.findBestSellingProductsByBranchAndDayAndMonthAndYear(branchId, day, month, year, pageable);
        List<BestSellingProductResponseDTO> bestSellingProducts = BestSellingProductResponseDTO.convert(productData.getContent());
        return new PageImpl<>(bestSellingProducts, pageable, productData.getTotalElements());
    }
}
