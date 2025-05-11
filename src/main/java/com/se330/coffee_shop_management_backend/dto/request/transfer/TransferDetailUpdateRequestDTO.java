package com.se330.coffee_shop_management_backend.dto.request.transfer;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class TransferDetailUpdateRequestDTO {
    private UUID id;
    private int transferDetailQuantity;
    private String transferDetailUnit;
    private UUID ingredientId;
    private UUID transferId;
}
