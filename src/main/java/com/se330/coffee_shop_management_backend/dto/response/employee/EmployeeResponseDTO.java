package com.se330.coffee_shop_management_backend.dto.response.employee;

import com.se330.coffee_shop_management_backend.dto.response.AbstractBaseResponse;
import com.se330.coffee_shop_management_backend.entity.Employee;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
public class EmployeeResponseDTO extends AbstractBaseResponse {

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

    private String employeePosition;
    private String employeeDepartment;
    private LocalDateTime employeeHireDate;

    // Related entities
    private String branchId;
    private String userId;
    private String userFullName;
    private List<String> shiftIds;
    private List<String> orderIds;

    public static EmployeeResponseDTO convert(Employee employee) {
        return EmployeeResponseDTO.builder()
                .id(employee.getId().toString())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .employeePosition(employee.getEmployeePosition())
                .employeeDepartment(employee.getEmployeeDepartment())
                .employeeHireDate(employee.getEmployeeHireDate())
                .branchId(employee.getBranch() != null ? employee.getBranch().getId().toString() : null)
                .userId(employee.getUser() != null ? employee.getUser().getId().toString() : null)
                .userFullName(employee.getUser() != null ? employee.getUser().getFullName() : null)
                .shiftIds(employee.getShifts() != null ? employee.getShifts().stream()
                        .map(shift -> shift.getId().toString())
                        .collect(Collectors.toList()) : Collections.emptyList())
                .orderIds(employee.getOrders() != null ? employee.getOrders().stream()
                        .map(order -> order.getId().toString())
                        .collect(Collectors.toList()) : Collections.emptyList())
                .build();
    }

    public static List<EmployeeResponseDTO> convert(List<Employee> employees) {
        if (employees == null || employees.isEmpty()) {
            return Collections.emptyList();
        }

        return employees.stream()
                .map(EmployeeResponseDTO::convert)
                .collect(Collectors.toList());
    }
}