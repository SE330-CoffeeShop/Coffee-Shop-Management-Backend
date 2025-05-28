package com.se330.coffee_shop_management_backend.controller.productcontrollers;

import com.se330.coffee_shop_management_backend.dto.request.product.ProductCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.product.ProductUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.response.ErrorResponse;
import com.se330.coffee_shop_management_backend.dto.response.PageResponse;
import com.se330.coffee_shop_management_backend.dto.response.product.BestSellingProductResponseDTO;
import com.se330.coffee_shop_management_backend.dto.response.product.ProductResponseDTO;
import com.se330.coffee_shop_management_backend.entity.product.Product;
import com.se330.coffee_shop_management_backend.service.productservices.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.se330.coffee_shop_management_backend.util.Constants.SECURITY_SCHEME_NAME;
import static com.se330.coffee_shop_management_backend.util.CreatePageHelper.createPageable;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get product detail",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved product",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ProductResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid ID format",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<ProductResponseDTO> findByIdProduct(@PathVariable UUID id) {
        return ResponseEntity.ok(ProductResponseDTO.convert(productService.findByIdProduct(id)));
    }

    @GetMapping("/all")
    @Operation(
            summary = "Get all products with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved product list",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<ProductResponseDTO>> findAllProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "vi") String lan,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<Product> productPages = productService.findAllProducts(pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        ProductResponseDTO.convert(productPages.getContent()),
                        productPages.getTotalElements(),
                        productPages.getNumber(),
                        productPages.getSize()
                )
        );
    }

    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(
            summary = "Create new product",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Product created successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ProductResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Product already exists",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductCreateRequestDTO productRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductResponseDTO.convert(productService.createProduct(productRequestDTO)));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(
            summary = "Update product",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product updated successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ProductResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<ProductResponseDTO> updateProduct(@RequestBody ProductUpdateRequestDTO productRequestDTO) {
        return ResponseEntity.ok(ProductResponseDTO.convert(productService.updateProduct(productRequestDTO)));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete product",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Product deleted successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/best-selling")
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @Operation(
            summary = "Get all best-selling products",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved best-selling products",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied - Requires MANAGER role",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<BestSellingProductResponseDTO>> findAllBestSellingProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "totalQuantity") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<BestSellingProductResponseDTO> productsPage = productService.findAllBestSellingProducts(pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        productsPage.getContent(),
                        productsPage.getTotalElements(),
                        productsPage.getNumber(),
                        productsPage.getSize()
                )
        );
    }

    @GetMapping("/best-selling/year")
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @Operation(
            summary = "Get best-selling products by year",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    public ResponseEntity<PageResponse<BestSellingProductResponseDTO>> findBestSellingProductsByYear(
            @RequestParam int year,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "totalQuantity") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<BestSellingProductResponseDTO> productsPage = productService.findBestSellingProductsByYear(year, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        productsPage.getContent(),
                        productsPage.getTotalElements(),
                        productsPage.getNumber(),
                        productsPage.getSize()
                )
        );
    }

    @GetMapping("/best-selling/month-year")
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @Operation(
            summary = "Get best-selling products by month and year",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    public ResponseEntity<PageResponse<BestSellingProductResponseDTO>> findBestSellingProductsByMonthAndYear(
            @RequestParam int month,
            @RequestParam int year,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "totalQuantity") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<BestSellingProductResponseDTO> productsPage = productService.findBestSellingProductsByMonthAndYear(month, year, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        productsPage.getContent(),
                        productsPage.getTotalElements(),
                        productsPage.getNumber(),
                        productsPage.getSize()
                )
        );
    }

    @GetMapping("/best-selling/day-month-year")
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @Operation(
            summary = "Get best-selling products by day, month and year",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    public ResponseEntity<PageResponse<BestSellingProductResponseDTO>> findBestSellingProductsByDayAndMonthAndYear(
            @RequestParam int day,
            @RequestParam int month,
            @RequestParam int year,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "totalQuantity") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<BestSellingProductResponseDTO> productsPage = productService.findBestSellingProductsByDayAndMonthAndYear(day, month, year, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        productsPage.getContent(),
                        productsPage.getTotalElements(),
                        productsPage.getNumber(),
                        productsPage.getSize()
                )
        );
    }

    @GetMapping("/best-selling/branch")
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @Operation(
            summary = "Get best-selling products by branch",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    public ResponseEntity<PageResponse<BestSellingProductResponseDTO>> findBestSellingProductsByBranch(
            @RequestParam UUID branchId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "totalQuantity") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<BestSellingProductResponseDTO> productsPage = productService.findBestSellingProductsByBranch(branchId, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        productsPage.getContent(),
                        productsPage.getTotalElements(),
                        productsPage.getNumber(),
                        productsPage.getSize()
                )
        );
    }

    @GetMapping("/best-selling/branch-year")
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @Operation(
            summary = "Get best-selling products by branch and year",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    public ResponseEntity<PageResponse<BestSellingProductResponseDTO>> findBestSellingProductsByBranchAndYear(
            @RequestParam UUID branchId,
            @RequestParam int year,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "productSoldCount") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<BestSellingProductResponseDTO> productsPage = productService.findBestSellingProductsByBranchAndYear(branchId, year, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        productsPage.getContent(),
                        productsPage.getTotalElements(),
                        productsPage.getNumber(),
                        productsPage.getSize()
                )
        );
    }

    @GetMapping("/best-selling/branch-month-year")
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @Operation(
            summary = "Get best-selling products by branch, month and year",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    public ResponseEntity<PageResponse<BestSellingProductResponseDTO>> findBestSellingProductsByBranchAndMonthAndYear(
            @RequestParam UUID branchId,
            @RequestParam int month,
            @RequestParam int year,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "totalQuantity") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<BestSellingProductResponseDTO> productsPage = productService.findBestSellingProductsByBranchAndMonthAndYear(branchId, month, year, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        productsPage.getContent(),
                        productsPage.getTotalElements(),
                        productsPage.getNumber(),
                        productsPage.getSize()
                )
        );
    }

    @GetMapping("/best-selling/branch-day-month-year")
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @Operation(
            summary = "Get best-selling products by branch, day, month and year",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    public ResponseEntity<PageResponse<BestSellingProductResponseDTO>> findBestSellingProductsByBranchAndDayAndMonthAndYear(
            @RequestParam UUID branchId,
            @RequestParam int day,
            @RequestParam int month,
            @RequestParam int year,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "totalQuantity") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<BestSellingProductResponseDTO> productsPage = productService.findBestSellingProductsByBranchAndDayAndMonthAndYear(branchId, day, month, year, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        productsPage.getContent(),
                        productsPage.getTotalElements(),
                        productsPage.getNumber(),
                        productsPage.getSize()
                )
        );
    }
}
