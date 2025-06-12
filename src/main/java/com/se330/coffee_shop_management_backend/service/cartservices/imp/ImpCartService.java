package com.se330.coffee_shop_management_backend.service.cartservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.cart.CartDetailCreateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.*;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import com.se330.coffee_shop_management_backend.repository.*;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductVariantRepository;
import com.se330.coffee_shop_management_backend.service.cartservices.ICartService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ImpCartService implements ICartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartDetailRepository cartDetailRepository;
    private final ProductVariantRepository productVariantRepository;
    private final BranchRepository branchRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Cart> getAllCarts(Pageable pageable) {
        return cartRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Cart getCartByUserId(UUID userId) {
        if (cartRepository.existsByUser_Id(userId)) {
            return cartRepository.findByUser_Id(userId);
        } else {
            return cartRepository.save(
                    Cart.builder()
                            .user(userRepository.findById(userId).orElseThrow(
                                    () -> new IllegalArgumentException("User not found with ID: " + userId)
                            ))
                            .cartDiscountCost(BigDecimal.ZERO)
                            .cartTotalCost(BigDecimal.ZERO)
                            .cartTotalCostAfterDiscount(BigDecimal.ZERO)
                            .build()
            );
        }
    }

    @Override
    public Page<CartDetail> getAllCartDetailsByUserId(UUID userId, Pageable pageable) {
        return cartDetailRepository.findAllByCart_User_Id(userId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UUID> findBranchesWithSufficientInventory(UUID userId, Pageable pageable) {
        Cart existingCart = cartRepository.findByUser_Id(userId);
        if (existingCart == null || existingCart.getCartDetails().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        List<CartDetail> cartDetails = existingCart.getCartDetails();

        Map<UUID, Integer> requiredIngredients = new HashMap<>();

        for (CartDetail cartDetail : cartDetails) {
            ProductVariant variant = cartDetail.getProductVariant();
            for (Recipe recipe : variant.getRecipes()) {
                UUID ingredientId = recipe.getIngredient().getId();
                int quantity = recipe.getRecipeQuantity() * cartDetail.getCartDetailQuantity();

                requiredIngredients.merge(ingredientId, quantity, Integer::sum);
            }
        }

        List<Branch> branches = branchRepository.findAll();
        List<UUID> sufficientBranchIds = new ArrayList<>();

        for (Branch branch : branches) {
            boolean isInventorySufficient = true;

            for (Map.Entry<UUID, Integer> entry : requiredIngredients.entrySet()) {
                UUID ingredientId = entry.getKey();
                Integer requiredQuantity = entry.getValue();

                Integer availableQuantity = inventoryRepository.countQuantityByBranch_IdAndIngredient_Id(branch.getId(), ingredientId);

                if (availableQuantity < requiredQuantity) {
                    isInventorySufficient = false;
                    break;
                }
            }

            if (isInventorySufficient) {
                sufficientBranchIds.add(branch.getId());
            }
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), sufficientBranchIds.size());

        if (start >= sufficientBranchIds.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, sufficientBranchIds.size());
        }

        List<UUID> pageContent = sufficientBranchIds.subList(start, end);
        return new PageImpl<>(pageContent, pageable, sufficientBranchIds.size());
    }

    @Override
    @Transactional
    public Cart addCartDetail(UUID userId, CartDetailCreateRequestDTO cartDetailCreateRequestDTO) {
        Cart existingCart = null;

        if (!cartRepository.existsByUser_Id(userId))
            existingCart = cartRepository.save(
                    Cart.builder()
                            .user(userRepository.findById(userId).orElseThrow(
                                    () -> new IllegalArgumentException("User not found with ID: " + userId)
                            ))
                            .cartDiscountCost(BigDecimal.ZERO)
                            .cartTotalCost(BigDecimal.ZERO)
                            .cartTotalCostAfterDiscount(BigDecimal.ZERO)
                            .build());
        else
            existingCart = cartRepository.findByUser_Id(userId);

        ProductVariant existingProductVariant = productVariantRepository.findById(cartDetailCreateRequestDTO.getVariantId())
                .orElseThrow(() -> new IllegalArgumentException("Product variant not found"));

        // Tạo CartDetail mới
        CartDetail newCartDetail = CartDetail.builder()
                .cart(existingCart)
                .productVariant(existingProductVariant)
                .cartDetailQuantity(cartDetailCreateRequestDTO.getCartDetailQuantity())
                .cartDetailUnitPrice(cartDetailCreateRequestDTO.getCartDetailUnitPrice())
                .cartDetailDiscountCost(BigDecimal.ZERO)
                .cartDetailUnitPriceAfterDiscount(cartDetailCreateRequestDTO.getCartDetailUnitPrice())
                .build();

        // Lưu CartDetail vào database
        newCartDetail = cartDetailRepository.save(newCartDetail);

        // Thêm CartDetail vào danh sách trong Cart (quan trọng)
        existingCart.getCartDetails().add(newCartDetail);

        calculateCartTotalCost(existingCart);

        // Lưu Cart sau khi cập nhật
        return cartRepository.save(existingCart);
    }

    @Override
    @Transactional
    public Cart removeCartDetail(UUID userId, UUID cartDetailId) {
        Cart existingCart = cartRepository.findByUser_Id(userId);

        if (existingCart == null) {
            throw new IllegalArgumentException("Cart not found for user ID: " + userId);
        }

        CartDetail existingCartDetail = cartDetailRepository.findById(cartDetailId)
                .orElseThrow(() -> new IllegalArgumentException("Cart detail not found with ID: " + cartDetailId));

        if (!existingCartDetail.getCart().getId().equals(existingCart.getId())) {
            throw new IllegalArgumentException("Cart detail does not belong to the user's cart");
        }

        cartDetailRepository.delete(existingCartDetail);

        calculateCartTotalCost(existingCart);

        return cartRepository.findById(existingCart.getId()).orElseThrow();
    }

    @Override
    @Transactional
    public Cart updateCartDetail(UUID userId, CartDetailCreateRequestDTO cartDetailCreateRequestDTO) {
        Cart existingCart = cartRepository.findByUser_Id(userId);

        if (existingCart == null) {
            throw new IllegalArgumentException("Cart not found for user ID: " + userId);
        }

        ProductVariant existingProductVariant = productVariantRepository.findById(cartDetailCreateRequestDTO.getVariantId())
                .orElseThrow(() -> new IllegalArgumentException("Product variant not found"));

        CartDetail existingCartDetail = cartDetailRepository.findByCart_IdAndProductVariant_Id(existingCart.getId(), existingProductVariant.getId());

        if (existingCartDetail != null) {
            existingCartDetail.setCartDetailQuantity(cartDetailCreateRequestDTO.getCartDetailQuantity());
            existingCartDetail.setCartDetailUnitPrice(cartDetailCreateRequestDTO.getCartDetailUnitPrice());
            existingCartDetail.setCartDetailUnitPriceAfterDiscount(cartDetailCreateRequestDTO.getCartDetailUnitPrice());
            cartDetailRepository.save(existingCartDetail);
        } else {
            throw new IllegalArgumentException("Cart detail not found for product variant ID: " + cartDetailCreateRequestDTO.getVariantId());
        }

        calculateCartTotalCost(existingCart);

        return cartRepository.findById(existingCart.getId()).orElseThrow();
    }



    @Transactional
    public Cart clearCart(UUID userId) {
        Cart cart = cartRepository.findByUser_Id(userId);

        if (cart == null) {
            return getCartByUserId(userId);
        }

        // Xóa tất cả cart details thông qua việc clear collection
        cart.getCartDetails().clear();

        // Cập nhật lại giá trị giỏ hàng
        cart.setCartTotalCost(BigDecimal.ZERO);
        cart.setCartDiscountCost(BigDecimal.ZERO);
        cart.setCartTotalCostAfterDiscount(BigDecimal.ZERO);

        // Lưu giỏ hàng đã cập nhật
        return cartRepository.save(cart);
    }

    @Transactional
    protected void calculateCartTotalCost(Cart cart) {
        BigDecimal cartTotalCost = BigDecimal.ZERO;
        BigDecimal cartDiscountCost = BigDecimal.ZERO;
        BigDecimal cartTotalCostAfterDiscount = BigDecimal.ZERO;

        if (cart.getCartDetails() != null && !cart.getCartDetails().isEmpty()) {
            for (CartDetail cartDetail : cart.getCartDetails()) {
                cartTotalCost = cartTotalCost.add(cartDetail.getCartDetailUnitPrice().multiply(BigDecimal.valueOf(cartDetail.getCartDetailQuantity())));
                cartDiscountCost = cartDiscountCost.add(cartDetail.getCartDetailDiscountCost());
                cartTotalCostAfterDiscount = cartTotalCostAfterDiscount.add(cartDetail.getCartDetailUnitPriceAfterDiscount().multiply(BigDecimal.valueOf(cartDetail.getCartDetailQuantity())));
            }
        }

        cart.setCartTotalCost(cartTotalCost);
        cart.setCartDiscountCost(cartDiscountCost);
        cart.setCartTotalCostAfterDiscount(cartTotalCostAfterDiscount);

        cartRepository.save(cart);
    }
}
