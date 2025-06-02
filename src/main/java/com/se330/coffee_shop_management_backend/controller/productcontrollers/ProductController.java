package com.se330.coffee_shop_management_backend.controller.productcontrollers;

import com.se330.coffee_shop_management_backend.dto.request.product.ProductCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.product.ProductUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.response.ErrorResponse;
import com.se330.coffee_shop_management_backend.dto.response.PageResponse;
import com.se330.coffee_shop_management_backend.dto.response.SingleResponse;
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
    @PreAuthorize("hasAnyAuthority('MANAGER', 'EMPLOYEE')")
    @Operation(
            summary = "Get product detail",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved product",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
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
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<ProductResponseDTO>> findById(@PathVariable UUID id) {
        ProductResponseDTO product = ProductResponseDTO.convert(productService.findByIdProduct(id));
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "Product retrieved successfully",
                        product
                )
        );
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'EMPLOYEE')")
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
                    )
            }
    )
    public ResponseEntity<PageResponse<ProductResponseDTO>> findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<Product> productPage = productService.findAllProducts(pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Products retrieved successfully",
                        ProductResponseDTO.convert(productPage.getContent()),
                        new PageResponse.PagingResponse(
                                productPage.getNumber(),
                                productPage.getSize(),
                                productPage.getTotalElements(),
                                productPage.getTotalPages()
                        )
                )
        );
    }

    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'EMPLOYEE')")
    @Operation(
            summary = "Create new product",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Product created successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<ProductResponseDTO>> create(@RequestBody ProductCreateRequestDTO dto) {
        ProductResponseDTO product = ProductResponseDTO.convert(productService.createProduct(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new SingleResponse<>(
                        HttpStatus.CREATED.value(),
                        "Product created successfully",
                        product
                )
        );
    }

    @PatchMapping("/")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'EMPLOYEE')")
    @Operation(
            summary = "Update product",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product updated successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
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
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<ProductResponseDTO>> update(@RequestBody ProductUpdateRequestDTO dto) {
        ProductResponseDTO product = ProductResponseDTO.convert(productService.updateProduct(dto));
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "Product updated successfully",
                        product
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'EMPLOYEE')")
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
                            description = "Product not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}