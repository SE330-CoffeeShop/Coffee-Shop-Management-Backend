package com.se330.coffee_shop_management_backend.dto.response.order;

import com.se330.coffee_shop_management_backend.dto.response.AbstractBaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
public class PreConfirmOrderResponseDTO extends AbstractBaseResponse {
    List<AvailableDiscountForOrder> availableDiscounts;
    BigDecimal totalPriceBeforeDiscounts;
    BigDecimal discountAmount;
    BigDecimal totalPriceAfterDiscounts;
}
