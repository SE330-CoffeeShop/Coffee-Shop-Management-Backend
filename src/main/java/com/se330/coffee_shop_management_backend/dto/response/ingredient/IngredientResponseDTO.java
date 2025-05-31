package com.se330.coffee_shop_management_backend.dto.response.ingredient;

import com.se330.coffee_shop_management_backend.dto.response.AbstractBaseResponse;
import com.se330.coffee_shop_management_backend.entity.Ingredient;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
public class IngredientResponseDTO extends AbstractBaseResponse {

    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;

    @Schema(
            name = "createdAt",
            description = "Date time field of employee creation",
            type = "LocalDateTime",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime createdAt;

    @Schema(
            name = "updatedAt",
            type = "LocalDateTime",
            description = "Date time field of employee update",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime updatedAt;

    private String ingredientName;
    private String ingredientDescription;
    private BigDecimal ingredientPrice;
    private String ingredientType;
    private long shelfLifeDays;
    private List<String> transferDetailIds;
    private List<String> inventoryIds;
    private List<String> recipeIds;
    private List<String> invoiceDetailIds;
    private List<String> stockIds;

    public static IngredientResponseDTO convert(Ingredient ingredient) {
        return IngredientResponseDTO.builder()
                .id(ingredient.getId().toString())
                .createdAt(ingredient.getCreatedAt())
                .updatedAt(ingredient.getUpdatedAt())
                .ingredientName(ingredient.getIngredientName())
                .ingredientDescription(ingredient.getIngredientDescription())
                .ingredientPrice(ingredient.getIngredientPrice())
                .ingredientType(ingredient.getIngredientType())
                .shelfLifeDays(ingredient.getShelfLifeDays())
                .transferDetailIds(ingredient.getTransferDetails() != null ? ingredient.getTransferDetails().stream()
                        .map(entity -> entity.getId().toString())
                        .toList() : List.of())
                .inventoryIds(ingredient.getInventories() != null ? ingredient.getInventories().stream()
                        .map(inventory -> inventory.getId().toString())
                        .collect(Collectors.toList()) : List.of())
                .recipeIds(ingredient.getRecipes() != null ? ingredient.getRecipes().stream()
                        .map(recipe -> recipe.getId().toString())
                        .collect(Collectors.toList()) : List.of())
                .invoiceDetailIds(ingredient.getInvoiceDetails() != null ? ingredient.getInvoiceDetails().stream()
                        .map(invoiceDetail -> invoiceDetail.getId().toString())
                        .collect(Collectors.toList()) : List.of())
                .stockIds(ingredient.getStocks() != null ? ingredient.getStocks().stream()
                        .map(stock -> stock.getId().toString())
                        .collect(Collectors.toList()) : List.of())
                .build();
    }

    public static List<IngredientResponseDTO> convert(List<Ingredient> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            return List.of();
        }

        return ingredients.stream()
                .map(IngredientResponseDTO::convert)
                .collect(Collectors.toList());
    }
}
