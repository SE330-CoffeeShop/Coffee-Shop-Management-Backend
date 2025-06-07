package com.se330.coffee_shop_management_backend.service.shiftservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.shift.ShiftCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.shift.ShiftUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Employee;
import com.se330.coffee_shop_management_backend.entity.Shift;
import com.se330.coffee_shop_management_backend.entity.User;
import com.se330.coffee_shop_management_backend.repository.EmployeeRepository;
import com.se330.coffee_shop_management_backend.repository.ShiftRepository;
import com.se330.coffee_shop_management_backend.service.notificationservices.INotificationService;
import com.se330.coffee_shop_management_backend.service.shiftservices.IShiftService;
import com.se330.coffee_shop_management_backend.util.Constants;
import com.se330.coffee_shop_management_backend.util.CreateNotiContentHelper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ImpShiftService implements IShiftService {

    private final ShiftRepository shiftRepository;
    private final EmployeeRepository employeeRepository;
    private final INotificationService notificationService;

    public ImpShiftService(
            ShiftRepository shiftRepository,
            EmployeeRepository employeeRepository,
            INotificationService notificationService
    ) {
        this.shiftRepository = shiftRepository;
        this.employeeRepository = employeeRepository;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional(readOnly = true)
    public Shift findByIdShift(UUID id) {
        return shiftRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Shift> findAllShifts(Pageable pageable) {
        return shiftRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Shift createShift(ShiftCreateRequestDTO shiftCreateRequestDTO) {
        Employee employee = employeeRepository.findById(shiftCreateRequestDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        User employeeUser = employee.getUser();
        User manager = employee.getBranch().getManager().getUser();

        Shift newShift = shiftRepository.save(
                Shift.builder()
                        .employee(employee)
                        .shiftEndTime(shiftCreateRequestDTO.getShiftEndTime())
                        .shiftStartTime(shiftCreateRequestDTO.getShiftStartTime())
                        .build()
        );

        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                        .notificationContent(CreateNotiContentHelper.createNewShiftAssignmentContentManager(
                                employeeUser.getFullName(),
                                String.valueOf(newShift.getMonth()),
                                String.valueOf(newShift.getYear())
                        ))
                        .senderId(null)
                        .receiverId(manager.getId())
                        .isRead(false)
                        .build()
        );

        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                        .notificationContent(CreateNotiContentHelper.createNewShiftAssignmentContent(
                                String.valueOf(newShift.getMonth()),
                                String.valueOf(newShift.getYear())
                        ))
                        .senderId(manager.getId())
                        .receiverId(employeeUser.getId())
                        .isRead(false)
                        .build()
        );

        return newShift;
    }

    @Transactional
    @Override
    public Shift updateShift(ShiftUpdateRequestDTO shiftUpdateRequestDTO) {
        Shift existingShift = shiftRepository.findById(shiftUpdateRequestDTO.getShiftId())
                .orElseThrow(() -> new RuntimeException("Shift not found"));

        Employee employee = employeeRepository.findById(shiftUpdateRequestDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        User employeeUser = employee.getUser();
        User manager = employee.getBranch().getManager().getUser();

        if (existingShift.getEmployee() != null) {
            existingShift.getEmployee().getShifts().remove(existingShift);
            existingShift.setEmployee(employee);
            employee.getShifts().add(existingShift);
        }

        existingShift.setShiftStartTime(shiftUpdateRequestDTO.getShiftStartTime());
        existingShift.setShiftEndTime(shiftUpdateRequestDTO.getShiftEndTime());

        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                        .notificationContent(CreateNotiContentHelper.createShiftUpdatedContentForManager(employeeUser.getFullName()))
                        .senderId(null)
                        .receiverId(manager.getId())
                        .isRead(false)
                        .build()
        );

        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                        .notificationContent(CreateNotiContentHelper.createShiftUpdatedContent(
                                String.valueOf(existingShift.getMonth()),
                                String.valueOf(existingShift.getYear())
                        ))
                        .senderId(manager.getId())
                        .receiverId(employeeUser.getId())
                        .isRead(false)
                        .build()
        );

        return shiftRepository.save(existingShift);
    }

    @Transactional
    @Override
    public void deleteShift(UUID id) {
        Shift existingShift = shiftRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shift not found"));

        Employee employee = existingShift.getEmployee();
        User employeeUser = employee.getUser();
        User manager = employee.getBranch().getManager().getUser();

        existingShift.getEmployee().getShifts().remove(existingShift);
        existingShift.setEmployee(null);

        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                        .notificationContent(CreateNotiContentHelper.createShiftDeletedContentForManager(employeeUser.getFullName()))
                        .senderId(null)
                        .receiverId(manager.getId())
                        .isRead(false)
                        .build()
        );

        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                        .notificationContent(CreateNotiContentHelper.createShiftDeletedContent(
                                String.valueOf(existingShift.getMonth()),
                                String.valueOf(existingShift.getYear())
                        ))
                        .senderId(manager.getId())
                        .receiverId(employeeUser.getId())
                        .isRead(false)
                        .build()
        );

        shiftRepository.deleteById(id);
    }
}
