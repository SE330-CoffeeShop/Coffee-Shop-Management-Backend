package com.se330.coffee_shop_management_backend.dto.request.employee;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
public class EmployeeCreateRequestDTO {
    private String employeePosition;
    private String employeeDepartment;
    private LocalDateTime employeeHireDate;
    private UUID branchId;
    private UUID userId;
}