package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.Checkin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CheckinRepository extends JpaRepository<Checkin, UUID>, JpaSpecificationExecutor<Checkin> {
    // Basic queries by ID
    Page<Checkin> findAllByShiftId(UUID shiftId, Pageable pageable);

    @Query("SELECT c FROM Checkin c WHERE c.shift.employee.id = :employeeId")
    Page<Checkin> findAllByEmployeeId(@Param("employeeId") UUID employeeId, Pageable pageable);

    @Query("SELECT c FROM Checkin c WHERE c.shift.employee.branch.id = :branchId")
    Page<Checkin> findAllByBranchId(@Param("branchId") UUID branchId, Pageable pageable);

    // ShiftId with date components
    @Query("SELECT c FROM Checkin c WHERE c.shift.id = :shiftId AND EXTRACT(YEAR FROM c.checkinTime) = :year")
    Page<Checkin> findAllByShiftIdAndYear(@Param("shiftId") UUID shiftId, @Param("year") int year, Pageable pageable);

    @Query("SELECT c FROM Checkin c WHERE c.shift.id = :shiftId AND EXTRACT(YEAR FROM c.checkinTime) = :year AND EXTRACT(MONTH FROM c.checkinTime) = :month")
    Page<Checkin> findAllByShiftIdAndYearAndMonth(@Param("shiftId") UUID shiftId, @Param("year") int year, @Param("month") int month, Pageable pageable);

    @Query("SELECT c FROM Checkin c WHERE c.shift.id = :shiftId AND EXTRACT(YEAR FROM c.checkinTime) = :year AND EXTRACT(MONTH FROM c.checkinTime) = :month AND EXTRACT(DAY FROM c.checkinTime) = :day")
    Page<Checkin> findAllByShiftIdAndYearAndMonthAndDay(@Param("shiftId") UUID shiftId, @Param("year") int year, @Param("month") int month, @Param("day") int day, Pageable pageable);

    // EmployeeId with date components
    @Query("SELECT c FROM Checkin c WHERE c.shift.employee.id = :employeeId AND EXTRACT(YEAR FROM c.checkinTime) = :year")
    Page<Checkin> findAllByEmployeeIdAndYear(@Param("employeeId") UUID employeeId, @Param("year") int year, Pageable pageable);

    @Query("SELECT c FROM Checkin c WHERE c.shift.employee.id = :employeeId AND EXTRACT(YEAR FROM c.checkinTime) = :year AND EXTRACT(MONTH FROM c.checkinTime) = :month")
    Page<Checkin> findAllByEmployeeIdAndYearAndMonth(@Param("employeeId") UUID employeeId, @Param("year") int year, @Param("month") int month, Pageable pageable);

    @Query("SELECT c FROM Checkin c WHERE c.shift.employee.id = :employeeId AND EXTRACT(YEAR FROM c.checkinTime) = :year AND EXTRACT(MONTH FROM c.checkinTime) = :month AND EXTRACT(DAY FROM c.checkinTime) = :day")
    Page<Checkin> findAllByEmployeeIdAndYearAndMonthAndDay(@Param("employeeId") UUID employeeId, @Param("year") int year, @Param("month") int month, @Param("day") int day, Pageable pageable);

    // BranchId with date components
    @Query("SELECT c FROM Checkin c WHERE c.shift.employee.branch.id = :branchId AND EXTRACT(YEAR FROM c.checkinTime) = :year")
    Page<Checkin> findAllByBranchIdAndYear(@Param("branchId") UUID branchId, @Param("year") int year, Pageable pageable);

    @Query("SELECT c FROM Checkin c WHERE c.shift.employee.branch.id = :branchId AND EXTRACT(YEAR FROM c.checkinTime) = :year AND EXTRACT(MONTH FROM c.checkinTime) = :month")
    Page<Checkin> findAllByBranchIdAndYearAndMonth(@Param("branchId") UUID branchId, @Param("year") int year, @Param("month") int month, Pageable pageable);

    @Query("SELECT c FROM Checkin c WHERE c.shift.employee.branch.id = :branchId AND EXTRACT(YEAR FROM c.checkinTime) = :year AND EXTRACT(MONTH FROM c.checkinTime) = :month AND EXTRACT(DAY FROM c.checkinTime) = :day")
    Page<Checkin> findAllByBranchIdAndYearAndMonthAndDay(@Param("branchId") UUID branchId, @Param("year") int year, @Param("month") int month, @Param("day") int day, Pageable pageable);
}