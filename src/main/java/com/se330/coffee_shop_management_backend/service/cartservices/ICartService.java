package com.se330.coffee_shop_management_backend.service.cartservices;

import com.se330.coffee_shop_management_backend.dto.request.cart.CartDetailCreateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Cart;
import com.se330.coffee_shop_management_backend.entity.CartDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ICartService {
    Page<Cart> getAllCarts(Pageable pageable);
    Cart getCartByUserId(UUID userId);
    Page<CartDetail> getAllCartDetailsByUserId(UUID userId, Pageable pageable);
    Page<UUID> findBranchesWithSufficientInventory(UUID userId, Pageable pageable);
    Cart addCartDetail(UUID userId, CartDetailCreateRequestDTO cartDetailCreateRequestDTO);
    Cart removeCartDetail(UUID userId, UUID cartDetailId);
    Cart updateCartDetail(UUID userId, CartDetailCreateRequestDTO cartDetailCreateRequestDTO);
    Cart clearCart(UUID userId);
}