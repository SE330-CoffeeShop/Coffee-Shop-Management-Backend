package com.se330.coffee_shop_management_backend.service.cartservices;

import com.se330.coffee_shop_management_backend.dto.request.cart.CartCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.cart.CartUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ICartService {
    Cart findByIdCart(UUID id);
    Page<Cart> findAllCarts(Pageable pageable);
    Page<Cart> findCartsByUserId(UUID userId, Pageable pageable);
    Cart createCart(CartCreateRequestDTO cartCreateRequestDTO);
    Cart updateCart(CartUpdateRequestDTO cartUpdateRequestDTO);
    void deleteCart(UUID id);
}