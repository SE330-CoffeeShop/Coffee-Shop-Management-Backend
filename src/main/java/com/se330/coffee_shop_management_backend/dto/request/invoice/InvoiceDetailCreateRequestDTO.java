package com.se330.coffee_shop_management_backend.dto.request.invoice;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class InvoiceDetailCreateRequestDTO {
    private int invoiceDetailQuantity;
    private int invoiceDetailUnit;
    private UUID ingredientId;
    private UUID invoiceId;
}
