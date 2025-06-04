package com.se330.coffee_shop_management_backend.service.employeeservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.employee.EmployeeCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.employee.EmployeeUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.User;
import com.se330.coffee_shop_management_backend.entity.Branch;
import com.se330.coffee_shop_management_backend.entity.Employee;
import com.se330.coffee_shop_management_backend.repository.UserRepository;
import com.se330.coffee_shop_management_backend.repository.BranchRepository;
import com.se330.coffee_shop_management_backend.repository.EmployeeRepository;
import com.se330.coffee_shop_management_backend.service.RoleService;
import com.se330.coffee_shop_management_backend.service.employeeservices.IEmployeeService;
import com.se330.coffee_shop_management_backend.util.Constants;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ImpEmployeeService implements IEmployeeService {

    private final EmployeeRepository employeeRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;
    private final RoleService roleService;

    public ImpEmployeeService(
            EmployeeRepository employeeRepository,
            BranchRepository branchRepository,
            UserRepository userRepository,
            RoleService roleService
    ) {
        this.employeeRepository = employeeRepository;
        this.branchRepository = branchRepository;
        this.userRepository = userRepository;
        this.roleService = roleService;
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
    public Page<Employee> findAllEmployeesByBranchId(UUID branchId, Pageable pageable) {
        return employeeRepository.findAllByBranch_Id(branchId, pageable);
    }

    @Override
    public Employee createEmployee(EmployeeCreateRequestDTO employeeCreateRequestDTO) {
        Branch branch = branchRepository.findById(employeeCreateRequestDTO.getBranchId())
                .orElseThrow(() -> new EntityNotFoundException("Branch not found with ID: " + employeeCreateRequestDTO.getBranchId()));

        User user = userRepository.findById(employeeCreateRequestDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + employeeCreateRequestDTO.getUserId()));

        if (user.getEmployee() != null) {
            throw new IllegalStateException("User with ID " + user.getId() + " is already assigned to an employee.");
        }

        user.getRole().getUsers().remove(user);
        user.setRole(roleService.findByName(Constants.RoleEnum.EMPLOYEE));
        userRepository.save(user);

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

    @Transactional
    @Override
    public Employee updateEmployee(EmployeeUpdateRequestDTO employeeUpdateRequestDTO) {
        Employee existingEmployee = employeeRepository.findById(employeeUpdateRequestDTO.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + employeeUpdateRequestDTO.getEmployeeId()));

        Branch newBranch = branchRepository.findById(employeeUpdateRequestDTO.getBranchId())
                .orElseThrow(() -> new EntityNotFoundException("Branch not found with ID: " + employeeUpdateRequestDTO.getBranchId()));

        User newUser = userRepository.findById(employeeUpdateRequestDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + employeeUpdateRequestDTO.getUserId()));

        if (existingEmployee.getBranch() != null) {
            existingEmployee.getBranch().getEmployees().remove(existingEmployee);
        }

        existingEmployee.setEmployeePosition(employeeUpdateRequestDTO.getEmployeePosition());
        existingEmployee.setEmployeeDepartment(employeeUpdateRequestDTO.getEmployeeDepartment());
        existingEmployee.setEmployeeHireDate(employeeUpdateRequestDTO.getEmployeeHireDate());
        existingEmployee.setBranch(newBranch);
        existingEmployee.setUser(newUser);

        newBranch.getEmployees().add(existingEmployee);

        return employeeRepository.save(existingEmployee);
    }

    @Transactional
    @Override
    public void deleteEmployee(UUID id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + id));

        if (employee.getBranch() != null) {
            employee.getBranch().getEmployees().remove(employee);
            employee.setBranch(null);
        }

        if (employee.getUser() != null) {
            employee.getUser().setEmployee(null);
            employee.setUser(null);
        }

        employeeRepository.deleteById(id);
    }

}