package com.se330.coffee_shop_management_backend.dto.request.invoice;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
public class InvoiceUpdateRequestDTO {
    private UUID id;
    private String invoiceDescription;
    private String invoiceTrackingNumber;
    private BigDecimal invoiceTransferTotalCost;
    private UUID supplierId;
    private UUID warehouseId;
}
