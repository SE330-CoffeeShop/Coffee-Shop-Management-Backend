package com.se330.coffee_shop_management_backend.service.checkinservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.checkin.CheckinCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.checkin.CheckinUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationCreateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Checkin;
import com.se330.coffee_shop_management_backend.entity.Shift;
import com.se330.coffee_shop_management_backend.entity.User;
import com.se330.coffee_shop_management_backend.repository.CheckinRepository;
import com.se330.coffee_shop_management_backend.repository.ShiftRepository;
import com.se330.coffee_shop_management_backend.service.checkinservices.ICheckinService;
import com.se330.coffee_shop_management_backend.service.notificationservices.INotificationService;
import com.se330.coffee_shop_management_backend.util.Constants;
import com.se330.coffee_shop_management_backend.util.CreateNotiContentHelper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ImpCheckinService implements ICheckinService {

    private final CheckinRepository checkinRepository;
    private final ShiftRepository shiftRepository;
    private final INotificationService notificationService;

    public ImpCheckinService(
            CheckinRepository checkinRepository,
            ShiftRepository shiftRepository,
            INotificationService notificationService
    ) {
        this.checkinRepository = checkinRepository;
        this.shiftRepository = shiftRepository;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional(readOnly = true)
    public Checkin findById(UUID id) {
        return checkinRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Checkin> findAll(Pageable pageable) {
        return checkinRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Checkin> findAllByShiftId(UUID shiftId, Pageable pageable) {
        return checkinRepository.findAllByShiftId(shiftId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Checkin> findAllByEmployeeId(UUID employeeId, Pageable pageable) {
        return checkinRepository.findAllByEmployeeId(employeeId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Checkin> findAllByBranchId(UUID branchId, Pageable pageable) {
        return checkinRepository.findAllByBranchId(branchId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Checkin> findAllByShiftIdAndYear(UUID shiftId, int year, Pageable pageable) {
        return checkinRepository.findAllByShiftIdAndYear(shiftId, year, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Checkin> findAllByShiftIdAndYearAndMonth(UUID shiftId, int year, int month, Pageable pageable) {
        return checkinRepository.findAllByShiftIdAndYearAndMonth(shiftId, year, month, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Checkin> findAllByShiftIdAndYearAndMonthAndDay(UUID shiftId, int year, int month, int day, Pageable pageable) {
        return checkinRepository.findAllByShiftIdAndYearAndMonthAndDay(shiftId, year, month, day, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Checkin> findAllByEmployeeIdAndYear(UUID employeeId, int year, Pageable pageable) {
        return checkinRepository.findAllByEmployeeIdAndYear(employeeId, year, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Checkin> findAllByEmployeeIdAndYearAndMonth(UUID employeeId, int year, int month, Pageable pageable) {
        return checkinRepository.findAllByEmployeeIdAndYearAndMonth(employeeId, year, month, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Checkin> findAllByEmployeeIdAndYearAndMonthAndDay(UUID employeeId, int year, int month, int day, Pageable pageable) {
        return checkinRepository.findAllByEmployeeIdAndYearAndMonthAndDay(employeeId, year, month, day, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Checkin> findAllByBranchIdAndYear(UUID branchId, int year, Pageable pageable) {
        return checkinRepository.findAllByBranchIdAndYear(branchId, year, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Checkin> findAllByBranchIdAndYearAndMonth(UUID branchId, int year, int month, Pageable pageable) {
        return checkinRepository.findAllByBranchIdAndYearAndMonth(branchId, year, month, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Checkin> findAllByBranchIdAndYearAndMonthAndDay(UUID branchId, int year, int month, int day, Pageable pageable) {
        return checkinRepository.findAllByBranchIdAndYearAndMonthAndDay(branchId, year, month, day, pageable);
    }

    @Override
    @Transactional
    public Checkin create(CheckinCreateRequestDTO checkinCreateRequestDTO) {
        Shift shift = shiftRepository.findById(checkinCreateRequestDTO.getShiftId())
                .orElseThrow(() -> new EntityNotFoundException("Shift not found with id: " + checkinCreateRequestDTO.getShiftId()));

        Checkin checkin =  checkinRepository.save(Checkin.builder()
                .shift(shift)
                .checkinTime(checkinCreateRequestDTO.getCheckinTime())
                .build());

        User employee = checkin.getShift().getEmployee().getUser();
        User manager = checkin.getShift().getEmployee().getBranch().getManager().getUser();

        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                        .notificationContent(CreateNotiContentHelper.createCheckinSuccessContent(
                                employee.getFullName(),
                                checkin.getCheckinTime().toString()
                        ))
                        .senderId(null)
                        .receiverId(manager.getId())
                        .isRead(false)
                        .build()
        );

        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                        .notificationContent(CreateNotiContentHelper.createCheckinSuccessContent(checkin.getCheckinTime().toString()))
                        .senderId(null)
                        .receiverId(employee.getId())
                        .isRead(false)
                        .build()
        );

        return checkin;
    }

    @Override
    @Transactional
    public Checkin update(CheckinUpdateRequestDTO checkinUpdateRequestDTO) {
        Checkin existingCheckin = checkinRepository.findById(checkinUpdateRequestDTO.getCheckinId())
                .orElseThrow(() -> new EntityNotFoundException("Checkin not found with id: " + checkinUpdateRequestDTO.getCheckinId()));

        User employee = existingCheckin.getShift().getEmployee().getUser();
        User manager = existingCheckin.getShift().getEmployee().getBranch().getManager().getUser();

        existingCheckin.setCheckinTime(checkinUpdateRequestDTO.getCheckinTime());

        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                        .notificationContent(CreateNotiContentHelper.createCheckinUpdatedContentForManager(
                                employee.getFullName(),
                                existingCheckin.getCheckinTime().toString()
                        ))
                        .senderId(null)
                        .receiverId(manager.getId())
                        .isRead(false)
                        .build()
        );

        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                        .notificationContent(CreateNotiContentHelper.createCheckinUpdatedContent(existingCheckin.getCheckinTime().toString()))
                        .senderId(null)
                        .receiverId(employee.getId())
                        .isRead(false)
                        .build()
        );

        return checkinRepository.save(existingCheckin);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Checkin checkin = checkinRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Checkin not found with id: " + id));

        User employee = checkin.getShift().getEmployee().getUser();
        User manager = checkin.getShift().getEmployee().getBranch().getManager().getUser();

        checkin.getShift().getCheckins().remove(checkin);
        checkin.setShift(null);

        checkinRepository.delete(checkin);

        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                        .notificationContent(CreateNotiContentHelper.createCheckinDeletedContentForManager(
                                checkin.getShift().getEmployee().getUser().getFullName(),
                                checkin.getCheckinTime().toString()
                        ))
                        .senderId(null)
                        .receiverId(manager.getId())
                        .isRead(false)
                        .build()
        );

        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                        .notificationContent(CreateNotiContentHelper.createCheckinDeletedContent(checkin.getCheckinTime().toString()))
                        .senderId(manager.getId())
                        .receiverId(employee.getId())
                        .isRead(false)
                        .build()
        );
    }
}