package com.se330.coffee_shop_management_backend.service.dummydataservices.domain;

import com.se330.coffee_shop_management_backend.entity.Branch;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import com.se330.coffee_shop_management_backend.repository.BranchRepository;
import com.se330.coffee_shop_management_backend.repository.DiscountRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductVariantRepository;
import com.se330.coffee_shop_management_backend.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class Discounts {
    private final DiscountRepository discountRepository;
    private final BranchRepository branchRepository;
    private final ProductVariantRepository productVariantRepository;

    public void create() {
        log.info("Creating discount dummy data...");
        List<com.se330.coffee_shop_management_backend.entity.Discount> discounts = createDiscount();
        createDiscountProductVariant(discounts);
        log.info("Created {} discounts", discounts.size());
    }

    private List<com.se330.coffee_shop_management_backend.entity.Discount> createDiscount() {
        List<Branch> branches = branchRepository.findAll();
        if (branches.isEmpty()) {
            log.error("Cannot create discounts: No branches found");
            return Collections.emptyList();
        }

        Random random = new Random();
        List<com.se330.coffee_shop_management_backend.entity.Discount> discounts = new ArrayList<>();

        // Mã giảm giá theo phần trăm
        List<Map<String, Object>> percentageDiscounts = new ArrayList<>();
        percentageDiscounts.add(Map.of(
                "name", "Mừng Ngày Lễ Lớn",
                "desc", "Giảm 20% cho tất cả đơn hàng nhân dịp lễ lớn, áp dụng cho khách hàng thân thiết.",
                "code", "LENHAY20",
                "value", new BigDecimal("20"),
                "minValue", new BigDecimal("100000")
        ));
        percentageDiscounts.add(Map.of(
                "name", "Khuyến Mãi Tết Nguyên Đán",
                "desc", "Mừng xuân mới với ưu đãi giảm 15% cho tất cả sản phẩm, chúc quý khách năm mới an khang thịnh vượng.",
                "code", "TET2023",
                "value", new BigDecimal("15"),
                "minValue", new BigDecimal("50000")
        ));
        percentageDiscounts.add(Map.of(
                "name", "Sinh Nhật BCoffee",
                "desc", "Mừng sinh nhật 5 năm BCoffee với ưu đãi giảm 25% cho tất cả sản phẩm. Cảm ơn quý khách đã đồng hành cùng chúng tôi.",
                "code", "SINHNHAT5",
                "value", new BigDecimal("25"),
                "minValue", new BigDecimal("150000")
        ));
        percentageDiscounts.add(Map.of(
                "name", "Quốc Tế Phụ Nữ",
                "desc", "Giảm 10% nhân ngày Quốc tế Phụ nữ 8/3, dành tặng cho một nửa xinh đẹp của thế giới.",
                "code", "WOMEN10",
                "value", new BigDecimal("10"),
                "minValue", new BigDecimal("0")
        ));
        percentageDiscounts.add(Map.of(
                "name", "Chào Mừng Thành Viên Mới",
                "desc", "Giảm 30% cho đơn hàng đầu tiên của thành viên mới. Chào mừng bạn đến với gia đình BCoffee!",
                "code", "WELCOME30",
                "value", new BigDecimal("30"),
                "minValue", new BigDecimal("50000")
        ));

        // Mã giảm giá cố định
        List<Map<String, Object>> fixedDiscounts = new ArrayList<>();
        fixedDiscounts.add(Map.of(
                "name", "Giảm 20K Đơn Hàng",
                "desc", "Giảm ngay 20.000đ cho đơn hàng từ 100.000đ, áp dụng cho tất cả các cửa hàng.",
                "code", "GIAM20K",
                "value", new BigDecimal("20000"),
                "minValue", new BigDecimal("100000")
        ));
        fixedDiscounts.add(Map.of(
                "name", "Ưu Đãi 50K Cuối Tuần",
                "desc", "Giảm ngay 50.000đ cho đơn hàng từ 200.000đ vào cuối tuần. Thưởng thức cà phê ngon với giá tốt hơn!",
                "code", "WEEKEND50",
                "value", new BigDecimal("50000"),
                "minValue", new BigDecimal("200000")
        ));
        fixedDiscounts.add(Map.of(
                "name", "Combo Tiết Kiệm",
                "desc", "Giảm ngay 30.000đ khi đặt combo 2 đồ uống và 1 bánh, áp dụng cho tất cả chi nhánh.",
                "code", "COMBO30K",
                "value", new BigDecimal("30000"),
                "minValue", new BigDecimal("150000")
        ));
        fixedDiscounts.add(Map.of(
                "name", "Freeship Nội Thành",
                "desc", "Miễn phí giao hàng trị giá 15.000đ cho đơn hàng từ 100.000đ trong phạm vi 5km.",
                "code", "FREESHIP",
                "value", new BigDecimal("15000"),
                "minValue", new BigDecimal("100000")
        ));
        fixedDiscounts.add(Map.of(
                "name", "Quà Tặng Sinh Nhật",
                "desc", "Tặng ngay 100.000đ cho đơn hàng nhân dịp sinh nhật khách hàng, áp dụng cho tất cả sản phẩm.",
                "code", "HPBD100K",
                "value", new BigDecimal("100000"),
                "minValue", new BigDecimal("300000")
        ));

        LocalDateTime now = LocalDateTime.now();

        // Tạo discount phần trăm
        for (Map<String, Object> info : percentageDiscounts) {
            Branch branch = branches.get(random.nextInt(branches.size()));

            LocalDateTime startDate, endDate;
            boolean isActive;

            // Tạo một số discount đã hết hạn, một số đang có hiệu lực, một số sẽ có hiệu lực trong tương lai
            int timeCase = random.nextInt(3);
            switch (timeCase) {
                case 0: // Đã hết hạn
                    startDate = now.minusMonths(3);
                    endDate = now.minusMonths(1);
                    isActive = false;
                    break;
                case 1: // Đang có hiệu lực
                    startDate = now.minusWeeks(1);
                    endDate = now.plusMonths(1);
                    isActive = true;
                    break;
                default: // Sẽ có hiệu lực
                    startDate = now.plusWeeks(1);
                    endDate = now.plusMonths(2);
                    isActive = true;
                    break;
            }

            com.se330.coffee_shop_management_backend.entity.Discount discount = com.se330.coffee_shop_management_backend.entity.Discount.builder()
                    .discountName((String) info.get("name"))
                    .discountDescription((String) info.get("desc"))
                    .discountType(Constants.DiscountTypeEnum.PERCENTAGE)
                    .discountValue((BigDecimal) info.get("value"))
                    .discountCode((String) info.get("code"))
                    .discountStartDate(startDate)
                    .discountEndDate(endDate)
                    .discountMaxUsers(100 + random.nextInt(900))
                    .discountUserCount(random.nextInt(50))
                    .discountMaxPerUser(1 + random.nextInt(3))
                    .discountMinOrderValue((BigDecimal) info.get("minValue"))
                    .discountIsActive(isActive)
                    .branch(branch)
                    .build();

            discounts.add(discount);
        }

        // Tạo discount cố định
        for (Map<String, Object> info : fixedDiscounts) {
            Branch branch = branches.get(random.nextInt(branches.size()));

            LocalDateTime startDate, endDate;
            boolean isActive;

            int timeCase = random.nextInt(3);
            switch (timeCase) {
                case 0: // Đã hết hạn
                    startDate = now.minusMonths(3);
                    endDate = now.minusMonths(1);
                    isActive = false;
                    break;
                case 1: // Đang có hiệu lực
                    startDate = now.minusWeeks(1);
                    endDate = now.plusMonths(1);
                    isActive = true;
                    break;
                default: // Sẽ có hiệu lực
                    startDate = now.plusWeeks(1);
                    endDate = now.plusMonths(2);
                    isActive = true;
                    break;
            }

            com.se330.coffee_shop_management_backend.entity.Discount discount = com.se330.coffee_shop_management_backend.entity.Discount.builder()
                    .discountName((String) info.get("name"))
                    .discountDescription((String) info.get("desc"))
                    .discountType(Constants.DiscountTypeEnum.AMOUNT)
                    .discountValue((BigDecimal) info.get("value"))
                    .discountCode((String) info.get("code"))
                    .discountStartDate(startDate)
                    .discountEndDate(endDate)
                    .discountMaxUsers(100 + random.nextInt(900))
                    .discountUserCount(random.nextInt(50))
                    .discountMaxPerUser(1 + random.nextInt(3))
                    .discountMinOrderValue((BigDecimal) info.get("minValue"))
                    .discountIsActive(isActive)
                    .branch(branch)
                    .build();

            discounts.add(discount);
        }

        return discountRepository.saveAll(discounts);
    }

    private void createDiscountProductVariant(List<com.se330.coffee_shop_management_backend.entity.Discount> discounts) {
        if (discounts.isEmpty()) {
            log.error("No discounts to associate with product variants");
            return;
        }

        List<ProductVariant> productVariants = productVariantRepository.findAll();
        if (productVariants.isEmpty()) {
            log.error("No product variants found to associate with discounts");
            return;
        }

        Random random = new Random();

        // Với mỗi discount, gắn 1-5 product variant
        for (com.se330.coffee_shop_management_backend.entity.Discount discount : discounts) {
            int variantCount = 1 + random.nextInt(5);
            List<ProductVariant> selectedVariants = new ArrayList<>();

            // Chọn ngẫu nhiên các variant
            for (int i = 0; i < variantCount; i++) {
                ProductVariant variant = productVariants.get(random.nextInt(productVariants.size()));
                if (!selectedVariants.contains(variant)) {
                    selectedVariants.add(variant);
                }
            }

            // Thêm discount vào danh sách discounts của mỗi variant đã chọn
            for (ProductVariant variant : selectedVariants) {
                variant.getDiscounts().add(discount);
            }

            // Cập nhật lại danh sách product variants cho discount
            discount.setProductVariants(selectedVariants);
        }

        // Lưu tất cả các thay đổi
        productVariantRepository.saveAll(productVariants);
        discountRepository.saveAll(discounts);

        log.info("Associated discounts with product variants");
    }
}