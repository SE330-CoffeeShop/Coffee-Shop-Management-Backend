package com.se330.coffee_shop_management_backend.controller;

import com.se330.coffee_shop_management_backend.dto.request.order.OrderCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.order.OrderUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.response.ErrorResponse;
import com.se330.coffee_shop_management_backend.dto.response.PageResponse;
import com.se330.coffee_shop_management_backend.dto.response.order.OrderResponseDTO;
import com.se330.coffee_shop_management_backend.entity.Order;
import com.se330.coffee_shop_management_backend.service.orderservices.IOrderService;
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
@RequestMapping("/orders")
public class OrderController {

    private final IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get order detail",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved order",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = OrderResponseDTO.class)
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
                            description = "Order not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<OrderResponseDTO> findByIdOrder(@PathVariable UUID id) {
        return ResponseEntity.ok(OrderResponseDTO.convert(orderService.findByIdOrder(id)));
    }

    @GetMapping("/all")
    @Operation(
            summary = "Get all orders with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved order list",
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
    public ResponseEntity<PageResponse<OrderResponseDTO>> findAllOrders(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "vi") String lan,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<Order> orderPages = orderService.findAllOrders(pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        OrderResponseDTO.convert(orderPages.getContent()),
                        orderPages.getTotalElements(),
                        orderPages.getNumber(),
                        orderPages.getSize()
                )
        );
    }

    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'EMPLOYEE')")
    @Operation(
            summary = "Create new order",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Order created successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = OrderResponseDTO.class)
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
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderCreateRequestDTO orderCreateRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                OrderResponseDTO.convert(
                        orderService.createOrder(orderCreateRequestDTO)
                )
        );
    }

    @PatchMapping("/")
    @Operation(
            summary = "Update order",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Order updated successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = OrderResponseDTO.class)
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
                            description = "Order not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<OrderResponseDTO> updateOrder(@RequestBody OrderUpdateRequestDTO orderUpdateRequestDTO) {
        return ResponseEntity.ok(
                OrderResponseDTO.convert(
                        orderService.updateOrder(orderUpdateRequestDTO)
                )
        );
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete order",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Order deleted successfully"
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
                            description = "Order not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}