package com.se330.coffee_shop_management_backend.service.employeeservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.employee.EmployeeCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.employee.EmployeeUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.User;
import com.se330.coffee_shop_management_backend.entity.Branch;
import com.se330.coffee_shop_management_backend.entity.Employee;
import com.se330.coffee_shop_management_backend.repository.UserRepository;
import com.se330.coffee_shop_management_backend.repository.BranchRepository;
import com.se330.coffee_shop_management_backend.repository.EmployeeRepository;
import com.se330.coffee_shop_management_backend.service.employeeservices.IEmployeeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ImpEmployeeService implements IEmployeeService {

    private final EmployeeRepository employeeRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;

    public ImpEmployeeService(
            EmployeeRepository employeeRepository,
            BranchRepository branchRepository,
            UserRepository userRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.branchRepository = branchRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Employee findByIdEmployee(UUID id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Employee> findAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    @Override
    public Employee createEmployee(EmployeeCreateRequestDTO employeeCreateRequestDTO) {
        Branch branch = branchRepository.findById(employeeCreateRequestDTO.getBranchId())
                .orElseThrow(() -> new EntityNotFoundException("Branch not found with ID: " + employeeCreateRequestDTO.getBranchId()));

        User user = userRepository.findById(employeeCreateRequestDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + employeeCreateRequestDTO.getUserId()));

        return employeeRepository.save(
                Employee.builder()
                        .employeePosition(employeeCreateRequestDTO.getEmployeePosition())
                        .employeeDepartment(employeeCreateRequestDTO.getEmployeeDepartment())
                        .employeeHireDate(employeeCreateRequestDTO.getEmployeeHireDate())
                        .branch(branch)
                        .user(user)
                        .build()
        );
    }

    @Override
    public Employee updateEmployee(EmployeeUpdateRequestDTO employeeUpdateRequestDTO) {
        Employee existingEmployee = employeeRepository.findById(employeeUpdateRequestDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + employeeUpdateRequestDTO.getId()));

        Branch branch = branchRepository.findById(employeeUpdateRequestDTO.getBranchId())
                .orElseThrow(() -> new EntityNotFoundException("Branch not found with ID: " + employeeUpdateRequestDTO.getBranchId()));

        User user = userRepository.findById(employeeUpdateRequestDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + employeeUpdateRequestDTO.getUserId()));

        existingEmployee.setEmployeePosition(employeeUpdateRequestDTO.getEmployeePosition());
        existingEmployee.setEmployeeDepartment(employeeUpdateRequestDTO.getEmployeeDepartment());
        existingEmployee.setEmployeeHireDate(employeeUpdateRequestDTO.getEmployeeHireDate());
        existingEmployee.setBranch(branch);
        existingEmployee.setUser(user);

        return employeeRepository.save(existingEmployee);
    }

    @Transactional
    @Override
    public void deleteEmployee(UUID id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + id));

        // Clear relationships before deletion
        if (employee.getUser() != null) {
            employee.getUser().setEmployee(null);
        }
        if (employee.getBranch() != null) {
            employee.getBranch().getEmployees().remove(employee);
            employee.setBranch(null);
        }

        employeeRepository.delete(employee);
    }

}