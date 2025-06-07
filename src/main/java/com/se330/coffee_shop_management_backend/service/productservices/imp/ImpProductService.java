package com.se330.coffee_shop_management_backend.service.productservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.product.ProductCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.product.ProductUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.response.product.BestSellingProductResponseDTO;
import com.se330.coffee_shop_management_backend.entity.product.Product;
import com.se330.coffee_shop_management_backend.entity.product.ProductCategory;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductCategoryRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductRepository;
import com.se330.coffee_shop_management_backend.service.CloudinaryService;
import com.se330.coffee_shop_management_backend.service.notificationservices.INotificationService;
import com.se330.coffee_shop_management_backend.service.productservices.IProductService;
import com.se330.coffee_shop_management_backend.util.Constants;
import com.se330.coffee_shop_management_backend.util.CreateNotiContentHelper;
import com.se330.coffee_shop_management_backend.util.CreateSlug;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class ImpProductService implements IProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final CloudinaryService cloudinaryService;
    private final INotificationService notificationService;

    public ImpProductService(
            ProductRepository productRepository,
            ProductCategoryRepository productCategoryRepository,
            CloudinaryService cloudinaryService,
            INotificationService notificationService
    ) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.cloudinaryService = cloudinaryService;
        this.notificationService = notificationService;
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
    @Transactional
    public Product createProduct(ProductCreateRequestDTO productCreateRequestDTO) {
        ProductCategory category = productCategoryRepository.findById(productCreateRequestDTO.getProductCategory())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + productCreateRequestDTO.getProductCategory()));

        Product newProduct = productRepository.save(
                Product.builder()
                        .productCategory(category)
                        .productDescription(productCreateRequestDTO.getProductDescription())
                        .productPrice(productCreateRequestDTO.getProductPrice())
                        .productName(productCreateRequestDTO.getProductName())
                        .productThumb(cloudinaryService.getProductDefault())
                        .productIsDeleted(false)
                        .productIsPublished(false)
                        .productSlug(CreateSlug.createSlug(productCreateRequestDTO.getProductName()))
                        .productCommentCount(0)
                        .productRatingsAverage(BigDecimal.valueOf(0))
                        .build()
        );

        notificationService.sendNotificationToAllUsers(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.PRODUCT)
                        .notificationContent(CreateNotiContentHelper.createProductAddedContentAll(newProduct.getProductName()))
                        .senderId(null)
                        .receiverId(null)
                        .isRead(false)
                        .build()
        );

        return newProduct;
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
        existingProduct.setProductSlug(CreateSlug.createSlug(productUpdateRequestDTO.getProductName()));
        existingProduct.setProductIsPublished(productUpdateRequestDTO.getProductIsPublished());
        existingProduct.setProductRatingsAverage(productUpdateRequestDTO.getProductRatingsAverage());

        productRepository.save(existingProduct);

        notificationService.sendNotificationToAllUsers(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.PRODUCT)
                        .notificationContent(CreateNotiContentHelper.createProductUpdatedContentAll(existingProduct.getProductName()))
                        .senderId(null)
                        .receiverId(null)
                        .isRead(false)
                        .build()
        );

        return existingProduct;
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

        notificationService.sendNotificationToAllUsers(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.PRODUCT)
                        .notificationContent(CreateNotiContentHelper.createProductDeletedContentAll(existingProduct.getProductName()))
                        .senderId(null)
                        .receiverId(null)
                        .isRead(false)
                        .build()
        );

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

    @Override
    public String uploadProductImage(UUID productId, MultipartFile file) throws Exception {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));

        // Check if the file is empty
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }

        // Check if the current file is the default image
        if (existingProduct.getProductThumb().equals(cloudinaryService.getProductDefault())) {
            // Upload the new image
            Map uploadResult = cloudinaryService.uploadFile(file, "product");
            String imageUrl = uploadResult.get("url").toString();
            // Update the product's image URL
            existingProduct.setProductThumb(imageUrl);
            productRepository.save(existingProduct);

            return imageUrl;
        } else {
            // Delete the old image from Cloudinary
            try {
                cloudinaryService.deleteFile(existingProduct.getProductThumb());
            } catch (IOException e) {
                log.error("Failed to delete old product image: {}", e.getMessage());
                // Continue with upload even if delete fails
            }

            // Upload the new image
            Map uploadResult = cloudinaryService.uploadFile(file, "product");
            String imageUrl = uploadResult.get("url").toString();

            // Update the product's image URL
            existingProduct.setProductThumb(imageUrl);
            productRepository.save(existingProduct);

            return imageUrl;
        }
    }

    @Override
    public String deleteProductImage(UUID productId) throws Exception {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));

        String defaultImageUrl = cloudinaryService.getProductDefault();

        if (existingProduct.getProductThumb().equals(defaultImageUrl)) {
            return defaultImageUrl;
        }

        try {
            cloudinaryService.deleteFile(existingProduct.getProductThumb());
        } catch (IOException e) {
            log.error("Failed to delete product image: {}", e.getMessage());
        }

        // Set the product's image back to the default
        existingProduct.setProductThumb(defaultImageUrl);
        productRepository.save(existingProduct);

        return defaultImageUrl;
    }
}
