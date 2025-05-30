package com.se330.coffee_shop_management_backend.controller;

import com.se330.coffee_shop_management_backend.dto.request.stock.StockCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.stock.StockUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.response.ErrorResponse;
import com.se330.coffee_shop_management_backend.dto.response.PageResponse;
import com.se330.coffee_shop_management_backend.dto.response.stock.StockResponseDTO;
import com.se330.coffee_shop_management_backend.entity.Stock;
import com.se330.coffee_shop_management_backend.service.stockservices.IStockService;
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
@RequestMapping("/stock")
public class StockController {

    private final IStockService stockService;

    public StockController(IStockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get stock detail",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved stock",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = StockResponseDTO.class)
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
                            description = "Stock not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<StockResponseDTO> findByIdStock(@PathVariable UUID id) {
        return ResponseEntity.ok(StockResponseDTO.convert(stockService.findByIdStock(id)));
    }

    @GetMapping("/all")
    @Operation(
            summary = "Get all stocks with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved stock list",
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
    public ResponseEntity<PageResponse<StockResponseDTO>> findAllStocks(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "vi") String lan,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<Stock> stockPage = stockService.findAllStocks(pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        StockResponseDTO.convert(stockPage.getContent()),
                        stockPage.getTotalElements(),
                        stockPage.getNumber(),
                        stockPage.getSize()
                )
        );
    }

    @PostMapping("/")
    @Operation(
            summary = "Create new stock",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Stock created successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = StockResponseDTO.class)
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
    public ResponseEntity<StockResponseDTO> createStock(@RequestBody StockCreateRequestDTO stockCreateRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                StockResponseDTO.convert(
                        stockService.createStock(stockCreateRequestDTO)
                )
        );
    }

    @PatchMapping("/")
    @Operation(
            summary = "Update stock",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Stock updated successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = StockResponseDTO.class)
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
                            description = "Stock not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<StockResponseDTO> updateStock(@RequestBody StockUpdateRequestDTO stockUpdateRequestDTO) {
        return ResponseEntity.ok(
                StockResponseDTO.convert(
                        stockService.updateStock(stockUpdateRequestDTO)
                )
        );
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete stock",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Stock deleted successfully"
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
                            description = "Stock not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<Void> deleteStock(@PathVariable UUID id) {
        stockService.deleteStock(id);
        return ResponseEntity.noContent().build();
    }
}