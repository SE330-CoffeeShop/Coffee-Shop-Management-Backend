package com.se330.coffee_shop_management_backend.service;

import com.se330.coffee_shop_management_backend.dto.request.user.CreateUserRequest;
import com.se330.coffee_shop_management_backend.entity.*;
import com.se330.coffee_shop_management_backend.entity.product.Product;
import com.se330.coffee_shop_management_backend.entity.product.ProductCategory;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import com.se330.coffee_shop_management_backend.repository.*;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductCategoryRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductVariantRepository;
import com.se330.coffee_shop_management_backend.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class DummyDataService implements CommandLineRunner {
    private final RoleService roleService;
    private final UserService userService;
    private final BranchRepository branchRepository;
    private final IngredientRepository ingredientRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final WarehouseRepository warehouseRepository;
    private final CommentRepository commentRepository;
    private final DiscountRepository discountRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final InventoryRepository inventoryRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final SupplierRepository supplierRepository;
    private final NotificationRepository notificationRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ShippingAddressesRepository shippingAddressesRepository;
    private final PaymentMethodsRepository paymentMethodsRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository;
    private final RecipeRepository recipeRepository;
    private final ShiftRepository shiftRepository;
    private final StockRepository stockRepository;
    private final TransferRepository transferRepository;
    private final TransferDetailRepository transferDetailRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roleService.count() == 0) {
            log.info("Creating roles...");
            createRoles();
            log.info("Roles created.");
        }

        if (userService.count() == 0) {
            log.info("Creating users...");
            createUsers();
            log.info("Users created.");
        }

        if (branchRepository.count() == 0) {
            log.info("Creating branches...");
            createBranches();
            log.info("Branches created.");
        }

        if (warehouseRepository.count() == 0) {
            log.info("Creating warehouses...");
            createWarehouses();
            log.info("Warehouses created.");
        }

        if (ingredientRepository.count() == 0) {
            log.info("Creating ingredients...");
            createIngredients();
            log.info("Ingredients created.");
        }

        if (productCategoryRepository.count() == 0) {
            log.info("Creating product categories...");
            createProductCategories();
            log.info("Product categories created.");
        }

        if (productRepository.count() == 0) {
            log.info("Creating products and variants...");
            createProducts();
            log.info("Products and variants created.");
        }

        if (employeeRepository.count() == 0) {
            log.info("Creating employees...");
            createEmployees();
            log.info("Employees created.");
        }

        if (supplierRepository.count() == 0) {
            log.info("Creating suppliers...");
            createSuppliers();
            log.info("Suppliers created.");
        }

        if (inventoryRepository.count() == 0) {
            log.info("Creating inventory...");
            createInventories();
            log.info("Inventory created.");
        }

        if (shiftRepository.count() == 0) {
            log.info("Creating shifts...");
            createShifts();
            log.info("Shifts created.");
        }

        if (shippingAddressesRepository.count() == 0) {
            log.info("Creating shipping addresses...");
            createShippingAddresses();
            log.info("Shipping addresses created.");
        }

        if (paymentMethodsRepository.count() == 0) {
            log.info("Creating payment methods...");
            createPaymentMethods();
            log.info("Payment methods created.");
        }

        if (recipeRepository.count() == 0) {
            log.info("Creating recipes...");
            createRecipes();
            log.info("Recipes created.");
        }

        if (stockRepository.count() == 0) {
            log.info("Creating stocks...");
            createStocks();
            log.info("Stocks created.");
        }

        if (invoiceRepository.count() == 0) {
            log.info("Creating invoices...");
            createInvoicesAndDetails();
            log.info("Invoices created.");
        }

        if (invoiceDetailRepository.count() == 0) {
            log.info("Creating invoice details...");
            createInvoiceDetails();
            log.info("Invoice details created.");
        }

        if (transferRepository.count() == 0) {
            log.info("Creating transfers and transfer details...");
            createTransfers();
            log.info("Transfers and transfer details created.");
        }
//
//        if (orderRepository.count() == 0) {
//            log.info("Creating orders...");
//            createOrdersAndDetails();
//            log.info("Orders created.");
//        }

        if (discountRepository.count() == 0) {
            log.info("Creating discounts...");
            createDiscounts();
            log.info("Discounts created.");
        }

        if (notificationRepository.count() == 0) {
            log.info("Creating notifications...");
            createNotifications();
            log.info("Notifications created.");
        }

