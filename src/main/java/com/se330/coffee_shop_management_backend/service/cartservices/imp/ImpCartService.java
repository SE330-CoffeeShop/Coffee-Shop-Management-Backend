package com.se330.coffee_shop_management_backend.service.cartservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.cart.CartCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.cart.CartUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Cart;
import com.se330.coffee_shop_management_backend.entity.CartDetail;
import com.se330.coffee_shop_management_backend.entity.User;
import com.se330.coffee_shop_management_backend.repository.CartRepository;
import com.se330.coffee_shop_management_backend.repository.UserRepository;
import com.se330.coffee_shop_management_backend.service.cartservices.ICartDetailService;
import com.se330.coffee_shop_management_backend.service.cartservices.ICartService;
import com.se330.coffee_shop_management_backend.service.discountservices.IDiscountService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class ImpCartService implements ICartService {

    private final CartRepository cartRepository;
    private final ICartDetailService cartDetailService;
    private final UserRepository userRepository;
    private final IDiscountService discountService;

    public ImpCartService(
            CartRepository cartRepository,
            ICartDetailService cartDetailService,
            UserRepository userRepository,
            IDiscountService discountService
    ) {
        this.cartRepository = cartRepository;
        this.cartDetailService = cartDetailService;
        this.userRepository = userRepository;
        this.discountService = discountService;
    }

    @Override
    @Transactional(readOnly = true)
    public Cart findByIdCart(UUID id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cart> findAllCarts(Pageable pageable) {
        return cartRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cart> findCartsByUserId(UUID userId, Pageable pageable) {
        return cartRepository.findAllByUser_Id(userId, pageable);
    }

    @Override
    @Transactional
    public Cart createCart(CartCreateRequestDTO cartCreateRequestDTO) {
        User existingUser = userRepository.findById(cartCreateRequestDTO.getUserId()).orElseThrow(
                () -> new RuntimeException("User not found with id: " + cartCreateRequestDTO.getUserId())
        );

        // first create a new Cart
        Cart newCart = cartRepository.save(
                Cart.builder()
                        .user(existingUser)
                        .cartTotalCost(BigDecimal.ZERO)
                        .cartDiscountCost(BigDecimal.ZERO)
                        .cartTotalCostAfterDiscount(BigDecimal.ZERO)
                        .build()
        );

        // then create CartDetails for the new Cart
        List<CartDetail> newCartDetails = cartDetailService.createCartDetails(cartCreateRequestDTO.getCartDetails(), cartCreateRequestDTO.getBranchId(), newCart.getId());

        // Recalculate the total cost of the cart after adding cart details
        BigDecimal totalCost = newCartDetails.stream()
                .map(detail -> detail.getCartDetailUnitPriceAfterDiscount().multiply(BigDecimal.valueOf(detail.getCartDetailQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        newCart.setCartTotalCost(totalCost);
        cartRepository.save(newCart);

        newCart = discountService.applyDiscountToCart(newCart.getId());

        return cartRepository.save(newCart);
    }

    @Override
    @Transactional
    public Cart updateCart(CartUpdateRequestDTO cartUpdateRequestDTO) {
        deleteCart(cartUpdateRequestDTO.getCartId());
        return createCart(CartCreateRequestDTO.builder()
                .userId(cartUpdateRequestDTO.getUserId())
                .branchId(cartUpdateRequestDTO.getBranchId())
                .cartDetails(cartUpdateRequestDTO.getCartDetails())
                .build()
        );
    }

    @Override
    @Transactional
    public void deleteCart(UUID id) {
        Cart existingCart = cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart not found with id: " + id));
        if (existingCart.getUser() != null) {
            existingCart.getUser().getCarts().remove(existingCart);
        }

        cartRepository.delete(existingCart);
    }
}
