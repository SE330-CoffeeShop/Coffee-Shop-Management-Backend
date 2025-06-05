package com.se330.coffee_shop_management_backend.service.dummydataservices.domain;

import com.se330.coffee_shop_management_backend.entity.*;
import com.se330.coffee_shop_management_backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class StockAndInventory {

    private final StockRepository stockRepository;
    private final InventoryRepository inventoryRepository;
    private final IngredientRepository ingredientRepository;
    private final WarehouseRepository warehouseRepository;
    private final BranchRepository branchRepository;

    public void create() {
    }

    private void createStock() {
        log.info("Creating stock records...");

        List<Ingredient> ingredients = ingredientRepository.findAll();
        List<Warehouse> warehouses = warehouseRepository.findAll();

        if (ingredients.isEmpty() || warehouses.isEmpty()) {
            log.error("No ingredients or warehouses found. Cannot create stock records.");
            return;
        }

        List<Stock> stocks = new ArrayList<>();
        Random random = new Random();

        // Map ingredient types to appropriate units
        Map<String, String> unitsByType = new HashMap<>();
        unitsByType.put("COFFEE_BEANS", "kg");
        unitsByType.put("COFFEE_BASE", "l");
        unitsByType.put("DAIRY", "l");
        unitsByType.put("SYRUP", "l");
        unitsByType.put("TEA_LEAVES", "kg");
        unitsByType.put("TEA_TOPPING", "kg");
        unitsByType.put("FRUIT", "kg");
        unitsByType.put("BASE", "kg");
        unitsByType.put("SWEETENER", "l");
        unitsByType.put("TOPPING", "kg");
        unitsByType.put("POWDER", "kg");
        unitsByType.put("FLOUR", "kg");
        unitsByType.put("MEAT", "kg");
        unitsByType.put("CAKE_MIX", "kg");

        // Define warehouse specializations to distribute ingredients realistically
        Map<String, List<String>> warehouseSpecializations = new HashMap<>();
        warehouseSpecializations.put("Kho Trung Tâm TP.HCM", Arrays.asList("COFFEE_BASE", "DAIRY", "SYRUP", "BASE", "SWEETENER", "TOPPING", "POWDER"));
        warehouseSpecializations.put("Kho Nguyên Liệu Đà Lạt", Arrays.asList("COFFEE_BEANS", "TEA_LEAVES", "FRUIT"));
        warehouseSpecializations.put("Kho Xuất Khẩu Miền Đông", Arrays.asList("COFFEE_BEANS", "TEA_LEAVES", "FLOUR", "CAKE_MIX"));
        warehouseSpecializations.put("Kho Vận Miền Tây", Arrays.asList("TEA_TOPPING", "FRUIT", "MEAT", "FLOUR"));

        // Create stock for each ingredient in appropriate warehouses
        for (Ingredient ingredient : ingredients) {
            String ingredientType = ingredient.getIngredientType();
            String unit = unitsByType.getOrDefault(ingredientType, "kg");

            for (Warehouse warehouse : warehouses) {
                // Check if this warehouse specializes in this ingredient type
                boolean isSpecialized = warehouseSpecializations.getOrDefault(warehouse.getWarehouseName(), Collections.emptyList())
                        .contains(ingredientType);

                // Skip creating stock for non-specialized warehouses (50% chance)
                if (!isSpecialized && random.nextBoolean()) {
                    continue;
                }

                // Calculate base quantity based on ingredient type
                int baseQuantity = getBaseQuantity(ingredientType);

                // Adjust quantity based on warehouse specialization
                int quantity = isSpecialized ?
                        baseQuantity + random.nextInt(baseQuantity) : // More stock in specialized warehouses
                        Math.max(1, baseQuantity / 2 + random.nextInt(baseQuantity / 2)); // Less stock elsewhere

                stocks.add(Stock.builder()
                        .ingredient(ingredient)
                        .warehouse(warehouse)
                        .stockQuantity(quantity)
                        .stockUnit(unit)
                        .build());
            }
        }

        stockRepository.saveAll(stocks);
        log.info("Created {} stock records", stocks.size());
    }

    private int getBaseQuantity(String ingredientType) {
        // Return appropriate base quantities based on ingredient type
        switch (ingredientType) {
            case "COFFEE_BEANS":
                return 50; // 50kg of coffee beans
            case "COFFEE_BASE":
                return 30; // 30l of coffee base
            case "DAIRY":
                return 40; // 40l of dairy products
            case "SYRUP":
                return 25; // 25l of syrups
            case "TEA_LEAVES":
                return 20; // 20kg of tea leaves
            case "TEA_TOPPING":
                return 15; // 15kg of tea toppings
            case "FRUIT":
                return 25; // 25kg of fruits
            case "BASE":
                return 100; // 100kg of base ingredients (like ice)
            case "SWEETENER":
                return 35; // 35l of sweeteners
            case "TOPPING":
                return 15; // 15kg of toppings
            case "POWDER":
                return 10; // 10kg of powders
            case "FLOUR":
                return 50; // 50kg of flour
            case "MEAT":
                return 20; // 20kg of meat products
            case "CAKE_MIX":
                return 25; // 25kg of cake mixes
            default:
                return 15; // Default quantity
        }
    }

    private void createInventory() {
        log.info("Creating inventory records...");

        List<Ingredient> ingredients = ingredientRepository.findAll();
        List<Branch> branches = branchRepository.findAll();

        if (ingredients.isEmpty() || branches.isEmpty()) {
            log.error("No ingredients or branches found. Cannot create inventory records.");
            return;
        }

        List<Inventory> inventories = new ArrayList<>();
        Random random = new Random();
        LocalDateTime now = LocalDateTime.now();

        // Define branch sizes (affects inventory quantity)
        Map<String, Double> branchSizeMultipliers = new HashMap<>();
        branchSizeMultipliers.put("BCoffee Quận 1", 1.5);      // Largest branch
        branchSizeMultipliers.put("BCoffee Thủ Đức", 1.2);     // University area - high volume
        branchSizeMultipliers.put("BCoffee Quận 7", 1.3);      // Business district - high volume
        branchSizeMultipliers.put("BCoffee Tân Bình", 1.0);    // Standard branch
        branchSizeMultipliers.put("BCoffee Bình Thạnh", 0.8);  // Smaller branch

        // Define ingredients that all branches must have
        List<String> essentialIngredients = Arrays.asList(
                "Hạt Cà Phê", "Sữa Tươi", "Sữa Đặc", "Đường Nước", "Đá Viên"
        );

        for (Branch branch : branches) {
            double sizeMultiplier = branchSizeMultipliers.getOrDefault(branch.getBranchName(), 1.0);

            for (Ingredient ingredient : ingredients) {
                String ingredientName = ingredient.getIngredientName();
                String ingredientType = ingredient.getIngredientType();

                // Essential ingredients are in all branches
                boolean isEssential = essentialIngredients.stream()
                        .anyMatch(ingredientName::contains);

                // Skip some non-essential ingredients randomly (30% chance)
                if (!isEssential && random.nextDouble() > 0.7) {
                    continue;
                }

                // Calculate base quantity based on ingredient type
                int branchBaseQuantity = getBranchBaseQuantity(ingredientType);

                // Adjust quantity based on branch size
                int quantity = (int) Math.round(branchBaseQuantity * sizeMultiplier * (0.7 + random.nextDouble() * 0.6));

                // Set expiration date based on ingredient shelf life
                LocalDateTime expireDate = now.plusDays(
                        (long) (ingredient.getShelfLifeDays() * (0.3 + random.nextDouble() * 0.7))
                );

                // Create inventory record
                inventories.add(Inventory.builder()
                        .branch(branch)
                        .ingredient(ingredient)
                        .inventoryQuantity(quantity)
                        .inventoryExpireDate(expireDate)
                        .build());
            }
        }

        inventoryRepository.saveAll(inventories);
        log.info("Created {} inventory records", inventories.size());
    }

    private int getBranchBaseQuantity(String ingredientType) {
        // Return appropriate base quantities for branches (smaller than warehouse quantities)
        switch (ingredientType) {
            case "COFFEE_BEANS":
                return 10; // 10kg of coffee beans
            case "COFFEE_BASE":
                return 5; // 5L of coffee base
            case "DAIRY":
                return 8; // 8L of dairy products
            case "SYRUP":
                return 3; // 3L of syrups
            case "TEA_LEAVES":
                return 4; // 4kg of tea leaves
            case "TEA_TOPPING":
                return 3; // 3kg of tea toppings
            case "FRUIT":
                return 5; // 5kg of fruits
            case "BASE":
                return 15; // 15kg of base ingredients (like ice)
            case "SWEETENER":
                return 6; // 6L of sweeteners
            case "TOPPING":
                return 2; // 2kg of toppings
            case "POWDER":
                return 2; // 2kg of powders
            case "FLOUR":
                return 8; // 8kg of flour
            case "MEAT":
                return 4; // 4kg of meat products
            case "CAKE_MIX":
                return 5; // 5kg of cake mixes
            default:
                return 3; // Default quantity
        }
    }
}
