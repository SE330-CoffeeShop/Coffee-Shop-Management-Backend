package com.se330.coffee_shop_management_backend.service.productservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.product.ProductCategoryUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.product.ProdutCategoryCreateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.product.ProductCategory;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductCategoryRepository;
import com.se330.coffee_shop_management_backend.service.productservices.IProductCategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ImpProductCategoryService implements IProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    public ImpProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @Override
    public ProductCategory findByIdProductCategory(UUID id) {
        return productCategoryRepository.findById(id).orElse(null);
    }

    @Override
    public Page<ProductCategory> findAllProductCategories(Pageable pageable) {
        return productCategoryRepository.findAll(pageable);
    }

    @Override
    public ProductCategory createProductCategory(ProdutCategoryCreateRequestDTO productCategoryRequestDTO) {
        return productCategoryRepository.save(
                ProductCategory.builder()
                        .categoryName(productCategoryRequestDTO.getCategoryName())
                        .categoryDescription(productCategoryRequestDTO.getCategoryDescription())
                        .build()
        );
    }

    @Override
    public ProductCategory updateProductCategory(ProductCategoryUpdateRequestDTO productCategoryRequestDTO) {
        ProductCategory existingCategory = productCategoryRepository.findById(productCategoryRequestDTO.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + productCategoryRequestDTO.getCategoryId()));

        existingCategory.setCategoryName(productCategoryRequestDTO.getCategoryName());
        existingCategory.setCategoryDescription(productCategoryRequestDTO.getCategoryDescription());

        return productCategoryRepository.save(existingCategory);
    }

    @Override
    public void deleteProductCategory(UUID id) {
        productCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + id));

        productCategoryRepository.deleteById(id);
    }
}
