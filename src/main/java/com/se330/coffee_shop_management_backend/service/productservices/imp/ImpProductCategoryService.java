package com.se330.coffee_shop_management_backend.service.productservices.imp;

import com.se330.coffee_shop_management_backend.entity.product.ProductCategory;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductCategoryRepository;
import com.se330.coffee_shop_management_backend.service.productservices.IProductCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public List<ProductCategory> findAllProductCategories() {
        return productCategoryRepository.findAll();
    }

    @Override
    public ProductCategory createProductCategory(ProductCategory productCategory) {
        return productCategoryRepository.save(productCategory);
    }

    @Override
    public ProductCategory updateProductCategory(ProductCategory productCategory) {
        return productCategoryRepository.save(productCategory);
    }

    @Override
    public void deleteProductCategory(UUID id) {
        productCategoryRepository.deleteById(id);
    }
}
