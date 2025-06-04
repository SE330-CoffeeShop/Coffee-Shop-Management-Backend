package com.se330.coffee_shop_management_backend.service.discountservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.discount.DiscountCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.discount.DiscountUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.discount.UsedDiscountCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.order.OrderDetailCreateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.*;
import com.se330.coffee_shop_management_backend.repository.*;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductVariantRepository;
import com.se330.coffee_shop_management_backend.service.discountservices.IDiscountService;
import com.se330.coffee_shop_management_backend.service.notificationservices.INotificationService;
import com.se330.coffee_shop_management_backend.service.useddiscount.IUsedDiscountService;
import com.se330.coffee_shop_management_backend.util.Constants;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
public class ImpDiscountService implements IDiscountService {

    private final DiscountRepository discountRepository;
    private final BranchRepository branchRepository;
    private final ProductVariantRepository productVariantRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UsedDiscountRepository usedDiscountRepository;
    private final IUsedDiscountService usedDiscountService;
    private final INotificationService notificationService;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;

    public ImpDiscountService(
            DiscountRepository discountRepository,
            BranchRepository branchRepository,
            ProductVariantRepository productVariantRepository,
            OrderDetailRepository orderDetailRepository,
            UsedDiscountRepository usedDiscountRepository,
            IUsedDiscountService usedDiscountService,
            INotificationService notificationService,
            CartRepository cartRepository,
            CartDetailRepository cartDetailRepository
    ) {
        this.discountRepository = discountRepository;
        this.branchRepository = branchRepository;
        this.productVariantRepository = productVariantRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.usedDiscountRepository = usedDiscountRepository;
        this.usedDiscountService = usedDiscountService;
        this.notificationService = notificationService;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
    }

