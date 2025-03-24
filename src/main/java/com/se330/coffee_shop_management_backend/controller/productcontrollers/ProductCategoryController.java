package com.se330.coffee_shop_management_backend.controller.productcontrollers;

import com.se330.coffee_shop_management_backend.dto.response.ErrorResponse;
import com.se330.coffee_shop_management_backend.dto.response.PageResponse;
import com.se330.coffee_shop_management_backend.dto.response.product.ProductCategoryResponseDTO;
import com.se330.coffee_shop_management_backend.entity.product.ProductCategory;
import com.se330.coffee_shop_management_backend.service.productservices.IProductCategoryService;
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
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.se330.coffee_shop_management_backend.util.Constants.SECURITY_SCHEME_NAME;
import static com.se330.coffee_shop_management_backend.util.CreatePageHelper.createPageable;

@RestController
@RequestMapping("/product/category")
public class ProductCategoryController {

    private final IProductCategoryService productCategoryService;

    public ProductCategoryController(IProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get product category detail",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success operation",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ProductCategoryResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Full authentication is required to access this resource",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Validation Failed",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<ProductCategoryResponseDTO> findByIdProductCategory(@PathVariable UUID id) {
        return ResponseEntity.ok(ProductCategoryResponseDTO.convert(productCategoryService.findByIdProductCategory(id)));
    }

    @GetMapping("/all")
    @Operation(
            summary = "Get all product categories with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved category list",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public ResponseEntity<PageResponse<ProductCategoryResponseDTO>> findAllProductCategories(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "vi") String lan,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);

        Page<ProductCategory> categoryPage = productCategoryService.findAllProductCategories(pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        ProductCategoryResponseDTO.convert(categoryPage.getContent()),
                        categoryPage.getTotalElements(),
                        categoryPage.getNumber(),
                        categoryPage.getSize()
                )
        );
    }

    @PostMapping("/")
    @Operation(
            summary = "Create new product category",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Category created successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ProductCategoryResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Category already exists",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public ResponseEntity<ProductCategoryResponseDTO> createProductCategory(@RequestBody ProductCategory product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductCategoryResponseDTO.convert(productCategoryService.createProductCategory(product)));
    }

    @PatchMapping("/")
    @Operation(
            summary = "Update product category",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Category updated successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ProductCategoryResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Category not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public ResponseEntity<ProductCategoryResponseDTO> updateProductCategory(@RequestBody ProductCategory product) {
        return ResponseEntity.ok(ProductCategoryResponseDTO.convert(productCategoryService.updateProductCategory(product)));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete product category",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Category deleted successfully"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Category not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        productCategoryService.deleteProductCategory(id);
        return ResponseEntity.noContent().build();
    }
}