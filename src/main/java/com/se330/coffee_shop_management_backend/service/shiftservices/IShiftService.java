package com.se330.coffee_shop_management_backend.service.shiftservices;

import com.se330.coffee_shop_management_backend.dto.request.shift.ShiftCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.shift.ShiftUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Shift;
import com.se330.coffee_shop_management_backend.util.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IShiftService {
    Shift findByIdShift(UUID id);
    Page<Shift> findAllShifts(Pageable pageable);
    Page<Shift> findAllShiftsByBranch(UUID branchId, Pageable pageable);
    Page<Shift> findAllShiftsByEmployee(UUID employeeId, Pageable pageable);
    Page<Shift> findAllShiftsByBranchAndDayOfWeekAndMonthAndYear(
            UUID branchId, Constants.DayOfWeekEnum dayOfWeek, int month, int year, Pageable pageable);
    Shift createShift(ShiftCreateRequestDTO shiftCreateRequestDTO);
    Shift updateShift(ShiftUpdateRequestDTO shiftUpdateRequestDTO);
    void deleteShift(UUID id);
}