    @Override
    public Discount findByIdDiscount(UUID id) {
        return discountRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Discount> findAllDiscounts(Pageable pageable) {
        return discountRepository.findAll(pageable);
    }

    @Override
    public Page<Discount> findAllDiscountsByBranchId(Pageable pageable, UUID branchId) {
        Branch existingBranch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found with id: " + branchId));

        return discountRepository.findAllByBranch(existingBranch, pageable);
    }

    @Override
    public Page<Discount> findAllDiscountsByProductVariantId(Pageable pageable, UUID productVariantId) {
        return discountRepository.findAllByProductVariants_Id(productVariantId, pageable);
    }

    @Override
    @Transactional
    public Discount createDiscount(DiscountCreateRequestDTO discountCreateRequestDTO) {
        Branch existingBranch = branchRepository.findById(discountCreateRequestDTO.getBranchId())
                .orElseThrow(() -> new EntityNotFoundException("Branch not found with id: " + discountCreateRequestDTO.getBranchId()));

        // TODO: validate the sender employee is a manager of the branch

        Discount returnDiscount = discountRepository.save(
                Discount.builder()
                        .discountName(discountCreateRequestDTO.getDiscountName())
                        .discountDescription(discountCreateRequestDTO.getDiscountDescription())
                        .discountType(Constants.DiscountTypeEnum.valueOf(discountCreateRequestDTO.getDiscountType()))
                        .discountValue(discountCreateRequestDTO.getDiscountValue())
                        .discountCode(discountCreateRequestDTO.getDiscountCode())
                        .discountStartDate(discountCreateRequestDTO.getDiscountStartDate())
                        .discountEndDate(discountCreateRequestDTO.getDiscountEndDate())
                        .discountMaxUsers(discountCreateRequestDTO.getDiscountMaxUsers())
                        .discountUserCount(0)
                        .discountMaxPerUser(discountCreateRequestDTO.getDiscountMaxPerUser())
                        .discountMinOrderValue(discountCreateRequestDTO.getDiscountMinOrderValue())
                        .discountIsActive(discountCreateRequestDTO.isDiscountIsActive())
                        .branch(existingBranch)
                        .productVariants(!discountCreateRequestDTO.getProductVariantIds().isEmpty() ?
                                discountCreateRequestDTO.getProductVariantIds().stream()
                                .map(id -> {
                                            return productVariantRepository.findById(id)
                                                    .orElseThrow(() -> new EntityNotFoundException("Product Variant not found with id: " + id));
                                        }).toList() : List.of())
                        .build()

        );

        // if the discount is active, send a notification to every user
        notificationService.sendNotificationToAllUsers(
                new NotificationCreateRequestDTO(
                    Constants.NotificationTypeEnum.DISCOUNT.name(),
                    "New Discount Available: " + discountCreateRequestDTO.getDiscountName() + " at " + existingBranch.getBranchName(),
                    UUID.randomUUID(),
                    UUID.randomUUID(),
                        false
                )
        );

        return returnDiscount;
    }

    @Override
    public Discount updateDiscount(DiscountUpdateRequestDTO discountUpdateRequestDTO) {
        return null;
    }

    @Override
    @Transactional
    public void deleteDiscount(UUID id) {
        Discount existingDiscount = discountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Discount not found with id: " + id));

        if (existingDiscount.getBranch() != null) {
            existingDiscount.getBranch().getDiscounts().remove(existingDiscount);
        }

        discountRepository.delete(existingDiscount);
    }

    @Override
    public boolean isDiscountValid(UUID discountId, UUID productVariantId, UUID userId, UUID employeeId) {
        Discount discount = discountRepository.findById(discountId).orElse(null);
        if (discount == null) {
            return false;
        }

        if (!discount.isDiscountIsActive()) {
            return false;
        }

        if (discount.getDiscountUserCount() >= discount.getDiscountMaxUsers()) {
            return false;
        }

        if (discount.getBranch().getEmployees().stream()
                .noneMatch(employee -> employee.getId().equals(employeeId))) {
            return false;
        }

        // Count how many times the user has used this discount
        int timesUsed = usedDiscountRepository.sumTimesUsedByUserAndDiscount(discountId, userId);

        // Check if the user has exceeded their maximum allowed uses
        if (timesUsed >= discount.getDiscountMaxPerUser()) {
            return false;
        }

        return true;
    }

    /**
     * Applies the most valuable discount to each item in an order detail.
     *
     * <p>This method processes each unit in an order detail individually to find and apply
     * the most valuable discount available, updating the unit price accordingly.</p>
     *
     * <p>The process for each item includes:</p>
     * <ol>
     *   <li>Evaluating all available discounts for the product variant</li>
     *   <li>Checking discount validity based on order value, user limits, etc.</li>
     *   <li>Calculating the price after applying each discount (percentage or fixed amount)</li>
     *   <li>Selecting the discount that results in the lowest price</li>
     *   <li>Recording the discount usage for the user</li>
     * </ol>
     *
     * <p>After processing all items, the method calculates a new average unit price
     * based on all applied discounts and updates the order detail.</p>
     *
     * @param orderDetailId The UUID of the order detail to apply discounts to
     * @param orderTotalValue The total value of the parent order, used for minimum order value validation
     * @throws EntityNotFoundException If the specified order detail cannot be found
     */
    @Override
    @Transactional
    public void applyMostValuableDiscountOfOrderDetail(UUID orderDetailId, BigDecimal orderTotalValue) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId).orElseThrow(() -> new EntityNotFoundException("Order Detail not found with id: " + orderDetailId));

        BigDecimal lowestCostOfOrderDetail = BigDecimal.ZERO;
        int numProductVariant = orderDetail.getOrderDetailQuantity();

        while (numProductVariant > 0) {

            BigDecimal lowestCostOfProductVariant = orderDetail.getOrderDetailUnitPrice();
            Discount mostValuableDiscountOfProductVariant = null;

            // first find the most valuable discount of that product variant
            for (Discount discount : orderDetail.getProductVariant().getDiscounts()) {
                BigDecimal currentCost = orderDetail.getOrderDetailUnitPrice();
                if (orderTotalValue.compareTo(discount.getDiscountMinOrderValue()) >= 0
                        && isDiscountValid(discount.getId(), orderDetail.getProductVariant().getId(), orderDetail.getOrder().getUser().getId(), orderDetail.getOrder().getEmployee().getId())) {
                    if (discount.getDiscountType().name().equals(Constants.DiscountTypeEnum.PERCENTAGE.name())) {
                        BigDecimal discountMultiplier = BigDecimal.valueOf(100)
                                .subtract(discount.getDiscountValue())
                                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

                        currentCost = currentCost.multiply(discountMultiplier);
                    } else {
                        currentCost = currentCost.subtract(discount.getDiscountValue());
                    }
                }

                if (lowestCostOfProductVariant.compareTo(currentCost) > 0) {
                    lowestCostOfProductVariant = currentCost;
                    mostValuableDiscountOfProductVariant = discount;
                }
            }

            // after the loop, we got the lowest cost of 1 product variant in that order detail
            // we first add it to the lowest cost of total order detail, for the later division
            lowestCostOfOrderDetail = lowestCostOfOrderDetail.add(lowestCostOfProductVariant);

            // then, we create a used discount
            if (mostValuableDiscountOfProductVariant != null) {
                usedDiscountService.createUsedDiscount(new UsedDiscountCreateRequestDTO(
                        orderDetailId,
                        mostValuableDiscountOfProductVariant.getId(),
                        1
                ));
            }

            // finally, reduce the amount of quantity
            numProductVariant--;
        }

