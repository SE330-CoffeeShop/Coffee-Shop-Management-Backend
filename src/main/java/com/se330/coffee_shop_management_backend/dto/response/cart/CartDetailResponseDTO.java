package com.se330.coffee_shop_management_backend.dto.response.cart;

import com.se330.coffee_shop_management_backend.entity.CartDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class CartDetailResponseDTO {
    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;

    private String cartId;
    private String variantId;
    private int cartDetailQuantity;
    private BigDecimal cartDetailUnitPrice;
    private BigDecimal cartDetailDiscountCost;
    private BigDecimal cartDetailUnitPriceAfterDiscount;

    public static CartDetailResponseDTO convert(CartDetail cartDetail) {
        return CartDetailResponseDTO.builder()
                .id(cartDetail.getId().toString())
                .cartId(cartDetail.getCart().getId().toString())
                .variantId(cartDetail.getProductVariant().getId().toString())
                .cartDetailQuantity(cartDetail.getCartDetailQuantity())
                .cartDetailUnitPrice(cartDetail.getCartDetailUnitPrice())
                .cartDetailDiscountCost(cartDetail.getCartDetailDiscountCost())
                .cartDetailUnitPriceAfterDiscount(cartDetail.getCartDetailUnitPriceAfterDiscount())
                .build();
    }

    public static List<CartDetailResponseDTO> convert(List<CartDetail> cartDetails) {
        if (cartDetails == null || cartDetails.isEmpty()) {
            return List.of();
        }

        return cartDetails.stream()
                .map(CartDetailResponseDTO::convert)
                .toList();
    }
}