//        if (commentRepository.count() == 0) {
//            log.info("Creating comments...");
//            createComments();
//            log.info("Comments created.");
//        }
    }

    /**
     * Create roles.
     */
    private void createRoles() {
        List<Role> roleList = new ArrayList<>();
        roleList.add(Role.builder().name(Constants.RoleEnum.ADMIN).build());
        roleList.add(Role.builder().name(Constants.RoleEnum.CUSTOMER).build());
        roleList.add(Role.builder().name(Constants.RoleEnum.MANAGER).build());
        roleList.add(Role.builder().name(Constants.RoleEnum.EMPLOYEE).build());

        roleService.saveList(roleList);
    }

    /**
     * Create users.
     *
     * @throws BindException Bind exception
     */
    private void createUsers() throws BindException {
        List<String> roleList = new ArrayList<>();
        roleList.add(Constants.RoleEnum.ADMIN.getValue());
        roleList.add(Constants.RoleEnum.CUSTOMER.getValue());
        roleList.add(Constants.RoleEnum.MANAGER.getValue());
        roleList.add(Constants.RoleEnum.EMPLOYEE.getValue());
        String defaultPassword = "P@sswd123.";

        userService.create(CreateUserRequest.builder()
            .email("admin@example.com")
            .password(defaultPassword)
            .name("John")
            .lastName("DOE")
            .roles(roleList)
            .isEmailVerified(true)
            .isBlocked(false)
            .build());

        userService.create(CreateUserRequest.builder()
            .email("user@example.com")
            .password(defaultPassword)
            .name("Jane")
            .lastName("DOE")
            .roles(List.of(roleList.get(1)))
            .isEmailVerified(true)
            .isBlocked(false)
            .build());

        userService.create(CreateUserRequest.builder()
            .email("manager@example.com")
            .password(defaultPassword)
            .name("Mike")
            .lastName("SMITH")
            .roles(List.of(roleList.get(1), roleList.get(2))) // CUSTOMER and MANAGER roles
            .isEmailVerified(true)
            .isBlocked(false)
            .build());

        userService.create(CreateUserRequest.builder()
            .email("employee@example.com")
            .password(defaultPassword)
            .name("Emily")
            .lastName("JOHNSON")
            .roles(List.of(roleList.get(1), roleList.get(3))) // CUSTOMER and EMPLOYEE roles
            .isEmailVerified(true)
            .isBlocked(false)
            .build());
    }

    private void createBranches() {
        List<Branch> branches = new ArrayList<>();

        branches.add(Branch.builder()
                .branchName("Downtown Coffee Shop")
                .branchAddress("123 Main Street, Downtown")
                .branchPhone("(555) 123-4567")
                .branchEmail("downtown@coffeeshop.com")
                .build());

        branches.add(Branch.builder()
                .branchName("University Coffee Shop")
                .branchAddress("456 Campus Drive, University District")
                .branchPhone("(555) 234-5678")
                .branchEmail("university@coffeeshop.com")
                .build());

        branches.add(Branch.builder()
                .branchName("Riverside Coffee Shop")
                .branchAddress("789 River Road, Riverside")
                .branchPhone("(555) 345-6789")
                .branchEmail("riverside@coffeeshop.com")
                .build());

        branches.add(Branch.builder()
                .branchName("Business District Coffee Shop")
                .branchAddress("101 Commerce Ave, Business District")
                .branchPhone("(555) 456-7890")
                .branchEmail("business@coffeeshop.com")
                .build());

        branches.add(Branch.builder()
                .branchName("Shopping Mall Coffee Shop")
                .branchAddress("202 Mall Circle, Shopping Center")
                .branchPhone("(555) 567-8901")
                .branchEmail("mall@coffeeshop.com")
                .build());

        branchRepository.saveAll(branches);
    }

    private void createIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();

        // Coffee beans
        ingredients.add(Ingredient.builder()
                .ingredientName("Arabica Coffee Beans")
                .ingredientDescription("Premium Arabica coffee beans with a smooth, rich flavor")
                .ingredientPrice(new BigDecimal("15.99"))
                .ingredientType("COFFEE_BEANS")
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Robusta Coffee Beans")
                .ingredientDescription("Strong Robusta coffee beans with high caffeine content")
                .ingredientPrice(new BigDecimal("12.50"))
                .ingredientType("COFFEE_BEANS")
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Ethiopian Coffee Beans")
                .ingredientDescription("Exotic Ethiopian beans with fruity and floral notes")
                .ingredientPrice(new BigDecimal("18.75"))
                .ingredientType("COFFEE_BEANS")
                .build());

        // Milk options
        ingredients.add(Ingredient.builder()
                .ingredientName("Whole Milk")
                .ingredientDescription("Fresh whole milk")
                .ingredientPrice(new BigDecimal("3.25"))
                .ingredientType("MILK")
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Almond Milk")
                .ingredientDescription("Unsweetened almond milk")
                .ingredientPrice(new BigDecimal("4.50"))
                .ingredientType("MILK")
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Oat Milk")
                .ingredientDescription("Creamy plant-based oat milk")
                .ingredientPrice(new BigDecimal("4.75"))
                .ingredientType("MILK")
                .build());

        // Syrups
        ingredients.add(Ingredient.builder()
                .ingredientName("Vanilla Syrup")
                .ingredientDescription("Sweet vanilla flavored syrup")
                .ingredientPrice(new BigDecimal("8.99"))
                .ingredientType("SYRUP")
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Caramel Syrup")
                .ingredientDescription("Rich caramel flavored syrup")
                .ingredientPrice(new BigDecimal("9.50"))
                .ingredientType("SYRUP")
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Hazelnut Syrup")
                .ingredientDescription("Nutty hazelnut flavored syrup")
                .ingredientPrice(new BigDecimal("9.25"))
                .ingredientType("SYRUP")
                .build());

        // Toppings
        ingredients.add(Ingredient.builder()
                .ingredientName("Whipped Cream")
                .ingredientDescription("Fresh whipped cream topping")
                .ingredientPrice(new BigDecimal("2.50"))
                .ingredientType("TOPPING")
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Chocolate Sprinkles")
                .ingredientDescription("Sweet chocolate sprinkles")
                .ingredientPrice(new BigDecimal("1.75"))
                .ingredientType("TOPPING")
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Cinnamon Powder")
                .ingredientDescription("Ground cinnamon for sprinkling")
                .ingredientPrice(new BigDecimal("1.50"))
                .ingredientType("TOPPING")
                .build());

        // Tea varieties
        ingredients.add(Ingredient.builder()
                .ingredientName("Green Tea Leaves")
                .ingredientDescription("Premium green tea leaves")
                .ingredientPrice(new BigDecimal("12.75"))
                .ingredientType("TEA")
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Earl Grey Tea")
                .ingredientDescription("Fragrant Earl Grey tea with bergamot")
                .ingredientPrice(new BigDecimal("10.50"))
                .ingredientType("TEA")
                .build());

        ingredientRepository.saveAll(ingredients);
    }

    private void createProductCategories() {
        List<ProductCategory> categories = new ArrayList<>();

        categories.add(ProductCategory.builder()
                .categoryName("Hot Coffee")
                .categoryDescription("Traditional hot coffee drinks including espresso, americano, and lattes")
                .build());

        categories.add(ProductCategory.builder()
                .categoryName("Cold Coffee")
                .categoryDescription("Refreshing cold coffee options including iced coffees, cold brew, and frappuccinos")
                .build());

        categories.add(ProductCategory.builder()
                .categoryName("Tea")
                .categoryDescription("Various tea offerings including black, green, and herbal varieties")
                .build());

        categories.add(ProductCategory.builder()
                .categoryName("Pastries")
                .categoryDescription("Freshly baked pastries including croissants, muffins, and cookies")
                .build());

        categories.add(ProductCategory.builder()
                .categoryName("Sandwiches")
                .categoryDescription("Handcrafted sandwiches made with premium ingredients")
                .build());

        categories.add(ProductCategory.builder()
                .categoryName("Desserts")
                .categoryDescription("Sweet treats including cakes, brownies, and specialty desserts")
                .build());

        productCategoryRepository.saveAll(categories);
    }

    private void createWarehouses() {
        List<Warehouse> warehouses = new ArrayList<>();

        warehouses.add(Warehouse.builder()
                .warehouseName("Central Distribution Center")
                .warehousePhone("(555) 111-2222")
                .warehouseEmail("central@coffeeshop-distribution.com")
                .warehouseAddress("1000 Supply Chain Road, Industrial District")
                .build());

        warehouses.add(Warehouse.builder()
                .warehouseName("North Regional Warehouse")
                .warehousePhone("(555) 222-3333")
                .warehouseEmail("north@coffeeshop-distribution.com")
                .warehouseAddress("250 Northern Parkway, Logistics Park")
                .build());

        warehouses.add(Warehouse.builder()
                .warehouseName("East Regional Warehouse")
                .warehousePhone("(555) 333-4444")
                .warehouseEmail("east@coffeeshop-distribution.com")
                .warehouseAddress("500 Eastern Boulevard, Commerce Zone")
                .build());

        warehouses.add(Warehouse.builder()
                .warehouseName("Specialty Goods Warehouse")
                .warehousePhone("(555) 444-5555")
                .warehouseEmail("specialty@coffeeshop-distribution.com")
                .warehouseAddress("75 Gourmet Lane, Artisan District")
                .build());

        warehouseRepository.saveAll(warehouses);
    }

    private void createComments() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            log.error("Cannot create comments: No products found");
            return;
        }

        List<Comment> comments = new ArrayList<>();
        Random random = new Random();

        // Generate positive comments for each product
        for (Product product : products) {
            // Generate 3-15 comments per product
            int commentCount = 3 + random.nextInt(13);

            for (int i = 0; i < commentCount; i++) {
                // Generate a rating between 0.01 and 0.99
                // This respects the precision 2, scale 2 constraint
                BigDecimal rating = BigDecimal.valueOf(0.01 + (random.nextDouble() * 0.98));

                Comment comment = Comment.builder()
                        .commentContent(generateRandomPositiveComment())
                        .commentLeft(i)
                        .commentRight(i + 1)
                        .commentIsDeleted(false)
                        .commentRating(rating)
                        .product(product)
                        .build();

                comments.add(comment);
            }

            // Update product rating average and comment count
            product.setProductCommentCount(commentCount);

            // Calculate average rating (would normally be 0.01-0.99 for dummy data)
            BigDecimal avgRating = BigDecimal.valueOf(0.50 + (random.nextDouble() * 0.49));
            product.setProductRatingsAverage(avgRating);
        }

        commentRepository.saveAll(comments);
        productRepository.saveAll(products);
    }

    private String generateRandomPositiveComment() {
        String[] comments = {
                "Great product, I love it!",
                "Exceeded my expectations.",
                "Very tasty and refreshing.",
                "Will definitely order again.",
                "Perfect balance of flavors.",
                "The quality is amazing.",
                "Reasonable price for the quality.",
                "My favorite coffee shop product.",
                "Fast delivery and good packaging.",
                "Authentic taste, highly recommend."
        };

        return comments[new Random().nextInt(comments.length)];
    }

    private void createDiscounts() {
        List<Discount> discounts = new ArrayList<>();

        // Get branches for association
        List<Branch> branches = branchRepository.findAll();
        Branch defaultBranch = branches.isEmpty() ? null : branches.get(0);

        LocalDateTime now = LocalDateTime.now();

        discounts.add(Discount.builder()
                .discountName("Summer Special")
                .discountDescription("Special discount for the summer season")
                .discountType("PERCENTAGE")
                .discountValue(new BigDecimal("15.00"))
                .discountCode("SUMMER15")
                .discountStartDate(now)
                .discountEndDate(now.plusMonths(3))
                .discountMaxUsers(1000)
                .discountUserCount(0)
                .discountMaxPerUser(1)
                .discountMinOrderValue(20)
                .discountIsActive(true)
                .branch(defaultBranch)
                .build());

        discounts.add(Discount.builder()
                .discountName("Welcome Discount")
                .discountDescription("Discount for new customers")
                .discountType("PERCENTAGE")
                .discountValue(new BigDecimal("10.00"))
                .discountCode("WELCOME10")
                .discountStartDate(now)
                .discountEndDate(now.plusYears(1))
                .discountMaxUsers(5000)
                .discountUserCount(0)
                .discountMaxPerUser(1)
                .discountMinOrderValue(0)
                .discountIsActive(true)
                .branch(defaultBranch)
                .build());

        discounts.add(Discount.builder()
                .discountName("Loyalty Reward")
                .discountDescription("Special discount for loyal customers")
                .discountType("FIXED")
                .discountValue(new BigDecimal("5.00"))
                .discountCode("LOYAL5")
                .discountStartDate(now)
                .discountEndDate(now.plusMonths(6))
                .discountMaxUsers(2000)
                .discountUserCount(0)
                .discountMaxPerUser(5)
                .discountMinOrderValue(15)
                .discountIsActive(true)
                .branch(branches.size() > 1 ? branches.get(1) : defaultBranch)
                .build());

        discounts.add(Discount.builder()
                .discountName("Holiday Special")
                .discountDescription("Special discount for the holiday season")
                .discountType("PERCENTAGE")
                .discountValue(new BigDecimal("20.00"))
                .discountCode("HOLIDAY20")
                .discountStartDate(now.withMonth(11).withDayOfMonth(20))
                .discountEndDate(now.withMonth(12).withDayOfMonth(31))
                .discountMaxUsers(3000)
                .discountUserCount(0)
                .discountMaxPerUser(2)
                .discountMinOrderValue(25)
                .discountIsActive(true)
                .branch(branches.size() > 2 ? branches.get(2) : defaultBranch)
                .build());

        int unlimited = 10000;

        discounts.add(Discount.builder()
                .discountName("Weekend Deal")
                .discountDescription("Special discount for weekend orders")
                .discountType("PERCENTAGE")
                .discountValue(new BigDecimal("12.50"))
                .discountCode("WEEKEND12")
                .discountStartDate(now)
                .discountEndDate(now.plusMonths(12))
                .discountMaxUsers(unlimited)
                .discountUserCount(0)
                .discountMaxPerUser(unlimited)
                .discountMinOrderValue(30)
                .discountIsActive(true)
                .branch(branches.size() > 3 ? branches.get(3) : defaultBranch)
                .build());

        discountRepository.saveAll(discounts);
    }

    private void createEmployees() {
        // Get branches for association
        List<Branch> branches = branchRepository.findAll();
        if (branches.isEmpty()) {
            log.error("Cannot create employees: No branches found");
            return;
        }

        // Get users that will be associated with employees
        List<User> users = userRepository.findAll();
        List<User> availableUsers = users.stream()
                .filter(user -> user.getEmployee() == null)
                .filter(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName().equals(Constants.RoleEnum.EMPLOYEE) ||
                                role.getName().equals(Constants.RoleEnum.MANAGER)))
                .toList();

        if (availableUsers.isEmpty()) {
            log.error("Cannot create employees: No eligible users found");
            return;
        }

        List<Employee> employees = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        // Create employees with available users
        for (int i = 0; i < Math.min(availableUsers.size(), branches.size()); i++) {
            employees.add(Employee.builder()
                    .employeePosition(i == 0 ? "Manager" : "Barista")
                    .employeeDepartment(i == 0 ? "Management" : "Service")
                    .employeeHireDate(now.minusMonths(i * 3L)) // Different hire dates
                    .branch(branches.get(i % branches.size()))
                    .user(availableUsers.get(i))
                    .build());
        }

        // Create additional employees if we have more branches than users
        if (branches.size() > availableUsers.size()) {
            String[] positions = {"Barista", "Cashier", "Shift Supervisor", "Assistant Manager"};
            String[] departments = {"Service", "Front Desk", "Operations", "Management"};

            for (int i = availableUsers.size(); i < branches.size(); i++) {
                employees.add(Employee.builder()
                        .employeePosition(positions[i % positions.length])
                        .employeeDepartment(departments[i % departments.length])
                        .employeeHireDate(now.minusMonths(i * 2L))
                        .branch(branches.get(i))
                        .user(null) // These employees don't have user accounts yet
                        .build());
            }
        }

        employeeRepository.saveAll(employees);
    }

    private void createInventories() {
        // Get branches and ingredients for association
        List<Branch> branches = branchRepository.findAll();
        List<Ingredient> ingredients = ingredientRepository.findAll();

        if (branches.isEmpty() || ingredients.isEmpty()) {
            log.error("Cannot create inventories: Branches or ingredients not found");
            return;
        }

        List<Inventory> inventories = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        // For each branch, create inventory entries for each ingredient
        for (Branch branch : branches) {
            for (Ingredient ingredient : ingredients) {
                // Generate different quantities based on ingredient type
                int quantity = switch (ingredient.getIngredientType()) {
                    case "COFFEE_BEANS" -> 20 + (int) (Math.random() * 30); // 20-50 kg
                    case "MILK" -> 30 + (int) (Math.random() * 20); // 30-50 liters
                    case "SYRUP" -> 5 + (int) (Math.random() * 10);  // 5-15 bottles
                    case "TOPPING" -> 10 + (int) (Math.random() * 15); // 10-25 packages
                    case "TEA" -> 15 + (int) (Math.random() * 20); // 15-35 packages
                    default -> 10 + (int) (Math.random() * 20); // 10-30 units
                };

                // Generate expiry date based on ingredient type
                LocalDateTime expiryDate = switch (ingredient.getIngredientType()) {
                    case "COFFEE_BEANS" -> now.plusMonths(6 + (int) (Math.random() * 6)); // 6-12 months
                    case "MILK" -> now.plusDays(7 + (int) (Math.random() * 7)); // 7-14 days
                    case "SYRUP" -> now.plusMonths(3 + (int) (Math.random() * 3)); // 3-6 months
                    case "TOPPING" -> now.plusMonths(2 + (int) (Math.random() * 4)); // 2-6 months
                    case "TEA" -> now.plusMonths(12 + (int) (Math.random() * 12)); // 12-24 months
                    default -> now.plusMonths(3); // 3 months
                };

                inventories.add(Inventory.builder()
                        .inventoryQuantity(quantity)
                        .inventoryExpireDate(expiryDate)
                        .branch(branch)
                        .ingredient(ingredient)
                        .build());
            }
        }

        inventoryRepository.saveAll(inventories);
    }

    private void createInvoicesAndDetails() {
        // Get dependencies for association
        List<Warehouse> warehouses = warehouseRepository.findAll();
        List<Supplier> suppliers = supplierRepository.findAll();
        List<Ingredient> ingredients = ingredientRepository.findAll();

        if (warehouses.isEmpty() || suppliers.isEmpty() || ingredients.isEmpty()) {
            log.error("Cannot create invoices: Warehouses, suppliers, or ingredients not found");
            return;
        }

        List<Invoice> invoices = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        // Create several invoices
        for (int i = 0; i < Math.min(warehouses.size(), suppliers.size()); i++) {
            Warehouse warehouse = warehouses.get(i % warehouses.size());
            Supplier supplier = suppliers.get(i % suppliers.size());

            // Create tracking number in the format INV-YYYYMMDD-XXXX
            String trackingNumber = String.format("INV-%tY%<tm%<td-%04d", now, 1000 + i);

            Invoice invoice = Invoice.builder()
                    .invoiceDescription("Supply order for " + warehouse.getWarehouseName())
                    .invoiceTrackingNumber(trackingNumber)
                    .invoiceTransferTotalCost(BigDecimal.ZERO) // Will be calculated after details are added
                    .warehouse(warehouse)
                    .supplier(supplier)
                    .build();

            // Save invoice to get ID for details
            invoice = invoiceRepository.save(invoice);

            // Create invoice details for this invoice
            List<InvoiceDetail> invoiceDetails = new ArrayList<>();
            BigDecimal totalCost = BigDecimal.ZERO;

            // Select a subset of ingredients for this invoice (between 3-8 items)
            int itemCount = 3 + (int)(Math.random() * 6);
            for (int j = 0; j < itemCount && j < ingredients.size(); j++) {
                Ingredient ingredient = ingredients.get((i + j) % ingredients.size());

                // Randomize quantities based on ingredient type
                int quantity = switch (ingredient.getIngredientType()) {
                    case "COFFEE_BEANS" -> 10 + (int)(Math.random() * 40); // 10-50 kg
                    case "MILK" -> 20 + (int)(Math.random() * 30); // 20-50 liters
                    case "SYRUP" -> 5 + (int)(Math.random() * 15); // 5-20 bottles
                    case "TOPPING" -> 10 + (int)(Math.random() * 20); // 10-30 packages
                    case "TEA" -> 5 + (int)(Math.random() * 25); // 5-30 packages
                    default -> 5 + (int)(Math.random() * 15); // 5-20 units
                };

                // Unit price is the ingredient price
                int unitPrice = ingredient.getIngredientPrice().intValue();

                InvoiceDetail invoiceDetail = InvoiceDetail.builder()
                        .invoice(invoice)
                        .ingredient(ingredient)
                        .invoiceDetailQuantity(quantity)
                        .invoiceDetailUnit(unitPrice)
                        .build();

                invoiceDetails.add(invoiceDetail);

                // Calculate item cost and add to total
                BigDecimal itemCost = ingredient.getIngredientPrice().multiply(BigDecimal.valueOf(quantity));
                totalCost = totalCost.add(itemCost);
            }

            // Update invoice total cost
            invoice.setInvoiceTransferTotalCost(totalCost);
            invoice.setInvoiceDetails(invoiceDetails);
            invoices.add(invoice);
        }

        // Save all invoices with their details
        invoiceRepository.saveAll(invoices);
    }

    private void createNotifications() {
        // Get users for sender and receiver associations
        List<User> users = userRepository.findAll();
        if (users.size() < 2) {
            log.error("Cannot create notifications: Not enough users found");
            return;
        }

        List<Notification> notifications = new ArrayList<>();

        // Create system notifications (sender is admin user)
        User adminUser = users.stream()
                .filter(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName().equals(Constants.RoleEnum.ADMIN)))
                .findFirst()
                .orElse(users.get(0));

        // Create different types of notifications for different users
        for (int i = 0; i < users.size(); i++) {
            User receiver = users.get(i);

            if (receiver.equals(adminUser)) continue;

            // Order status notification
            notifications.add(Notification.builder()
                    .notificationType(Constants.NotificationTypeEnum.SYSTEM) // Order status notification type
                    .notificationContent("Your order #ORD-2023" + (1000 + i) + " has been confirmed and is being processed.")
                    .sender(adminUser)
                    .receiver(receiver)
                    .build());

            // Promotional notification
            notifications.add(Notification.builder()
                    .notificationType(Constants.NotificationTypeEnum.SYSTEM) // Promotional notification type
                    .notificationContent("Enjoy 15% off on all beverages this weekend with code WEEKEND15!")
                    .sender(adminUser)
                    .receiver(receiver)
                    .build());

            // Account notification
            notifications.add(Notification.builder()
                    .notificationType(Constants.NotificationTypeEnum.SYSTEM) // Account notification type
                    .notificationContent("Your account profile has been updated successfully.")
                    .sender(adminUser)
                    .receiver(receiver)
                    .build());
        }

        // Create a few user-to-user notifications for employees/managers
        List<User> staffUsers = users.stream()
                .filter(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName().equals(Constants.RoleEnum.EMPLOYEE) ||
                                role.getName().equals(Constants.RoleEnum.MANAGER)))
                .toList();

        if (staffUsers.size() >= 2) {
            for (int i = 0; i < staffUsers.size() - 1; i++) {
                User sender = staffUsers.get(i);
                User receiver = staffUsers.get((i + 1) % staffUsers.size());

                notifications.add(Notification.builder()
                        .notificationType(Constants.NotificationTypeEnum.SYSTEM) // Staff communication notification type
                        .notificationContent("Please check the updated shift schedule for next week.")
                        .sender(sender)
                        .receiver(receiver)
                        .build());
            }
        }

        notificationRepository.saveAll(notifications);
    }

    private void createOrderDetails(Order order, List<ProductVariant> allProductVariants) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        Random random = new Random();

        // Each order has 1-5 items
        int itemCount = 1 + random.nextInt(5);

        // Select random product variants for this order
        List<ProductVariant> orderProductVariants = new ArrayList<>(allProductVariants);
        Collections.shuffle(orderProductVariants);
        orderProductVariants = orderProductVariants.subList(0, Math.min(itemCount, orderProductVariants.size()));

        for (ProductVariant productVariant : orderProductVariants) {
            // Create reasonable quantities and unit prices
            int quantity = 1 + random.nextInt(3); // 1-3 items
            int unitPrice = productVariant.getVariantPrice().intValue();

            OrderDetail orderDetail = OrderDetail.builder()
                    .orderDetailQuantity(quantity)
                    .orderDetailUnitPrice(unitPrice)
                    .productVariant(productVariant)
                    .order(order)
                    .build();

            orderDetails.add(orderDetail);
        }

        orderDetailRepository.saveAll(orderDetails);
    }

    private void createProducts() {
        // Get product categories for association
        List<ProductCategory> categories = productCategoryRepository.findAll();
        if (categories.isEmpty()) {
            log.error("Cannot create products: No product categories found");
            return;
        }

        List<Product> products = new ArrayList<>();

        // Create Hot Coffee products
        ProductCategory hotCoffeeCategory = categories.stream()
                .filter(c -> c.getCategoryName().equals("Hot Coffee"))
                .findFirst()
                .orElse(categories.get(0));

        products.add(Product.builder()
                .productName("Classic Espresso")
                .productThumb("/images/products/espresso.jpg")
                .productDescription("Our signature espresso is bold and full-bodied with a rich crema, extracted to perfection from freshly ground premium coffee beans.")
                .productPrice(new BigDecimal("3.50"))
                .productSlug("classic-espresso")
                .productCommentCount(5)
                .productRatingsAverage(new BigDecimal("4.7"))
                .productIsPublished(true)
                .productIsDeleted(false)
                .productCategory(hotCoffeeCategory)
                .build());

        products.add(Product.builder()
                .productName("Creamy Latte")
                .productThumb("/images/products/latte.jpg")
                .productDescription("Smooth espresso combined with steamed milk and topped with a thin layer of foam for a delicious, balanced coffee experience.")
                .productPrice(new BigDecimal("4.25"))
                .productSlug("creamy-latte")
                .productCommentCount(7)
                .productRatingsAverage(new BigDecimal("4.5"))
                .productIsPublished(true)
                .productIsDeleted(false)
                .productCategory(hotCoffeeCategory)
                .build());

        products.add(Product.builder()
                .productName("Cappuccino")
                .productThumb("/images/products/cappuccino.jpg")
                .productDescription("Equal parts espresso, steamed milk and velvety foam, creating the perfect harmony of flavors and textures.")
                .productPrice(new BigDecimal("4.00"))
                .productSlug("cappuccino")
                .productCommentCount(6)
                .productRatingsAverage(new BigDecimal("4.6"))
                .productIsPublished(true)
                .productIsDeleted(false)
                .productCategory(hotCoffeeCategory)
                .build());

        // Create Cold Coffee products
        ProductCategory coldCoffeeCategory = categories.stream()
                .filter(c -> c.getCategoryName().equals("Cold Coffee"))
                .findFirst()
                .orElse(categories.get(0));

        products.add(Product.builder()
                .productName("Iced Coffee")
                .productThumb("/images/products/iced-coffee.jpg")
                .productDescription("Our signature coffee blend served chilled over ice for a refreshing pick-me-up any time of day.")
                .productPrice(new BigDecimal("3.75"))
                .productSlug("iced-coffee")
                .productCommentCount(8)
                .productRatingsAverage(new BigDecimal("4.4"))
                .productIsPublished(true)
                .productIsDeleted(false)
                .productCategory(coldCoffeeCategory)
                .build());

        products.add(Product.builder()
                .productName("Cold Brew")
                .productThumb("/images/products/cold-brew.jpg")
                .productDescription("Steeped for 12 hours in cold water, our cold brew offers a smooth, full-bodied flavor with low acidity and naturally sweet notes.")
                .productPrice(new BigDecimal("4.50"))
                .productSlug("cold-brew")
                .productCommentCount(9)
                .productRatingsAverage(new BigDecimal("4.8"))
                .productIsPublished(true)
                .productIsDeleted(false)
                .productCategory(coldCoffeeCategory)
                .build());

        // Create Tea products
        ProductCategory teaCategory = categories.stream()
                .filter(c -> c.getCategoryName().equals("Tea"))
                .findFirst()
                .orElse(categories.get(0));

        products.add(Product.builder()
                .productName("Earl Grey Tea")
                .productThumb("/images/products/earl-grey.jpg")
                .productDescription("A classic black tea infused with bergamot, offering a citrusy aroma and a sophisticated flavor profile.")
                .productPrice(new BigDecimal("3.25"))
                .productSlug("earl-grey-tea")
                .productCommentCount(4)
                .productRatingsAverage(new BigDecimal("4.3"))
                .productIsPublished(true)
                .productIsDeleted(false)
                .productCategory(teaCategory)
                .build());

        products.add(Product.builder()
                .productName("Green Tea")
                .productThumb("/images/products/green-tea.jpg")
                .productDescription("Delicate and refreshing, our green tea offers a perfect balance of subtle sweetness and gentle astringency.")
                .productPrice(new BigDecimal("3.00"))
                .productSlug("green-tea")
                .productCommentCount(3)
                .productRatingsAverage(new BigDecimal("4.2"))
                .productIsPublished(true)
                .productIsDeleted(false)
                .productCategory(teaCategory)
                .build());

        // Create Pastries
        ProductCategory pastriesCategory = categories.stream()
                .filter(c -> c.getCategoryName().equals("Pastries"))
                .findFirst()
                .orElse(categories.get(0));

        products.add(Product.builder()
                .productName("Butter Croissant")
                .productThumb("/images/products/croissant.jpg")
                .productDescription("Flaky, buttery layers of hand-rolled pastry, baked to golden perfection each morning.")
                .productPrice(new BigDecimal("2.75"))
                .productSlug("butter-croissant")
                .productCommentCount(10)
                .productRatingsAverage(new BigDecimal("4.9"))
                .productIsPublished(true)
                .productIsDeleted(false)
                .productCategory(pastriesCategory)
                .build());

        products.add(Product.builder()
                .productName("Chocolate Muffin")
                .productThumb("/images/products/chocolate-muffin.jpg")
                .productDescription("Rich, moist chocolate muffin with chocolate chips throughout, perfect with your morning coffee.")
                .productPrice(new BigDecimal("3.25"))
                .productSlug("chocolate-muffin")
                .productCommentCount(6)
                .productRatingsAverage(new BigDecimal("4.5"))
                .productIsPublished(true)
                .productIsDeleted(false)
                .productCategory(pastriesCategory)
                .build());

        // Save products to get IDs before creating variants
        productRepository.saveAll(products);

        // Now create product variants for each product
        createProductVariants(products);
    }

    private void createProductVariants(List<Product> products) {
        List<ProductVariant> productVariants = new ArrayList<>();

        for (Product product : products) {
            // Get discounts for association (use a few random ones)
            List<Discount> allDiscounts = discountRepository.findAll();
            List<Discount> selectedDiscounts = new ArrayList<>();

            if (!allDiscounts.isEmpty()) {
                // Maybe apply a discount (30% chance)
                if (Math.random() < 0.3) {
                    // Pick a random discount
                    selectedDiscounts.add(allDiscounts.get((int)(Math.random() * allDiscounts.size())));
                }
            }

            // For drinks (coffee and tea), create size variants
            if (product.getProductCategory().getCategoryName().equals("Hot Coffee") ||
                    product.getProductCategory().getCategoryName().equals("Cold Coffee") ||
                    product.getProductCategory().getCategoryName().equals("Tea")) {

                // Small size
                ProductVariant smallVariant = ProductVariant.builder()
                        .variantTierIdx("small")
                        .variantDefault(false)
                        .variantSlug(product.getProductSlug() + "-small")
                        .variantSort(1)
                        .variantPrice(product.getProductPrice().longValue() * 100) // Convert to cents
                        .variantStock(50 + (int)(Math.random() * 50))
                        .variantIsPublished(true)
                        .variantIsDeleted(false)
                        .product(product)
                        .discounts(new ArrayList<>(selectedDiscounts))
                        .build();
                productVariants.add(smallVariant);

                // Medium size (default)
                ProductVariant mediumVariant = ProductVariant.builder()
                        .variantTierIdx("medium")
                        .variantDefault(true)
                        .variantSlug(product.getProductSlug() + "-medium")
                        .variantSort(2)
                        .variantPrice((product.getProductPrice().longValue() + 1) * 100) // Medium costs $1 more
                        .variantStock(80 + (int)(Math.random() * 50))
                        .variantIsPublished(true)
                        .variantIsDeleted(false)
                        .product(product)
                        .discounts(new ArrayList<>(selectedDiscounts))
                        .build();
                productVariants.add(mediumVariant);

                // Large size
                ProductVariant largeVariant = ProductVariant.builder()
                        .variantTierIdx("large")
                        .variantDefault(false)
                        .variantSlug(product.getProductSlug() + "-large")
                        .variantSort(3)
                        .variantPrice((product.getProductPrice().longValue() + 2) * 100) // Large costs $2 more
                        .variantStock(40 + (int)(Math.random() * 30))
                        .variantIsPublished(true)
                        .variantIsDeleted(false)
                        .product(product)
                        .discounts(new ArrayList<>(selectedDiscounts))
                        .build();
                productVariants.add(largeVariant);

                // For some coffees, add extra shot option
                if (product.getProductCategory().getCategoryName().contains("Coffee") && Math.random() < 0.5) {
                    ProductVariant extraShotVariant = ProductVariant.builder()
                            .variantTierIdx("extra_shot")
                            .variantDefault(false)
                            .variantSlug(product.getProductSlug() + "-extra-shot")
                            .variantSort(4)
                            .variantPrice((product.getProductPrice().longValue() + 1) * 100) // Extra shot costs $1 more
                            .variantStock(30 + (int)(Math.random() * 20))
                            .variantIsPublished(true)
                            .variantIsDeleted(false)
                            .product(product)
                            .discounts(new ArrayList<>(selectedDiscounts))
                            .build();
                    productVariants.add(extraShotVariant);
                }
            } else {
                // For pastries and other food items, create a single default variant
                ProductVariant defaultVariant = ProductVariant.builder()
                        .variantTierIdx("standard")
                        .variantDefault(true)
                        .variantSlug(product.getProductSlug() + "-standard")
                        .variantSort(1)
                        .variantPrice(product.getProductPrice().longValue() * 100) // Convert to cents
                        .variantStock(30 + (int)(Math.random() * 70))
                        .variantIsPublished(true)
                        .variantIsDeleted(false)
                        .product(product)
                        .discounts(new ArrayList<>(selectedDiscounts))
                        .build();
                productVariants.add(defaultVariant);
            }
        }

        productVariantRepository.saveAll(productVariants);
    }

    private void createPaymentMethods() {
        // Get users for association
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            log.error("Cannot create payment methods: No users found");
            return;
        }

        List<PaymentMethods> paymentMethods = new ArrayList<>();

        // Create various payment methods for each user
        for (User user : users) {
            // Credit Card
            paymentMethods.add(PaymentMethods.builder()
                    .methodType("Credit Card")
                    .methodDetails("VISA **** **** **** " + (1000 + new Random().nextInt(9000)))
                    .methodIsDefault(true)
                    .user(user)
                    .build());

            // Debit Card
            paymentMethods.add(PaymentMethods.builder()
                    .methodType("Debit Card")
                    .methodDetails("Mastercard **** **** **** " + (1000 + new Random().nextInt(9000)))
                    .methodIsDefault(false)
                    .user(user)
                    .build());

            // Digital Wallet - only for some users (70% chance)
            if (Math.random() < 0.7) {
                paymentMethods.add(PaymentMethods.builder()
                        .methodType("Digital Wallet")
                        .methodDetails("PayPal - " + user.getEmail())
                        .methodIsDefault(false)
                        .user(user)
                        .build());
            }
        }

        paymentMethodsRepository.saveAll(paymentMethods);
    }

    private void createRecipes() {
        // Get necessary entities for association
        List<ProductVariant> productVariants = productVariantRepository.findAll();
        List<Ingredient> ingredients = ingredientRepository.findAll();

        if (productVariants.isEmpty() || ingredients.isEmpty()) {
            log.error("Cannot create recipes: No product variants or ingredients found");
            return;
        }

        List<Recipe> recipes = new ArrayList<>();
        Random random = new Random();

        // Map ingredient types
        List<Ingredient> coffeeBeans = ingredients.stream()
                .filter(i -> i.getIngredientType().equals("Coffee Beans"))
                .toList();

        List<Ingredient> milks = ingredients.stream()
                .filter(i -> i.getIngredientType().equals("Milk"))
                .toList();

        List<Ingredient> syrups = ingredients.stream()
                .filter(i -> i.getIngredientType().equals("Syrup"))
                .toList();

        List<Ingredient> toppings = ingredients.stream()
                .filter(i -> i.getIngredientType().equals("Topping"))
                .toList();

        List<Ingredient> teas = ingredients.stream()
                .filter(i -> i.getIngredientType().equals("Tea"))
                .toList();

        // Create recipes for each product variant
        for (ProductVariant variant : productVariants) {
            String category = variant.getProduct().getProductCategory().getCategoryName();
            String variantSize = variant.getVariantTierIdx();

            // Base recipe quantities by size
            int sizeMultiplier = 1;
            if ("medium".equals(variantSize)) {
                sizeMultiplier = 2;
            } else if ("large".equals(variantSize)) {
                sizeMultiplier = 3;
            }

            // For coffee products
            if (category.equals("Hot Coffee") || category.equals("Cold Coffee")) {
                // Add coffee beans (all coffee products need beans)
                if (!coffeeBeans.isEmpty()) {
                    Ingredient bean = coffeeBeans.get(random.nextInt(coffeeBeans.size()));
                    recipes.add(Recipe.builder()
                            .recipeQuantity(10 * sizeMultiplier)
                            .recipeUnit("g")
                            .recipeIsTopping(false)
                            .ingredient(bean)
                            .productVariant(variant)
                            .build());
                }

                // Most coffee drinks need milk (except black coffee/espresso)
                if (!milks.isEmpty() && !variant.getProduct().getProductName().toLowerCase().contains("espresso")) {
                    Ingredient milk = milks.get(random.nextInt(milks.size()));
                    recipes.add(Recipe.builder()
                            .recipeQuantity(50 * sizeMultiplier)
                            .recipeUnit("ml")
                            .recipeIsTopping(false)
                            .ingredient(milk)
                            .productVariant(variant)
                            .build());
                }

                // Some coffees have syrup (30% chance unless it's flavored in name)
                if (!syrups.isEmpty() && (Math.random() < 0.3 ||
                        variant.getProduct().getProductName().toLowerCase().contains("vanilla") ||
                        variant.getProduct().getProductName().toLowerCase().contains("caramel"))) {
                    Ingredient syrup = syrups.get(random.nextInt(syrups.size()));
                    recipes.add(Recipe.builder()
                            .recipeQuantity(5 * sizeMultiplier)
                            .recipeUnit("ml")
                            .recipeIsTopping(false)
                            .ingredient(syrup)
                            .productVariant(variant)
                            .build());
                }

                // Some coffees have toppings (50% chance)
                if (!toppings.isEmpty() && Math.random() < 0.5) {
                    Ingredient topping = toppings.get(random.nextInt(toppings.size()));
                    recipes.add(Recipe.builder()
                            .recipeQuantity(5 * sizeMultiplier)
                            .recipeUnit("g")
                            .recipeIsTopping(true)
                            .ingredient(topping)
                            .productVariant(variant)
                            .build());
                }
            }

            // For tea products
            else if (category.equals("Tea")) {
                // Add tea leaves
                if (!teas.isEmpty()) {
                    Ingredient tea = teas.get(random.nextInt(teas.size()));
                    recipes.add(Recipe.builder()
                            .recipeQuantity(2 * sizeMultiplier)
                            .recipeUnit("bag")
                            .recipeIsTopping(false)
                            .ingredient(tea)
                            .productVariant(variant)
                            .build());
                }

                // Some teas have milk (30% chance)
                if (!milks.isEmpty() && Math.random() < 0.3) {
                    Ingredient milk = milks.get(random.nextInt(milks.size()));
                    recipes.add(Recipe.builder()
                            .recipeQuantity(30 * sizeMultiplier)
                            .recipeUnit("ml")
                            .recipeIsTopping(false)
                            .ingredient(milk)
                            .productVariant(variant)
                            .build());
                }

                // Some teas have syrup (20% chance)
                if (!syrups.isEmpty() && Math.random() < 0.2) {
                    Ingredient syrup = syrups.get(random.nextInt(syrups.size()));
                    recipes.add(Recipe.builder()
                            .recipeQuantity(3 * sizeMultiplier)
                            .recipeUnit("ml")
                            .recipeIsTopping(false)
                            .ingredient(syrup)
                            .productVariant(variant)
                            .build());
                }
            }

            // For pastries (no recipe as they are pre-made and not assembled from ingredients)
            // If needed, you could add recipes for pastries too
        }

        recipeRepository.saveAll(recipes);
    }

    private void createShifts() {
        // Get employees for association
        List<Employee> employees = employeeRepository.findAll();
        if (employees.isEmpty()) {
            log.error("Cannot create shifts: No employees found");
            return;
        }

        List<Shift> shifts = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        Random random = new Random();

        // Create shifts for the last 14 days (2 weeks)
        for (int day = 0; day < 14; day++) {
            LocalDateTime currentDay = now.minusDays(day);

            // Morning shift (8am - 2pm)
            LocalDateTime morningStart = LocalDateTime.of(
                    currentDay.getYear(),
                    currentDay.getMonth(),
                    currentDay.getDayOfMonth(),
                    8, 0);
            LocalDateTime morningEnd = morningStart.plusHours(6);

            // Afternoon shift (2pm - 8pm)
            LocalDateTime afternoonStart = LocalDateTime.of(
                    currentDay.getYear(),
                    currentDay.getMonth(),
                    currentDay.getDayOfMonth(),
                    14, 0);
            LocalDateTime afternoonEnd = afternoonStart.plusHours(6);

            // Evening shift (8pm - 12am) for some locations
            LocalDateTime eveningStart = LocalDateTime.of(
                    currentDay.getYear(),
                    currentDay.getMonth(),
                    currentDay.getDayOfMonth(),
                    20, 0);
            LocalDateTime eveningEnd = eveningStart.plusHours(4);

            // Assign random employees to each shift
            // Ensure each employee doesn't have more than one shift per day
            List<Employee> availableEmployees = new ArrayList<>(employees);
            Collections.shuffle(availableEmployees, random);

            // Morning shift - assign 30-50% of available employees
            int morningEmployeeCount = Math.max(1, availableEmployees.size() * (30 + random.nextInt(21)) / 100);
            for (int i = 0; i < morningEmployeeCount && i < availableEmployees.size(); i++) {
                shifts.add(Shift.builder()
                        .shiftStartTime(morningStart)
                        .shiftEndTime(morningEnd)
                        .employee(availableEmployees.get(i))
                        .build());
            }

            // Remove assigned employees
            availableEmployees = availableEmployees.subList(
                    Math.min(morningEmployeeCount, availableEmployees.size()),
                    availableEmployees.size());
            Collections.shuffle(availableEmployees, random);

            // Afternoon shift - assign 30-50% of remaining available employees
            int afternoonEmployeeCount = Math.max(1, availableEmployees.size() * (30 + random.nextInt(21)) / 100);
            for (int i = 0; i < afternoonEmployeeCount && i < availableEmployees.size(); i++) {
                shifts.add(Shift.builder()
                        .shiftStartTime(afternoonStart)
                        .shiftEndTime(afternoonEnd)
                        .employee(availableEmployees.get(i))
                        .build());
            }

            // Evening shift (only on weekends or 30% chance on weekdays)
            boolean isWeekend = currentDay.getDayOfWeek().getValue() >= 6; // Saturday or Sunday
            if (isWeekend || random.nextDouble() < 0.3) {
                // Remove assigned employees
                availableEmployees = availableEmployees.subList(
                        Math.min(afternoonEmployeeCount, availableEmployees.size()),
                        availableEmployees.size());
                Collections.shuffle(availableEmployees, random);

                // Evening shift - assign 20-30% of remaining available employees
                int eveningEmployeeCount = Math.max(1, availableEmployees.size() * (20 + random.nextInt(11)) / 100);
                for (int i = 0; i < eveningEmployeeCount && i < availableEmployees.size(); i++) {
                    shifts.add(Shift.builder()
                            .shiftStartTime(eveningStart)
                            .shiftEndTime(eveningEnd)
                            .employee(availableEmployees.get(i))
                            .build());
                }
            }
        }

        shiftRepository.saveAll(shifts);
    }

    private void createShippingAddresses() {
        // Get users for association
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            log.error("Cannot create shipping addresses: No users found");
            return;
        }

        List<ShippingAddresses> addresses = new ArrayList<>();
        Random random = new Random();

        // List of common Vietnam cities
        String[] cities = {"Ho Chi Minh City", "Hanoi", "Da Nang", "Nha Trang", "Can Tho", "Hue", "Hai Phong"};

        // List of districts by city
        String[][] districts = {
                // HCMC
                {"District 1", "District 2", "District 3", "District 4", "District 5", "Binh Thanh", "Thu Duc"},
                // Hanoi
                {"Ba Dinh", "Hoan Kiem", "Hai Ba Trung", "Dong Da", "Tay Ho", "Cau Giay"},
                // Da Nang
                {"Hai Chau", "Thanh Khe", "Lien Chieu", "Ngu Hanh Son", "Son Tra"},
                // Nha Trang
                {"Vinh Nguyen", "Van Thanh", "Vinh Hai", "Phuoc Hai"},
                // Can Tho
                {"Ninh Kieu", "Binh Thuy", "Cai Rang", "O Mon"},
                // Hue
                {"Hue City", "Huong Thuy", "Huong Tra", "Phu Vang"},
                // Hai Phong
                {"Hong Bang", "Ngo Quyen", "Le Chan", "Hai An", "Duong Kinh"}
        };

        // Create 1-3 addresses for each user
        for (User user : users) {
            int addressCount = 1 + random.nextInt(3); // 1 to 3 addresses per user
            boolean defaultSet = false;

            for (int i = 0; i < addressCount; i++) {
                int cityIndex = random.nextInt(cities.length);
                String city = cities[cityIndex];
                String district = districts[cityIndex][random.nextInt(districts[cityIndex].length)];

                // Set the first address as default or randomly for subsequent addresses
                boolean isDefault = !defaultSet || random.nextDouble() < 0.2;
                if (isDefault) {
                    defaultSet = true;
                }

                addresses.add(ShippingAddresses.builder()
                        .addressLine(generateRandomAddress())
                        .addressCity(city)
                        .addressDistrict(district)
                        .addressIsDefault(isDefault)
                        .user(user)
                        .build());
            }
        }

        shippingAddressesRepository.saveAll(addresses);
    }

    private String generateRandomAddress() {
        String[] streetNames = {"Nguyen Hue", "Le Loi", "Tran Hung Dao", "Nguyen Van Linh",
                "Vo Van Kiet", "Phan Xich Long", "Le Van Sy", "Truong Chinh",
                "Cach Mang Thang 8", "Ly Thai To", "Bach Dang", "Dien Bien Phu"};

        String[] streetTypes = {"Street", "Boulevard", "Avenue", "Road"};

        Random random = new Random();
        int houseNumber = 1 + random.nextInt(200);
        String streetName = streetNames[random.nextInt(streetNames.length)];
        String streetType = streetTypes[random.nextInt(streetTypes.length)];

        return houseNumber + " " + streetName + " " + streetType;
    }

    private void createStocks() {
        // Get warehouses, suppliers and ingredients for association
        List<Warehouse> warehouses = warehouseRepository.findAll();
        List<Supplier> suppliers = supplierRepository.findAll();
        List<Ingredient> ingredients = ingredientRepository.findAll();

        if (warehouses.isEmpty() || suppliers.isEmpty() || ingredients.isEmpty()) {
            log.error("Cannot create stocks: Missing required entities");
            return;
        }

        List<Stock> stocks = new ArrayList<>();
        Random random = new Random();

        // Units mapping by ingredient type
        int[] coffeeUnits = {500, 1000}; // grams
        int[] milkUnits = {1000, 2000}; // ml
        int[] syrupUnits = {500, 750}; // ml
        int[] toppingUnits = {500, 1000}; // grams
        int[] teaUnits = {250, 500}; // grams

        // For each warehouse, create stock entries for various ingredients
        for (Warehouse warehouse : warehouses) {
            // Each warehouse works with a subset of suppliers (1-3)
            List<Supplier> warehouseSuppliers = new ArrayList<>(suppliers);
            Collections.shuffle(warehouseSuppliers);
            int supplierCount = 1 + random.nextInt(Math.min(3, warehouseSuppliers.size()));
            warehouseSuppliers = warehouseSuppliers.subList(0, supplierCount);

            // Each warehouse stocks a subset of ingredients (60-90%)
            List<Ingredient> warehouseIngredients = new ArrayList<>(ingredients);
            Collections.shuffle(warehouseIngredients);
            int ingredientCount = (int) (warehouseIngredients.size() * (0.6 + random.nextDouble() * 0.3));
            warehouseIngredients = warehouseIngredients.subList(0, ingredientCount);

            // Create stocks for each ingredient with various quantities
            for (Ingredient ingredient : warehouseIngredients) {
                // Determine which supplier provides this ingredient
                Supplier supplier = warehouseSuppliers.get(random.nextInt(warehouseSuppliers.size()));

                // Determine appropriate unit based on ingredient type
                int unit;
                String type = ingredient.getIngredientType();

                switch (type) {
                    case "Coffee Beans":
                        unit = coffeeUnits[random.nextInt(coffeeUnits.length)];
                        break;
                    case "Milk":
                        unit = milkUnits[random.nextInt(milkUnits.length)];
                        break;
                    case "Syrup":
                        unit = syrupUnits[random.nextInt(syrupUnits.length)];
                        break;
                    case "Topping":
                        unit = toppingUnits[random.nextInt(toppingUnits.length)];
                        break;
                    case "Tea":
                        unit = teaUnits[random.nextInt(teaUnits.length)];
                        break;
                    default:
                        unit = 1000;
                }

                // Determine quantity - more for common ingredients, less for specialty ones
                int baseQuantity;
                if (type.equals("Coffee Beans") || type.equals("Milk")) {
                    // High volume for common ingredients
                    baseQuantity = 10 + random.nextInt(21); // 10-30
                } else if (type.equals("Syrup") || type.equals("Tea")) {
                    // Medium volume for these
                    baseQuantity = 5 + random.nextInt(11); // 5-15
                } else {
                    // Lower volume for toppings and specialty ingredients
                    baseQuantity = 3 + random.nextInt(8); // 3-10
                }

                stocks.add(Stock.builder()
                        .stockQuantity(baseQuantity)
                        .stockUnit(unit)
                        .ingredient(ingredient)
                        .warehouse(warehouse)
                        .supplier(supplier)
                        .build());
            }
        }

        stockRepository.saveAll(stocks);
    }

    private void createSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        Random random = new Random();

        // Create Vietnamese coffee bean suppliers
        suppliers.add(Supplier.builder()
                .supplierName("Trung Nguyen Coffee")
                .supplierPhone("028" + (10000000 + random.nextInt(90000000)))
                .supplierEmail("contact@trungnguyen.com.vn")
                .supplierAddress("82-84 Bui Thi Xuan St., Ben Thanh Ward, District 1, Ho Chi Minh City")
                .build());

        suppliers.add(Supplier.builder()
                .supplierName("Highlands Coffee Supply")
                .supplierPhone("024" + (10000000 + random.nextInt(90000000)))
                .supplierEmail("supply@highlandscoffee.com.vn")
                .supplierAddress("340 Phan Van Tri, Ward 11, Binh Thanh District, Ho Chi Minh City")
                .build());

        // Create dairy suppliers
        suppliers.add(Supplier.builder()
                .supplierName("Vinamilk Corporation")
                .supplierPhone("028" + (10000000 + random.nextInt(90000000)))
                .supplierEmail("b2b@vinamilk.com.vn")
                .supplierAddress("10 Tan Trao St., Tan Phu Ward, District 7, Ho Chi Minh City")
                .build());

        suppliers.add(Supplier.builder()
                .supplierName("TH True Milk")
                .supplierPhone("024" + (10000000 + random.nextInt(90000000)))
                .supplierEmail("wholesale@thmilk.vn")
                .supplierAddress("No. 8, TNR Tower, Nguyen Chi Thanh St., Dong Da District, Hanoi")
                .build());

        // Create syrup and flavor suppliers
        suppliers.add(Supplier.builder()
                .supplierName("Phuc Long Ingredients")
                .supplierPhone("028" + (10000000 + random.nextInt(90000000)))
                .supplierEmail("supply@phuclong.com.vn")
                .supplierAddress("42/24 - 42/26 Dien Bien Phu St., Ward 1, District 3, Ho Chi Minh City")
                .build());

        suppliers.add(Supplier.builder()
                .supplierName("Monin Vietnam")
                .supplierPhone("028" + (10000000 + random.nextInt(90000000)))
                .supplierEmail("orders@moninvietnam.com")
                .supplierAddress("27 Truong Quyen St., Ward 6, District 3, Ho Chi Minh City")
                .build());

        // Create tea suppliers
        suppliers.add(Supplier.builder()
                .supplierName("Dai Gia Tea Co.")
                .supplierPhone("0243" + (1000000 + random.nextInt(9000000)))
                .supplierEmail("sales@daigiate.vn")
                .supplierAddress("45 Thai Ha St., Dong Da District, Hanoi")
                .build());

        // Create equipment suppliers
        suppliers.add(Supplier.builder()
                .supplierName("Coffee Tech Solutions")
                .supplierPhone("0236" + (1000000 + random.nextInt(9000000)))
                .supplierEmail("info@coffeetech.vn")
                .supplierAddress("12 Le Duan St., Hai Chau District, Da Nang")
                .build());

        // Create pastry suppliers
        suppliers.add(Supplier.builder()
                .supplierName("ABC Bakery Supply")
                .supplierPhone("028" + (10000000 + random.nextInt(90000000)))
                .supplierEmail("sales@abcbakerysupply.vn")
                .supplierAddress("57 Nguyen Du St., Ben Nghe Ward, District 1, Ho Chi Minh City")
                .build());

        // Create packaging suppliers
        suppliers.add(Supplier.builder()
                .supplierName("Eco-Pack Vietnam")
                .supplierPhone("0225" + (1000000 + random.nextInt(9000000)))
                .supplierEmail("contact@ecopackvn.com")
                .supplierAddress("Lot CN3, Dinh Vu Industrial Zone, Hai An District, Hai Phong")
                .build());

        supplierRepository.saveAll(suppliers);
    }

    private void createTransfers() {
        // Get necessary data for associations
        List<Branch> branches = branchRepository.findAll();
        List<Warehouse> warehouses = warehouseRepository.findAll();
        List<Ingredient> ingredients = ingredientRepository.findAll();

        if (branches.isEmpty() || warehouses.isEmpty() || ingredients.isEmpty()) {
            log.error("Cannot create transfers: Missing required entities");
            return;
        }

        List<Transfer> transfers = new ArrayList<>();
        Random random = new Random();

        // Create transfers for the past 30 days
        for (int i = 0; i < 30; i++) {
            // Not every day has transfers - 60% chance of having a transfer on any day
            if (random.nextDouble() > 0.6) {
                continue;
            }

            LocalDateTime transferDate = LocalDateTime.now().minusDays(i);

            // Each branch might receive 0-2 transfers per day
            for (Branch branch : branches) {
                int transfersForBranch = random.nextInt(3); // 0-2 transfers

                for (int j = 0; j < transfersForBranch; j++) {
                    // Pick a random warehouse as the source
                    Warehouse warehouse = warehouses.get(random.nextInt(warehouses.size()));

                    // Create a unique tracking number with format TRF-YYYYMMDD-XXXX
                    String trackingNumber = String.format("TRF-%d%02d%02d-%04d",
                            transferDate.getYear(),
                            transferDate.getMonthValue(),
                            transferDate.getDayOfMonth(),
                            1000 + random.nextInt(9000));

                    // Generate a description
                    String description = "Regular stock transfer to " + branch.getBranchName();

                    // Create the transfer - initially with zero cost, will update after adding details
                    Transfer transfer = Transfer.builder()
                            .transferDescription(description)
                            .transferTrackingNumber(trackingNumber)
                            .transferTotalCost(BigDecimal.ZERO)
                            .branch(branch)
                            .warehouse(warehouse)
                            .build();

                    transfers.add(transfer);
                }
            }
        }

        // Save transfers first to get IDs
        transferRepository.saveAll(transfers);

        // Now create transfer details for each transfer
        for (Transfer transfer : transfers) {
            createTransferDetails(transfer, ingredients);
        }

        // Update total costs based on transfer details
        for (Transfer transfer : transfers) {
            BigDecimal totalCost = BigDecimal.ZERO;

            for (TransferDetail detail : transfer.getTransferDetails()) {
                BigDecimal detailCost = detail.getIngredient().getIngredientPrice()
                        .multiply(BigDecimal.valueOf(detail.getTransferDetailQuantity()));
                totalCost = totalCost.add(detailCost);
            }

            transfer.setTransferTotalCost(totalCost);
        }

        // Save the transfers again with the updated total costs
        transferRepository.saveAll(transfers);
    }

    private void createTransferDetails(Transfer transfer, List<Ingredient> allIngredients) {
        List<TransferDetail> transferDetails = new ArrayList<>();
        Random random = new Random();

        // Each transfer includes 3-8 different ingredients
        int ingredientCount = 3 + random.nextInt(6);

        // Select a random subset of ingredients for this transfer
        List<Ingredient> transferIngredients = new ArrayList<>(allIngredients);
        Collections.shuffle(transferIngredients);
        transferIngredients = transferIngredients.subList(0, Math.min(ingredientCount, transferIngredients.size()));

        for (Ingredient ingredient : transferIngredients) {
            // Determine appropriate unit based on ingredient type
            String unit;
            int quantity;

            switch (ingredient.getIngredientType()) {
                case "Coffee Beans":
                    unit = "g";
                    quantity = 250 * (1 + random.nextInt(10)); // 250g - 2.5kg
                    break;
                case "Milk":
                    unit = "ml";
                    quantity = 500 * (1 + random.nextInt(8)); // 500ml - 4L
                    break;
                case "Syrup":
                    unit = "ml";
                    quantity = 100 * (1 + random.nextInt(6)); // 100ml - 600ml
                    break;
                case "Topping":
                    unit = "g";
                    quantity = 100 * (1 + random.nextInt(5)); // 100g - 500g
                    break;
                case "Tea":
                    unit = "g";
                    quantity = 50 * (1 + random.nextInt(8)); // 50g - 400g
                    break;
                default:
                    unit = "unit";
                    quantity = 1 + random.nextInt(10); // 1-10 units
                    break;
            }

            // Create the transfer detail
            TransferDetail transferDetail = TransferDetail.builder()
                    .transferDetailQuantity(quantity)
                    .transferDetailUnit(unit)
                    .ingredient(ingredient)
                    .transfer(transfer)
                    .build();

            transferDetails.add(transferDetail);
        }

        transferDetailRepository.saveAll(transferDetails);
    }

    private void createInvoiceDetails() {
        // Get necessary entities for association
        List<Invoice> invoices = invoiceRepository.findAll();
        List<Ingredient> ingredients = ingredientRepository.findAll();

        if (invoices.isEmpty() || ingredients.isEmpty()) {
            log.error("Cannot create invoice details: No invoices or ingredients found");
            return;
        }

        List<InvoiceDetail> invoiceDetails = new ArrayList<>();
        Random random = new Random();

        // For each invoice, create multiple invoice details
        for (Invoice invoice : invoices) {
            // Select a random subset of ingredients for this invoice (3-8 ingredients)
            int ingredientCount = 3 + random.nextInt(6);
            List<Ingredient> invoiceIngredients = new ArrayList<>(ingredients);
            Collections.shuffle(invoiceIngredients);
            invoiceIngredients = invoiceIngredients.subList(0, Math.min(ingredientCount, invoiceIngredients.size()));

            // For each selected ingredient, create an invoice detail
            for (Ingredient ingredient : invoiceIngredients) {
                // Determine appropriate quantities based on ingredient type
                int quantity;
                int unit;

                switch (ingredient.getIngredientType()) {
                    case "Coffee Beans":
                        unit = 1000; // 1kg
                        quantity = 1 + random.nextInt(10); // 1-10 kg
                        break;
                    case "Milk":
                        unit = 1000; // 1L
                        quantity = 2 + random.nextInt(20); // 2-21 L
                        break;
                    case "Syrup":
                        unit = 750; // 750ml bottle
                        quantity = 1 + random.nextInt(5); // 1-5 bottles
                        break;
                    case "Topping":
                        unit = 500; // 500g package
                        quantity = 1 + random.nextInt(8); // 1-8 packages
                        break;
                    case "Tea":
                        unit = 250; // 250g package
                        quantity = 1 + random.nextInt(6); // 1-6 packages
                        break;
                    default:
                        unit = 1;
                        quantity = 1 + random.nextInt(10);
                        break;
                }

                // Create the invoice detail
                invoiceDetails.add(InvoiceDetail.builder()
                        .invoiceDetailQuantity(quantity)
                        .invoiceDetailUnit(unit)
                        .ingredient(ingredient)
                        .invoice(invoice)
                        .build());
            }
        }

        invoiceDetailRepository.saveAll(invoiceDetails);

        // Update the total cost for each invoice based on its details
        for (Invoice invoice : invoices) {
            BigDecimal totalCost = BigDecimal.ZERO;

            for (InvoiceDetail detail : invoice.getInvoiceDetails()) {
                BigDecimal detailCost = detail.getIngredient().getIngredientPrice()
                        .multiply(BigDecimal.valueOf(detail.getInvoiceDetailQuantity()));
                totalCost = totalCost.add(detailCost);
            }

            invoice.setInvoiceTransferTotalCost(totalCost);
        }

        // Save the updated invoices
        invoiceRepository.saveAll(invoices);
    }
}
