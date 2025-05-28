package com.se330.coffee_shop_management_backend.repository.productrepositories;

import com.se330.coffee_shop_management_backend.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    @Override
    Page<Product> findAll(Pageable pageable);

    // Case 1: Get all best-selling products (no filters)
    @Query("SELECT pv.product, SUM(od.orderDetailQuantity) as totalQuantity " +
            "FROM OrderDetail od " +
            "JOIN od.productVariant pv " +
            "GROUP BY pv.product " +
            "ORDER BY totalQuantity DESC")
    Page<Object[]> findAllBestSellingProducts(Pageable pageable);

    // Case 2: Filter by year
    @Query("SELECT pv.product, SUM(od.orderDetailQuantity) as totalQuantity " +
            "FROM OrderDetail od " +
            "JOIN od.productVariant pv " +
            "JOIN od.order o " +
            "WHERE FUNCTION('YEAR', o.createdAt) = :year " +
            "GROUP BY pv.product " +
            "ORDER BY totalQuantity DESC")
    Page<Object[]> findBestSellingProductsByYear(
            @Param("year") int year,
            Pageable pageable);

    // Case 3: Filter by month and year
    @Query("SELECT pv.product, SUM(od.orderDetailQuantity) as totalQuantity " +
            "FROM OrderDetail od " +
            "JOIN od.productVariant pv " +
            "JOIN od.order o " +
            "WHERE FUNCTION('YEAR', o.createdAt) = :year " +
            "AND FUNCTION('MONTH', o.createdAt) = :month " +
            "GROUP BY pv.product " +
            "ORDER BY totalQuantity DESC")
    Page<Object[]> findBestSellingProductsByMonthAndYear(
            @Param("month") int month,
            @Param("year") int year,
            Pageable pageable);

    // Case 4: Filter by day, month and year
    @Query("SELECT pv.product, SUM(od.orderDetailQuantity) as totalQuantity " +
            "FROM OrderDetail od " +
            "JOIN od.productVariant pv " +
            "JOIN od.order o " +
            "WHERE FUNCTION('YEAR', o.createdAt) = :year " +
            "AND FUNCTION('MONTH', o.createdAt) = :month " +
            "AND FUNCTION('DAY', o.createdAt) = :day " +
            "GROUP BY pv.product " +
            "ORDER BY totalQuantity DESC")
    Page<Object[]> findBestSellingProductsByDayAndMonthAndYear(
            @Param("day") int day,
            @Param("month") int month,
            @Param("year") int year,
            Pageable pageable);

    // Case 1.2: Filter by branch
    @Query("SELECT pv.product, SUM(od.orderDetailQuantity) as totalQuantity " +
            "FROM OrderDetail od " +
            "JOIN od.productVariant pv " +
            "JOIN od.order o " +
            "JOIN o.employee e " +
            "JOIN e.branch b " +
            "WHERE b.id = :branchId " +
            "GROUP BY pv.product " +
            "ORDER BY totalQuantity DESC")
    Page<Object[]> findBestSellingProductsByBranch(
            @Param("branchId") UUID branchId,
            Pageable pageable);

    // Case 2.2: Filter by branch and year
    @Query("SELECT pv.product, SUM(od.orderDetailQuantity) as totalQuantity " +
            "FROM OrderDetail od " +
            "JOIN od.productVariant pv " +
            "JOIN od.order o " +
            "JOIN o.employee e " +
            "JOIN e.branch b " +
            "WHERE b.id = :branchId " +
            "AND FUNCTION('YEAR', o.createdAt) = :year " +
            "GROUP BY pv.product " +
            "ORDER BY totalQuantity DESC")
    Page<Object[]> findBestSellingProductsByBranchAndYear(
            @Param("branchId") UUID branchId,
            @Param("year") int year,
            Pageable pageable);

    // Case 3.2: Filter by branch, month and year
    @Query("SELECT pv.product, SUM(od.orderDetailQuantity) as totalQuantity " +
            "FROM OrderDetail od " +
            "JOIN od.productVariant pv " +
            "JOIN od.order o " +
            "JOIN o.employee e " +
            "JOIN e.branch b " +
            "WHERE b.id = :branchId " +
            "AND FUNCTION('YEAR', o.createdAt) = :year " +
            "AND FUNCTION('MONTH', o.createdAt) = :month " +
            "GROUP BY pv.product " +
            "ORDER BY totalQuantity DESC")
    Page<Object[]> findBestSellingProductsByBranchAndMonthAndYear(
            @Param("branchId") UUID branchId,
            @Param("month") int month,
            @Param("year") int year,
            Pageable pageable);

    // Case 4.2: Filter by branch, day, month and year
    @Query("SELECT pv.product, SUM(od.orderDetailQuantity) as totalQuantity " +
            "FROM OrderDetail od " +
            "JOIN od.productVariant pv " +
            "JOIN od.order o " +
            "JOIN o.employee e " +
            "JOIN e.branch b " +
            "WHERE b.id = :branchId " +
            "AND FUNCTION('YEAR', o.createdAt) = :year " +
            "AND FUNCTION('MONTH', o.createdAt) = :month " +
            "AND FUNCTION('DAY', o.createdAt) = :day " +
            "GROUP BY pv.product " +
            "ORDER BY totalQuantity DESC")
    Page<Object[]> findBestSellingProductsByBranchAndDayAndMonthAndYear(
            @Param("branchId") UUID branchId,
            @Param("day") int day,
            @Param("month") int month,
            @Param("year") int year,
            Pageable pageable);
}