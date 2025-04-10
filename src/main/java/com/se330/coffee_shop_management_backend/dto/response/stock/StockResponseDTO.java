package com.se330.coffee_shop_management_backend.dto.response.stock;

import com.se330.coffee_shop_management_backend.dto.response.AbstractBaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

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
    private String ingredient;
    private String warehouse;
    private String supplier;
}
