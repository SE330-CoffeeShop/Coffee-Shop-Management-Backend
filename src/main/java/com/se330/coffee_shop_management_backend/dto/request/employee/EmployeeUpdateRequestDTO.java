package com.se330.coffee_shop_management_backend.dto.request.employee;

import com.se330.coffee_shop_management_backend.entity.AbstractBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class EmployeeUpdateRequestDTO extends AbstractBaseEntity {
    private String employeePosition;
    private String employeeDepartment;
    private Date employeeHireDate;
    private UUID branchId;
    private UUID userId;
}