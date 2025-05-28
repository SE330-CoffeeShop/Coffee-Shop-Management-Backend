package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BranchRepository extends JpaRepository<Branch, UUID>, JpaSpecificationExecutor<Branch> {
    @Override
    Page<Branch> findAll(Pageable pageable);

    @Query("SELECT COALESCE(SUM(o.orderTotalCost), 0) FROM Order o " +
            "JOIN o.employee e " +
            "JOIN e.branch b " +
            "WHERE b.id = :branchId " +
            "AND FUNCTION('YEAR', o.createdAt) = :year")
    Optional<BigDecimal> calculateTotalOrderCostByBranchAndYear(
            @Param("branchId") UUID branchId,
            @Param("year") int year);

    @Query("SELECT COALESCE(SUM(o.orderTotalCost), 0) FROM Order o " +
            "JOIN o.employee e " +
            "JOIN e.branch b " +
            "WHERE b.id = :branchId " +
            "AND FUNCTION('MONTH', o.createdAt) = :month " +
            "AND FUNCTION('YEAR', o.createdAt) = :year")
    Optional<BigDecimal> calculateTotalOrderCostByBranchAndMonthAndYear(
            @Param("branchId") UUID branchId,
            @Param("month") int month,
            @Param("year") int year);

    @Query("SELECT COALESCE(SUM(o.orderTotalCost), 0) FROM Order o " +
            "JOIN o.employee e " +
            "JOIN e.branch b " +
            "WHERE b.id = :branchId " +
            "AND FUNCTION('DAY', o.createdAt) = :day " +
            "AND FUNCTION('MONTH', o.createdAt) = :month " +
            "AND FUNCTION('YEAR', o.createdAt) = :year")
    Optional<BigDecimal> calculateTotalOrderCostByBranchAndDayAndMonthAndYear(
            @Param("branchId") UUID branchId,
            @Param("day") int day,
            @Param("month") int month,
            @Param("year") int year);
}