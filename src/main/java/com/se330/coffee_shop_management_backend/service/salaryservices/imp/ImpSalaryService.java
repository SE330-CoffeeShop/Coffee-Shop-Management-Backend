package com.se330.coffee_shop_management_backend.service.salaryservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.salary.SalaryCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.salary.SalaryUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.*;
import com.se330.coffee_shop_management_backend.repository.BranchRepository;
import com.se330.coffee_shop_management_backend.repository.EmployeeRepository;
import com.se330.coffee_shop_management_backend.repository.SalaryRepository;
import com.se330.coffee_shop_management_backend.service.notificationservices.INotificationService;
import com.se330.coffee_shop_management_backend.service.salaryservices.ISalaryService;
import com.se330.coffee_shop_management_backend.util.Constants;
import com.se330.coffee_shop_management_backend.util.CreateNotiContentHelper;
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
    private final INotificationService notificationService;

    public ImpSalaryService(
            SalaryRepository salaryRepository,
            EmployeeRepository employeeRepository,
            BranchRepository branchRepository,
            INotificationService notificationService
    ) {
        this.salaryRepository = salaryRepository;
        this.employeeRepository = employeeRepository;
        this.branchRepository = branchRepository;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional(readOnly = true)
    public Salary findById(UUID id) {
        return salaryRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
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

        User manager = existingBranch.getManager().getUser();

        for (Employee employee : existingBranch.getEmployees()) {
            User employeeUser = employee.getUser();
            BigDecimal totalSalary = salaryRepository.calculateTotalSalaryForEmployeeInMonthAndYear(
                    employee.getId(), month, year
            );

            // if the salary for the month and year and employee already exists, update it
            Salary existingSalary = salaryRepository.findByEmployeeIdAndMonthAndYear(employee.getId(), month, year);

            if (existingSalary != null) {
                existingSalary.setMonthSalary(totalSalary);
                salaryRepository.save(existingSalary);

                notificationService.createNotification(
                        NotificationCreateRequestDTO.builder()
                                .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                                .notificationContent(
                                        CreateNotiContentHelper.createSalaryUpdatedContentForEmployee(
                                                String.valueOf(month),
                                                String.valueOf(year),
                                                String.valueOf(totalSalary))
                                )
                                .senderId(manager.getId())
                                .receiverId(employeeUser.getId())
                                .isRead(false)
                                .build()
                );
            } else {
                salaryRepository.save(
                        Salary.builder()
                                .employee(employee)
                                .month(month)
                                .year(year)
                                .monthSalary(totalSalary)
                                .build()
                );

                notificationService.createNotification(
                        NotificationCreateRequestDTO.builder()
                                .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                                .notificationContent(
                                        CreateNotiContentHelper.createSalaryCreatedContentForEmployee(
                                                String.valueOf(month),
                                                String.valueOf(year),
                                                String.valueOf(totalSalary))
                                )
                                .senderId(manager.getId())
                                .receiverId(employeeUser.getId())
                                .isRead(false)
                                .build()
                );
            }

            notificationService.createNotification(
                    NotificationCreateRequestDTO.builder()
                            .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                            .notificationContent(
                                    CreateNotiContentHelper.createSalaryCreatedContentForManagerAll(
                                            String.valueOf(month),
                                            String.valueOf(year)
                                    ))
                            .senderId(null)
                            .receiverId(manager.getId())
                            .isRead(false)
                            .build()
            );
        }
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Salary salary = salaryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Salary not found with id: " + id));

        Employee employee = salary.getEmployee();
        User employeeUser = employee.getUser();
        User manager = employee.getBranch().getManager().getUser();

        // Remove from employee
        if (employee != null) {
            employee.getSalaries().remove(salary);
            salary.setEmployee(null);
        }

        salaryRepository.delete(salary);

        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                        .notificationContent(
                                CreateNotiContentHelper.createSalaryDeletedContentForEmployee(
                                        String.valueOf(salary.getMonth()),
                                        String.valueOf(salary.getYear())
                                ))
                        .senderId(manager.getId())
                        .receiverId(employeeUser.getId())
                        .isRead(false)
                        .build()
        );

        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                        .notificationContent(
                                CreateNotiContentHelper.createSalaryDeletedContentForManager(
                                        employeeUser.getFullName(),
                                        String.valueOf(salary.getMonth()),
                                        String.valueOf(salary.getYear())
                                ))
                        .senderId(null)
                        .receiverId(manager.getId())
                        .isRead(false)
                        .build()
        );
    }

    @Override
    @Transactional
    public void updateSalaryForAllEmployeesInMonthAndYear(int month, int year) {
        for (Branch branch : branchRepository.findAll()) {
            updateSalaryForAllEmployeesInBranchInMonthAndYear(branch.getId(), month, year);
        }
    }
}