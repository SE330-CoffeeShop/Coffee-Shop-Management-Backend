package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.Shift;
import com.se330.coffee_shop_management_backend.util.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, UUID>, JpaSpecificationExecutor<Shift> {
    @Override
    Page<Shift> findAll(Pageable pageable);

    Page<Shift> findByEmployee_Branch_Id(UUID branchId, Pageable pageable);

    Page<Shift> findAllByEmployee_Id(UUID employeeId, Pageable pageable);

    Page<Shift> findByEmployee_Branch_IdAndDayOfWeekAndMonthAndYear(
            UUID branchId,
            Constants.DayOfWeekEnum dayOfWeek,
            int month,
            int year,
            Pageable pageable
    );
}
