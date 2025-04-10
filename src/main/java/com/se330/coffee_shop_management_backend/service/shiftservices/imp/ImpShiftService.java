package com.se330.coffee_shop_management_backend.service.shiftservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.shift.ShiftCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.shift.ShiftUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Employee;
import com.se330.coffee_shop_management_backend.entity.Shift;
import com.se330.coffee_shop_management_backend.repository.EmployeeRepository;
import com.se330.coffee_shop_management_backend.repository.ShiftRepository;
import com.se330.coffee_shop_management_backend.service.shiftservices.IShiftService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ImpShiftService implements IShiftService {

    private final ShiftRepository shiftRepository;
    private final EmployeeRepository employeeRepository;

    public ImpShiftService(
            ShiftRepository shiftRepository,
            EmployeeRepository employeeRepository
    ) {
        this.shiftRepository = shiftRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Shift findByIdShift(UUID id) {
        return shiftRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Shift> findAllShifts(Pageable pageable) {
        return shiftRepository.findAll(pageable);
    }

    @Override
    public Shift createShift(ShiftCreateRequestDTO shiftCreateRequestDTO) {
        Employee existingEmployee = employeeRepository.findById(shiftCreateRequestDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        return shiftRepository.save(
                Shift.builder()
                        .employee(existingEmployee)
                        .shiftEndTime(shiftCreateRequestDTO.getShiftEndTime())
                        .shiftStartTime(shiftCreateRequestDTO.getShiftStartTime())
                        .build()
        );
    }

    @Override
    public Shift updateShift(ShiftUpdateRequestDTO shiftUpdateRequestDTO) {
        Shift existingShift = shiftRepository.findById(shiftUpdateRequestDTO.getShiftId())
                .orElseThrow(() -> new RuntimeException("Shift not found"));

        Employee existingEmployee = employeeRepository.findById(shiftUpdateRequestDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        existingShift.setEmployee(existingEmployee);
        existingShift.setShiftStartTime(shiftUpdateRequestDTO.getShiftStartTime());
        existingShift.setShiftEndTime(shiftUpdateRequestDTO.getShiftEndTime());

        return shiftRepository.save(existingShift);
    }

    @Transactional
    @Override
    public void deleteShift(UUID id) {
        Shift existingShift = shiftRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shift not found"));

        if (existingShift.getEmployee() != null) {
            existingShift.getEmployee().getShifts().remove(existingShift);
        }

        shiftRepository.deleteById(id);
    }
}
