package com.se330.coffee_shop_management_backend.service.salaryservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.salary.SalaryCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.salary.SalaryUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.*;
import com.se330.coffee_shop_management_backend.repository.BranchRepository;
import com.se330.coffee_shop_management_backend.repository.EmployeeRepository;
import com.se330.coffee_shop_management_backend.repository.SalaryRepository;
import com.se330.coffee_shop_management_backend.service.salaryservices.ISalaryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class ImpSalaryService implements ISalaryService {

    private final SalaryRepository salaryRepository;
    private final EmployeeRepository employeeRepository;
    private final BranchRepository branchRepository;

    public ImpSalaryService(
            SalaryRepository salaryRepository,
            EmployeeRepository employeeRepository,
            BranchRepository branchRepository
    ) {
        this.salaryRepository = salaryRepository;
        this.employeeRepository = employeeRepository;
        this.branchRepository = branchRepository;
    }

    @Override
    public Salary findById(UUID id) {
        return salaryRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Salary> findAll(Pageable pageable) {
        return salaryRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Salary create(SalaryCreateRequestDTO salaryCreateRequestDTO) {
        Employee employee = employeeRepository.findById(salaryCreateRequestDTO.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + salaryCreateRequestDTO.getEmployeeId()));

        Salary salary = Salary.builder()
                .employee(employee)
                .month(salaryCreateRequestDTO.getMonth())
                .year(salaryCreateRequestDTO.getYear())
                .monthSalary(salaryCreateRequestDTO.getMonthSalary())
                .build();

        return salaryRepository.save(salary);
    }

    @Override
    @Transactional
    public Salary update(SalaryUpdateRequestDTO salaryUpdateRequestDTO) {
        Salary existingSalary = salaryRepository.findById(salaryUpdateRequestDTO.getSalaryId())
                .orElseThrow(() -> new EntityNotFoundException("Salary not found with id: " + salaryUpdateRequestDTO.getSalaryId()));

        Employee employee = employeeRepository.findById(salaryUpdateRequestDTO.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + salaryUpdateRequestDTO.getEmployeeId()));

        // If an employee is changing, handle the relationships
        if (existingSalary.getEmployee() != null &&
                !existingSalary.getEmployee().getId().equals(salaryUpdateRequestDTO.getEmployeeId())) {
            existingSalary.getEmployee().getSalaries().remove(existingSalary);
        }

        existingSalary.setEmployee(employee);
        existingSalary.setMonth(salaryUpdateRequestDTO.getMonth());
        existingSalary.setYear(salaryUpdateRequestDTO.getYear());
        existingSalary.setMonthSalary(salaryUpdateRequestDTO.getMonthSalary());

        return salaryRepository.save(existingSalary);
    }

    @Override
    @Transactional
    public void updateSalaryForAllEmployeesInBranchInMonthAndYear(UUID branchId, int month, int year) {
        Branch existingBranch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found with id: " + branchId));

        for (Employee employee : existingBranch.getEmployees()) {
            BigDecimal totalSalary = salaryRepository.calculateTotalSalaryForEmployeeInMonthAndYear(
                    employee.getId(), month, year
            );

            salaryRepository.save(
                    Salary.builder()
                            .employee(employee)
                            .month(month)
                            .year(year)
                            .monthSalary(totalSalary)
                            .build()
            );
        }
    }

    @Override
    @Transactional
    public void updateSalaryForAllEmployeesInMonthAndYear(int month, int year) {
        for (Branch branch : branchRepository.findAll()) {
            updateSalaryForAllEmployeesInBranchInMonthAndYear(branch.getId(), month, year);
        }
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Salary salary = salaryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Salary not found with id: " + id));

        // Remove from employee
        if (salary.getEmployee() != null) {
            salary.getEmployee().getSalaries().remove(salary);
            salary.setEmployee(null);
        }

        salaryRepository.delete(salary);
    }
}