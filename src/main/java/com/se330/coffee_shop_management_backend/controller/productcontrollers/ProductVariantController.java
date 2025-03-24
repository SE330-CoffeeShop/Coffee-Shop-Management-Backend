package com.se330.coffee_shop_management_backend.controller.productcontrollers;

import com.se330.coffee_shop_management_backend.dto.request.product.ProductVariantRequestDTO;
import com.se330.coffee_shop_management_backend.dto.response.ErrorResponse;
import com.se330.coffee_shop_management_backend.dto.response.PageResponse;
import com.se330.coffee_shop_management_backend.dto.response.product.ProductVariantResponseDTO;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import com.se330.coffee_shop_management_backend.service.productservices.IProductVariantService;
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
@RequestMapping("/product/variant")
public class ProductVariantController {

    private final IProductVariantService productVariantService;

    public ProductVariantController(
            IProductVariantService productVariantService
    ) {
        this.productVariantService = productVariantService;
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get product variant detail",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved product variant",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ProductVariantResponseDTO.class)
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
                            description = "Product variant not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<ProductVariantResponseDTO> findByIdProductVariant(@PathVariable UUID id) {
        return ResponseEntity.ok(ProductVariantResponseDTO.convert(productVariantService.findByIdProductVariant(id)));
    }

    @GetMapping("/all")
    @Operation(
            summary = "Get all product variants with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved product variant list",
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
    public ResponseEntity<PageResponse<ProductVariantResponseDTO>> findAllProductVariants(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "vi") String lan,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);

        Page<ProductVariant> variantPage = productVariantService.findAllProductVariants(pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        ProductVariantResponseDTO.convert(variantPage.getContent()),
                        variantPage.getTotalElements(),
                        variantPage.getNumber(),
                        variantPage.getSize()
                )
        );
    }

    @PostMapping("/")
    @Operation(
            summary = "Create new product variant",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Product variant created successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ProductVariantResponseDTO.class)
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
                            responseCode = "409",
                            description = "Product variant already exists",
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
                    )
            }
    )
    public ResponseEntity<ProductVariantResponseDTO> createProductVariant(@RequestBody ProductVariantRequestDTO productVariantRequestDTO) {
        ProductVariantResponseDTO createdVariant = ProductVariantResponseDTO.convert(productVariantService.createProductVariant(productVariantRequestDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVariant);
    }

    @PatchMapping("/")
    @Operation(
            summary = "Update product variant",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product variant updated successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ProductVariantResponseDTO.class)
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
                            description = "Product variant not found",
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
                    )
            }
    )
    public ResponseEntity<ProductVariantResponseDTO> updateProductVariant(@RequestBody ProductVariantRequestDTO productVariantRequestDTO) {
        ProductVariantResponseDTO updatedVariant = ProductVariantResponseDTO.convert(
                productVariantService.updateProductVariant(productVariantRequestDTO));
        return ResponseEntity.ok(updatedVariant);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete product variant",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Product variant deleted successfully"
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
                            description = "Product variant not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<Void> deleteProductVariant(@PathVariable UUID id) {
        productVariantService.deleteProductVariant(id);
        return ResponseEntity.noContent().build();
    }
}
