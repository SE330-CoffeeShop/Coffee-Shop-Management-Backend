package com.se330.coffee_shop_management_backend.service.salaryservices;

import com.se330.coffee_shop_management_backend.dto.request.salary.SalaryCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.salary.SalaryUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Salary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ISalaryService {
    Salary findById(UUID id);
    Page<Salary> findAll(Pageable pageable);
    Salary create(SalaryCreateRequestDTO salaryCreateRequestDTO);
    Salary update(SalaryUpdateRequestDTO salaryUpdateRequestDTO);
    void updateSalaryForAllEmployeesInBranchInMonthAndYear(UUID branchId, int month, int year);
    void updateSalaryForAllEmployeesInMonthAndYear(int month, int year);
    void delete(UUID id);
}