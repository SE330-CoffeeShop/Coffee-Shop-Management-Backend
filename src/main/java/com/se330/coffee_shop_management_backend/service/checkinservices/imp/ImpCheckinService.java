package com.se330.coffee_shop_management_backend.service.checkinservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.checkin.CheckinCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.checkin.CheckinUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Checkin;
import com.se330.coffee_shop_management_backend.entity.Shift;
import com.se330.coffee_shop_management_backend.repository.CheckinRepository;
import com.se330.coffee_shop_management_backend.repository.ShiftRepository;
import com.se330.coffee_shop_management_backend.service.checkinservices.ICheckinService;
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

    public ImpCheckinService(
            CheckinRepository checkinRepository,
            ShiftRepository shiftRepository
    ) {
        this.checkinRepository = checkinRepository;
        this.shiftRepository = shiftRepository;
    }

    @Override
    public Checkin findById(UUID id) {
        return checkinRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Checkin> findAll(Pageable pageable) {
        return checkinRepository.findAll(pageable);
    }

    @Override
    public Page<Checkin> findAllByShiftId(UUID shiftId, Pageable pageable) {
        return checkinRepository.findAllByShiftId(shiftId, pageable);
    }

    @Override
    public Page<Checkin> findAllByEmployeeId(UUID employeeId, Pageable pageable) {
        return checkinRepository.findAllByEmployeeId(employeeId, pageable);
    }

    @Override
    public Page<Checkin> findAllByBranchId(UUID branchId, Pageable pageable) {
        return checkinRepository.findAllByBranchId(branchId, pageable);
    }

    @Override
    public Page<Checkin> findAllByShiftIdAndYear(UUID shiftId, int year, Pageable pageable) {
        return checkinRepository.findAllByShiftIdAndYear(shiftId, year, pageable);
    }

    @Override
    public Page<Checkin> findAllByShiftIdAndYearAndMonth(UUID shiftId, int year, int month, Pageable pageable) {
        return checkinRepository.findAllByShiftIdAndYearAndMonth(shiftId, year, month, pageable);
    }

    @Override
    public Page<Checkin> findAllByShiftIdAndYearAndMonthAndDay(UUID shiftId, int year, int month, int day, Pageable pageable) {
        return checkinRepository.findAllByShiftIdAndYearAndMonthAndDay(shiftId, year, month, day, pageable);
    }

    @Override
    public Page<Checkin> findAllByEmployeeIdAndYear(UUID employeeId, int year, Pageable pageable) {
        return checkinRepository.findAllByEmployeeIdAndYear(employeeId, year, pageable);
    }

    @Override
    public Page<Checkin> findAllByEmployeeIdAndYearAndMonth(UUID employeeId, int year, int month, Pageable pageable) {
        return checkinRepository.findAllByEmployeeIdAndYearAndMonth(employeeId, year, month, pageable);
    }

    @Override
    public Page<Checkin> findAllByEmployeeIdAndYearAndMonthAndDay(UUID employeeId, int year, int month, int day, Pageable pageable) {
        return checkinRepository.findAllByEmployeeIdAndYearAndMonthAndDay(employeeId, year, month, day, pageable);
    }

    @Override
    public Page<Checkin> findAllByBranchIdAndYear(UUID branchId, int year, Pageable pageable) {
        return checkinRepository.findAllByBranchIdAndYear(branchId, year, pageable);
    }

    @Override
    public Page<Checkin> findAllByBranchIdAndYearAndMonth(UUID branchId, int year, int month, Pageable pageable) {
        return checkinRepository.findAllByBranchIdAndYearAndMonth(branchId, year, month, pageable);
    }

    @Override
    public Page<Checkin> findAllByBranchIdAndYearAndMonthAndDay(UUID branchId, int year, int month, int day, Pageable pageable) {
        return checkinRepository.findAllByBranchIdAndYearAndMonthAndDay(branchId, year, month, day, pageable);
    }

    @Override
    @Transactional
    public Checkin create(CheckinCreateRequestDTO checkinCreateRequestDTO) {
        Shift shift = shiftRepository.findById(checkinCreateRequestDTO.getShiftId())
                .orElseThrow(() -> new EntityNotFoundException("Shift not found with id: " + checkinCreateRequestDTO.getShiftId()));

        Checkin checkin = Checkin.builder()
                .shift(shift)
                .checkinTime(checkinCreateRequestDTO.getCheckinTime())
                .build();

        return checkinRepository.save(checkin);
    }

    @Override
    @Transactional
    public Checkin update(CheckinUpdateRequestDTO checkinUpdateRequestDTO) {
        Checkin existingCheckin = checkinRepository.findById(checkinUpdateRequestDTO.getCheckinId())
                .orElseThrow(() -> new EntityNotFoundException("Checkin not found with id: " + checkinUpdateRequestDTO.getCheckinId()));

        Shift shift = shiftRepository.findById(checkinUpdateRequestDTO.getShiftId())
                .orElseThrow(() -> new EntityNotFoundException("Shift not found with id: " + checkinUpdateRequestDTO.getShiftId()));

        // Remove from old shift if it's changed
        if (existingCheckin.getShift() != null &&
                !existingCheckin.getShift().getId().equals(checkinUpdateRequestDTO.getShiftId())) {
            existingCheckin.getShift().getCheckins().remove(existingCheckin);
        }

        existingCheckin.setShift(shift);
        existingCheckin.setCheckinTime(checkinUpdateRequestDTO.getCheckinTime());

        return checkinRepository.save(existingCheckin);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Checkin checkin = checkinRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Checkin not found with id: " + id));

        if (checkin.getShift() != null) {
            checkin.getShift().getCheckins().remove(checkin);
            checkin.setShift(null);
        }

        checkinRepository.delete(checkin);
    }
}