        // now we got the new total cost of the order detail,
        // then we divide to get the new unit price of each variant in the order detail
        BigDecimal newUnitPrice = lowestCostOfOrderDetail.divide(BigDecimal.valueOf(orderDetail.getOrderDetailQuantity()), 2, RoundingMode.HALF_UP);
        BigDecimal discountCost = orderDetail.getOrderDetailUnitPrice().subtract(newUnitPrice).multiply(BigDecimal.valueOf(orderDetail.getOrderDetailQuantity()));

        // then save the new unit price
        orderDetail.setOrderDetailDiscountCost(discountCost);
        orderDetail.setOrderDetailUnitPriceAfterDiscount(newUnitPrice);
        orderDetailRepository.save(orderDetail);
    }

    @Override
    @Transactional
    public Cart applyDiscountToCart(UUID cartId) {
        Cart existingCart = cartRepository.findById(cartId).orElseThrow(
                () -> new EntityNotFoundException("Cart not found with id: " + cartId)
        );

        // Calculate the total cart value before discounts
        BigDecimal totalCartValue = existingCart.getCartTotalCost();

        BigDecimal totalDiscountAmount = BigDecimal.ZERO;

        // Apply discounts to each cart detail
        for (CartDetail cartDetail : existingCart.getCartDetails()) {
            BigDecimal lowestUnitPrice = cartDetail.getCartDetailUnitPrice();

            // Find the most valuable discount for this product variant
            for (Discount discount : cartDetail.getProductVariant().getDiscounts()) {
                // Skip inactive discounts or if minimum order value not met
                if (!discount.isDiscountIsActive() ||
                        totalCartValue.compareTo(discount.getDiscountMinOrderValue()) < 0) {
                    continue;
                }

                BigDecimal discountedPrice = cartDetail.getCartDetailUnitPrice();

                // Apply discount based on type
                if (discount.getDiscountType().name().equals(Constants.DiscountTypeEnum.PERCENTAGE.name())) {
                    BigDecimal discountMultiplier = BigDecimal.valueOf(100)
                            .subtract(discount.getDiscountValue())
                            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

                    discountedPrice = discountedPrice.multiply(discountMultiplier);
                } else {
                    discountedPrice = discountedPrice.subtract(discount.getDiscountValue());
                    if (discountedPrice.compareTo(BigDecimal.ZERO) < 0) {
                        discountedPrice = BigDecimal.ZERO;
                    }
                }

                // Keep track of the best discount
                if (discountedPrice.compareTo(lowestUnitPrice) < 0) {
                    lowestUnitPrice = discountedPrice;
                }
            }

            // Calculate discount amount
            BigDecimal originalCost = cartDetail.getCartDetailUnitPrice().multiply(BigDecimal.valueOf(cartDetail.getCartDetailQuantity()));
            BigDecimal discountedCost = lowestUnitPrice.multiply(BigDecimal.valueOf(cartDetail.getCartDetailQuantity()));
            BigDecimal discountAmount = originalCost.subtract(discountedCost);

            // Update cart detail with discount information
            cartDetail.setCartDetailUnitPriceAfterDiscount(lowestUnitPrice);
            cartDetail.setCartDetailDiscountCost(discountAmount);

            // Add to total discount
            totalDiscountAmount = totalDiscountAmount.add(discountAmount);
        }

        // Update cart totals
        BigDecimal totalAfterDiscount = totalCartValue.subtract(totalDiscountAmount);
        existingCart.setCartTotalCost(totalCartValue);
        existingCart.setCartDiscountCost(totalDiscountAmount);
        existingCart.setCartTotalCostAfterDiscount(totalAfterDiscount);

        // Save updated cart
        return cartRepository.save(existingCart);
    }
}
