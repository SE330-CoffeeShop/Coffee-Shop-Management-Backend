package com.se330.coffee_shop_management_backend.service.employeeservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.employee.EmployeeCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.employee.EmployeeUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationCreateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.User;
import com.se330.coffee_shop_management_backend.entity.Branch;
import com.se330.coffee_shop_management_backend.entity.Employee;
import com.se330.coffee_shop_management_backend.repository.UserRepository;
import com.se330.coffee_shop_management_backend.repository.BranchRepository;
import com.se330.coffee_shop_management_backend.repository.EmployeeRepository;
import com.se330.coffee_shop_management_backend.service.RoleService;
import com.se330.coffee_shop_management_backend.service.employeeservices.IEmployeeService;
import com.se330.coffee_shop_management_backend.service.notificationservices.INotificationService;
import com.se330.coffee_shop_management_backend.util.Constants;
import com.se330.coffee_shop_management_backend.util.CreateNotiContentHelper;
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
    private final INotificationService notificationService;

    public ImpEmployeeService(
            EmployeeRepository employeeRepository,
            BranchRepository branchRepository,
            UserRepository userRepository,
            RoleService roleService,
            INotificationService notificationService
    ) {
        this.employeeRepository = employeeRepository;
        this.branchRepository = branchRepository;
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.notificationService = notificationService;
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

        Employee newEmployee = employeeRepository.save(
                Employee.builder()
                        .employeePosition(employeeCreateRequestDTO.getEmployeePosition())
                        .employeeDepartment(employeeCreateRequestDTO.getEmployeeDepartment())
                        .employeeHireDate(employeeCreateRequestDTO.getEmployeeHireDate())
                        .branch(branch)
                        .user(user)
                        .build()
        );

        User manager = newEmployee.getBranch().getManager().getUser();
        User employeeUser = newEmployee.getUser();

        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                        .notificationContent(CreateNotiContentHelper.createWelcomeBranchContentManager(employeeUser.getFullName()))
                        .senderId(null)
                        .receiverId(manager.getId())
                        .isRead(false)
                        .build()
        );

        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                        .notificationContent(CreateNotiContentHelper.createWelcomeBranchContent(branch.getBranchName()))
                        .senderId(manager.getId())
                        .receiverId(employeeUser.getId())
                        .isRead(false)
                        .build()
        );

        return  newEmployee;
    }

    @Transactional
    @Override
    public Employee updateEmployee(EmployeeUpdateRequestDTO employeeUpdateRequestDTO) {
        Employee existingEmployee = employeeRepository.findById(employeeUpdateRequestDTO.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + employeeUpdateRequestDTO.getEmployeeId()));

        existingEmployee.setEmployeePosition(employeeUpdateRequestDTO.getEmployeePosition());
        existingEmployee.setEmployeeDepartment(employeeUpdateRequestDTO.getEmployeeDepartment());
        existingEmployee.setEmployeeHireDate(employeeUpdateRequestDTO.getEmployeeHireDate());

        User manager = existingEmployee.getBranch().getManager().getUser();
        User employeeUser = existingEmployee.getUser();

        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                        .notificationContent(CreateNotiContentHelper.createEmployeeInfoUpdatedContentForManager(employeeUser.getFullName()))
                        .senderId(null)
                        .receiverId(manager.getId())
                        .isRead(false)
                        .build()
        );

        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                        .notificationContent(CreateNotiContentHelper.createEmployeeInfoUpdatedContent())
                        .senderId(manager.getId())
                        .receiverId(employeeUser.getId())
                        .isRead(false)
                        .build()
        );

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