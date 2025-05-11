package com.se330.coffee_shop_management_backend.dto.response.stock;

import com.se330.coffee_shop_management_backend.dto.response.AbstractBaseResponse;
import com.se330.coffee_shop_management_backend.entity.Stock;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
public class StockResponseDTO extends AbstractBaseResponse {
    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;

    @Schema(
            name = "createdAt",
            description = "Date time field of shift creation",
            type = "LocalDateTime",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime createdAt;

    @Schema(
            name = "updatedAt",
            type = "LocalDateTime",
            description = "Date time field of shift update",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime updatedAt;

    private int stockQuantity;
    private int stockUnit;
    private String ingredientId;
    private String warehouseId;
    private String supplierId;

    public static StockResponseDTO convert(Stock stock) {
        return StockResponseDTO.builder()
                .id(stock.getId().toString())
                .createdAt(stock.getCreatedAt())
                .updatedAt(stock.getUpdatedAt())
                .stockQuantity(stock.getStockQuantity())
                .stockUnit(stock.getStockUnit())
                .ingredientId(stock.getIngredient() != null ? stock.getIngredient().getId().toString() : null)
                .warehouseId(stock.getWarehouse() != null ? stock.getWarehouse().getId().toString() : null)
                .supplierId(stock.getSupplier() != null ? stock.getSupplier().getId().toString() : null)
                .build();
    }

    public static List<StockResponseDTO> convert(List<Stock> stocks) {
        if(stocks == null || stocks.isEmpty()) {
            return List.of();
        }

        return stocks.stream()
                .map(StockResponseDTO::convert)
                .toList();
    }
}
