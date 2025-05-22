package com.se330.coffee_shop_management_backend.service.customerservices;

import com.se330.coffee_shop_management_backend.dto.response.customer.CustomerResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ICustomerService {
    CustomerResponseDTO findByIdCustomer(UUID id, UUID branchId);
    Page<CustomerResponseDTO> findAllCustomerOfBranch(Pageable pageable, UUID branchId);
}
