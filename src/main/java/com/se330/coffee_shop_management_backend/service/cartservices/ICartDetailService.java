package com.se330.coffee_shop_management_backend.service.cartservices;

import com.se330.coffee_shop_management_backend.dto.request.cart.CartDetailCreateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.CartDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ICartDetailService {
    CartDetail findByIdCartDetail(UUID id);
    Page<CartDetail> findAllCartDetails(Pageable pageable);
    Page<CartDetail> findAllCartDetailsByCartId(UUID cartId, Pageable pageable);
    CartDetail createCartDetail(CartDetailCreateRequestDTO cartDetailCreateRequestDTO, UUID branchId, UUID cartId);
    List<CartDetail> createCartDetails(List<CartDetailCreateRequestDTO> cartDetailCreateRequestDTOs, UUID branchId, UUID cartId);
    void deleteCartDetail(UUID id);
}