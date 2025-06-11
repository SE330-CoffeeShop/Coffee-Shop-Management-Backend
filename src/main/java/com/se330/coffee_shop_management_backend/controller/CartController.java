package com.se330.coffee_shop_management_backend.controller;

import com.se330.coffee_shop_management_backend.dto.request.cart.CartDetailCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.response.ErrorResponse;
import com.se330.coffee_shop_management_backend.dto.response.PageResponse;
import com.se330.coffee_shop_management_backend.dto.response.SingleResponse;
import com.se330.coffee_shop_management_backend.dto.response.cart.CartDetailResponseDTO;
import com.se330.coffee_shop_management_backend.dto.response.cart.CartResponseDTO;
import com.se330.coffee_shop_management_backend.entity.Cart;
import com.se330.coffee_shop_management_backend.entity.CartDetail;
import com.se330.coffee_shop_management_backend.service.cartservices.ICartService;
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
@RequestMapping("/cart")
public class CartController {
    private final ICartService cartService;

    public CartController(ICartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(
            summary = "Get all carts with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved cart list",
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
    public ResponseEntity<PageResponse<CartResponseDTO>> getAllCarts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<Cart> cartPages = cartService.getAllCarts(pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Carts retrieved successfully",
                        CartResponseDTO.convert(cartPages.getContent()),
                        new PageResponse.PagingResponse(
                                cartPages.getNumber(),
                                cartPages.getSize(),
                                cartPages.getTotalElements(),
                                cartPages.getTotalPages()
                        )
                )
        );
    }

    @GetMapping("/{userId}")
    @Operation(
            summary = "Get cart by user ID",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved cart",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid user ID format",
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
    public ResponseEntity<SingleResponse<CartResponseDTO>> getCartByUserId(@PathVariable UUID userId) {
        CartResponseDTO cart = CartResponseDTO.convert(cartService.getCartByUserId(userId));
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "Cart retrieved successfully",
                        cart
                )
        );
    }

    @GetMapping("/branches/{userId}")
    @Operation(
            summary = "Find branches with sufficient inventory for a user's cart",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved branches",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Cart is empty",
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
    public ResponseEntity<PageResponse<UUID>> findBranchesWithSufficientInventory(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, "desc", "id");
        Page<UUID> branchPages = cartService.findBranchesWithSufficientInventory(userId, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Branches retrieved successfully",
                        branchPages.getContent(),
                        new PageResponse.PagingResponse(
                                branchPages.getNumber(),
                                branchPages.getSize(),
                                branchPages.getTotalElements(),
                                branchPages.getTotalPages()
                        )
                )
        );
    }

    @PostMapping("/{userId}/detail")
    @Operation(
            summary = "Add a cart detail to user's cart",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Cart detail added successfully",
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
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<CartResponseDTO>> addCartDetail(
            @PathVariable UUID userId,
            @RequestBody CartDetailCreateRequestDTO cartDetailCreateRequestDTO
    ) {
        CartResponseDTO cart = CartResponseDTO.convert(cartService.addCartDetail(userId, cartDetailCreateRequestDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new SingleResponse<>(
                        HttpStatus.CREATED.value(),
                        "Cart detail added successfully",
                        cart
                )
        );
    }

    @PutMapping("/{userId}/detail")
    @Operation(
            summary = "Update a cart detail in user's cart",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cart detail updated successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data or cart detail not found",
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
    public ResponseEntity<SingleResponse<CartResponseDTO>> updateCartDetail(
            @PathVariable UUID userId,
            @RequestBody CartDetailCreateRequestDTO cartDetailCreateRequestDTO
    ) {
        CartResponseDTO cart = CartResponseDTO.convert(cartService.updateCartDetail(userId, cartDetailCreateRequestDTO));
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "Cart detail updated successfully",
                        cart
                )
        );
    }

    @DeleteMapping("/{userId}/clear")
    @Operation(
            summary = "Clear user's cart",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cart cleared successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Cart not found",
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
    public ResponseEntity<SingleResponse<CartResponseDTO>> clearCart(@PathVariable UUID userId) {
        CartResponseDTO cart = CartResponseDTO.convert(cartService.clearCart(userId));
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "Cart cleared successfully",
                        cart
                )
        );
    }

    @GetMapping("/{userId}/details")
    @Operation(
            summary = "Get all cart details for a specific user with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved cart details",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid user ID format",
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
    public ResponseEntity<PageResponse<CartDetailResponseDTO>> getAllCartDetailsByUserId(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<CartDetail> cartDetailPages = cartService.getAllCartDetailsByUserId(userId, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Cart details retrieved successfully",
                        CartDetailResponseDTO.convert(cartDetailPages.getContent()),
                        new PageResponse.PagingResponse(
                                cartDetailPages.getNumber(),
                                cartDetailPages.getSize(),
                                cartDetailPages.getTotalElements(),
                                cartDetailPages.getTotalPages()
                        )
                )
        );
    }

    @DeleteMapping("/{userId}/detail/{cartDetailId}")
    @Operation(
            summary = "Remove a cart detail from user's cart",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cart detail removed successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data or cart detail not found",
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
    public ResponseEntity<SingleResponse<CartResponseDTO>> removeCartDetail(
            @PathVariable UUID userId,
            @PathVariable UUID cartDetailId
    ) {
        CartResponseDTO cart = CartResponseDTO.convert(cartService.removeCartDetail(userId, cartDetailId));
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "Cart detail removed successfully",
                        cart
                )
        );
    }
}