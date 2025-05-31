package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, UUID>, JpaSpecificationExecutor<Salary> {
    @Query("""
        SELECT COALESCE(SUM(s.shiftSalary), 0)
        FROM Shift s
        JOIN s.checkins c
        WHERE s.employee.id = :employeeId
        AND EXTRACT(MONTH FROM c.checkinTime) = :month
        AND EXTRACT(YEAR FROM c.checkinTime) = :year
    """)
    BigDecimal calculateTotalSalaryForEmployeeInMonthAndYear(
            @Param("employeeId") UUID employeeId,
            @Param("month") int month,
            @Param("year") int year
    );
}