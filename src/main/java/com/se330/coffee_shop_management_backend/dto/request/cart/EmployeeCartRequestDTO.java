package com.se330.coffee_shop_management_backend.dto.request.cart;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class EmployeeCartRequestDTO {
    List<CartDetailCreateRequestDTO> cartDetails;
}
