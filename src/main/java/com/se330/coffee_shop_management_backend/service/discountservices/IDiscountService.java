package com.se330.coffee_shop_management_backend.service.discountservices;

import com.se330.coffee_shop_management_backend.dto.request.discount.DiscountCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.discount.DiscountUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Discount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.UUID;

public interface IDiscountService {
    Discount findByIdDiscount(UUID id);
    Page<Discount> findAllDiscounts(Pageable pageable);
    Page<Discount> findAllDiscountsByBranchId(Pageable pageable, UUID branchId);
    Page<Discount> findAllDiscountsByProductVariantId(Pageable pageable, UUID productVariantId);
    Discount createDiscount(DiscountCreateRequestDTO discountCreateRequestDTO);
    Discount updateDiscount(DiscountUpdateRequestDTO discountUpdateRequestDTO);
    void deleteDiscount(UUID id);
    boolean isDiscountValid(UUID discountId, UUID productVariantId, UUID userId, UUID employeeId);
    void applyMostValuableDiscountOfOrderDetail(UUID orderDetailId, BigDecimal orderTotalValue);
}