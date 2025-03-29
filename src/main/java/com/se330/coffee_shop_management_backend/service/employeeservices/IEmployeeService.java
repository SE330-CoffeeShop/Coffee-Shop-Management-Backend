package com.se330.coffee_shop_management_backend.service.employeeservices;

import com.se330.coffee_shop_management_backend.dto.request.employee.EmployeeCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.employee.EmployeeUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.employee.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IEmployeeService {
    Employee findByIdEmployee(UUID id);
    Page<Employee> findAllEmployees(Pageable pageable);
    Employee createEmployee(EmployeeCreateRequestDTO employeeCreateRequestDTO);
    Employee updateEmployee(EmployeeUpdateRequestDTO employeeUpdateRequestDTO);
    void deleteEmployee(UUID id);
}