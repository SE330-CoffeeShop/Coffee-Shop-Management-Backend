package com.se330.coffee_shop_management_backend.service.cartservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.cart.CartDetailCreateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.CartDetail;
import com.se330.coffee_shop_management_backend.entity.Inventory;
import com.se330.coffee_shop_management_backend.entity.Recipe;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import com.se330.coffee_shop_management_backend.repository.CartDetailRepository;
import com.se330.coffee_shop_management_backend.repository.CartRepository;
import com.se330.coffee_shop_management_backend.repository.InventoryRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductVariantRepository;
import com.se330.coffee_shop_management_backend.service.cartservices.ICartDetailService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ImpCartDetailService implements ICartDetailService {

    private final CartDetailRepository cartDetailRepository;
    private final ProductVariantRepository productVariantRepository;
    private final InventoryRepository inventoryRepository;
    private final CartRepository cartRepository;

    public ImpCartDetailService(
            CartDetailRepository cartDetailRepository,
            ProductVariantRepository productVariantRepository,
            InventoryRepository inventoryRepository,
            CartRepository cartRepository
    ) {
        this.cartDetailRepository = cartDetailRepository;
        this.productVariantRepository = productVariantRepository;
        this.inventoryRepository = inventoryRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public CartDetail findByIdCartDetail(UUID id) {
        return cartDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CartDetail not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CartDetail> findAllCartDetails(Pageable pageable) {
        return cartDetailRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CartDetail> findAllCartDetailsByCartId(UUID cartId, Pageable pageable) {
        return cartDetailRepository.findAllByCart_Id(cartId, pageable);
    }

    @Override
    @Transactional
    public CartDetail createCartDetail(CartDetailCreateRequestDTO cartDetailCreateRequestDTO, UUID branchId, UUID cartId) {
        return cartDetailRepository.save(
                CartDetail.builder()
                        .productVariant(productVariantRepository.findById(cartDetailCreateRequestDTO.getVariantId()).orElseThrow(
                                () -> new IllegalArgumentException("HOW THE FUCK DO YOU EVEN GET HERE?")
                        ))
                        .cart(cartRepository.findById(cartId).orElseThrow(
                                () -> new IllegalArgumentException("HOW THE FUCK DO YOU EVEN GET HERE?")
                        ))
                        .cartDetailQuantity(cartDetailCreateRequestDTO.getCartDetailQuantity())
                        .cartDetailUnitPrice(cartDetailCreateRequestDTO.getCartDetailUnitPrice())
                        .cartDetailDiscountCost(BigDecimal.ZERO)
                        .cartDetailUnitPriceAfterDiscount(cartDetailCreateRequestDTO.getCartDetailUnitPrice())
                        .build()
        );
    }

    @Override
    @Transactional
    public List<CartDetail> createCartDetails(List<CartDetailCreateRequestDTO> cartDetailCreateRequestDTOs, UUID branchId, UUID cartId) {
        if (cartDetailCreateRequestDTOs == null || cartDetailCreateRequestDTOs.isEmpty()) {
            return List.of();
        }

        // now loop through the list to count the total quantity of ingredients needed to create the CartDetails
        Map<UUID, Integer> ingredientQuantities = new HashMap<>();

        for (CartDetailCreateRequestDTO cartDetailCreateRequestDTO : cartDetailCreateRequestDTOs) {
            ProductVariant variant = productVariantRepository.findById(cartDetailCreateRequestDTO.getVariantId())
                    .orElseThrow(() -> new IllegalArgumentException("Product variant not found with id: " + cartDetailCreateRequestDTO.getVariantId()));

            for (Recipe recipe : variant.getRecipes()) {
                UUID ingredientId = recipe.getIngredient().getId();
                int quantity = recipe.getRecipeQuantity() * cartDetailCreateRequestDTO.getCartDetailQuantity();

                if (ingredientQuantities.isEmpty()) {
                    ingredientQuantities.put(ingredientId, quantity);
                    continue;
                }

                ingredientQuantities.put(ingredientId, ingredientQuantities.get(ingredientId) + quantity);
            }
        }

        // now check if the branch has enough ingredients in its inventory
        List<Inventory> branchInventories = inventoryRepository.findAllByBranch_IdSortedByExpiredDay(branchId);

        if (branchInventories.isEmpty()) {
            throw new IllegalArgumentException("Branch with id " + branchId + " has no inventory.");
        }

        Map<UUID, Integer> availableIngredientQuantitiesInBranch = new HashMap<>();
        for (Inventory inventory : branchInventories) {
            availableIngredientQuantitiesInBranch.put(inventory.getId(), availableIngredientQuantitiesInBranch.get(inventory.getId()) == null ?
                    inventory.getInventoryQuantity() :
                    availableIngredientQuantitiesInBranch.get(inventory.getId()) + inventory.getInventoryQuantity());
        }

        for (Map.Entry<UUID, Integer> entry : ingredientQuantities.entrySet()) {
            UUID ingredientId = entry.getKey();
            int requiredQuantity = entry.getValue();

            if (!availableIngredientQuantitiesInBranch.containsKey(ingredientId) ||
                    availableIngredientQuantitiesInBranch.get(ingredientId) < requiredQuantity) {
                throw new IllegalArgumentException("Not enough inventory for ingredient with id: " + ingredientId);
            }
        }

        // Create a deep copy of ingredientQuantities to track remaining required quantities
        Map<UUID, Integer> remainingQuantities = new HashMap<>(ingredientQuantities);

        // Iterate through sorted inventories (the earliest expiry first)
        for (Inventory inventory : branchInventories) {
            UUID ingredientId = inventory.getIngredient().getId();

            // Check if we need this ingredient
            if (remainingQuantities.containsKey(ingredientId) && remainingQuantities.get(ingredientId) > 0) {
                int requiredQuantity = remainingQuantities.get(ingredientId);
                int availableQuantity = inventory.getInventoryQuantity();

                if (availableQuantity >= requiredQuantity) {
                    // We have enough in this inventory item
                    inventory.setInventoryQuantity(availableQuantity - requiredQuantity);
                    remainingQuantities.put(ingredientId, 0);
                } else {
                    // Use all available and continue with next inventory
                    inventory.setInventoryQuantity(0);
                    remainingQuantities.put(ingredientId, requiredQuantity - availableQuantity);
                }
            }
        }

        // Check if all requirements were satisfied
        boolean allSatisfied = remainingQuantities.values().stream().allMatch(qty -> qty == 0);
        if (!allSatisfied) {
            throw new IllegalArgumentException("Not enough inventory for required ingredients");
        }

        // Not yet saving the inventory changes to the database since this is just a cart, not an order.
        // inventoryRepository.saveAll(branchInventories);

        // now we create the CartDetails
        List<CartDetail> cartDetails = new ArrayList<>();
        for (CartDetailCreateRequestDTO cartDetailCreateRequestDTO : cartDetailCreateRequestDTOs) {
            cartDetails.add(createCartDetail(cartDetailCreateRequestDTO, branchId, cartId));
        }

        return cartDetails;
    }

    @Override
    @Transactional
    public void deleteCartDetail(UUID id) {
        CartDetail cartDetail = cartDetailRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("CartDetail not found with id: " + id)
        );

        if (cartDetail.getCart() != null) {
            cartDetail.getCart().getCartDetails().remove(cartDetail);
        }

        if (cartDetail.getProductVariant() != null) {
            cartDetail.getProductVariant().getCartDetails().remove(cartDetail);
        }

        cartDetailRepository.delete(cartDetail);
    }
}
