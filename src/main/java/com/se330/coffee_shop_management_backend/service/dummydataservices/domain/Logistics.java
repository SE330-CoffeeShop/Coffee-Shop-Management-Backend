package com.se330.coffee_shop_management_backend.service.dummydataservices.domain;

import com.se330.coffee_shop_management_backend.entity.*;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import com.se330.coffee_shop_management_backend.repository.*;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductVariantRepository;
import com.se330.coffee_shop_management_backend.util.Constants;
import com.se330.coffee_shop_management_backend.util.CreateTrackingNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class Logistics {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final ProductVariantRepository productVariantRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final InvoiceRepository invoiceRepository;
    private final ShippingAddressesRepository shippingAddressesRepository;
    private final PaymentMethodsRepository paymentMethodsRepository;
    private final TransferDetailRepository transferDetailRepository;
    private final TransferRepository transferRepository;
    private final WarehouseRepository warehouseRepository;
    private final IngredientRepository ingredientRepository;
    private final SupplierRepository supplierRepository;
    private final FavoriteDrinkRepository favoriteDrinkRepository;
    private final BranchRepository branchRepository;

    @Transactional
    public void create() {
        createOrders();
        createTransfer();
        createInvoice();
        createFavoriteDrink();
    }

    private void createOrders() {
        log.info("Creating orders...");

        // Get all necessary data
        List<User> customers = userRepository.findAllByRoleName(Constants.RoleEnum.CUSTOMER);
        List<Employee> employees = employeeRepository.findAll();
        List<ProductVariant> productVariants = productVariantRepository.findAll();
        List<ShippingAddresses> shippingAddresses = shippingAddressesRepository.findAll();
        PaymentMethods cashPaymentMethod = paymentMethodsRepository.findByMethodType("Tiền mặt");
        List<PaymentMethods> userPaymentMethods = paymentMethodsRepository.findAllByUserIsNotNull();

        if (customers.isEmpty() || employees.isEmpty() || productVariants.isEmpty()) {
            log.error("Cannot create orders: Missing required data");
            return;
        }

        Random random = new Random();
        List<Order> orders = new ArrayList<>();
        int orderCount = 10000;

        // Create orders
        for (int i = 0; i < orderCount; i++) {
            User customer = customers.get(random.nextInt(customers.size()));
            Employee employee = employees.get(random.nextInt(employees.size()));

            // Select payment method
            PaymentMethods paymentMethod;
            boolean isOnline = random.nextBoolean();

            if (isOnline) {
                // Filter payment methods for this customer
                List<PaymentMethods> customerPaymentMethods = userPaymentMethods.stream()
                        .filter(pm -> pm.getUser() != null && pm.getUser().getId().equals(customer.getId()))
                        .toList();

                if (!customerPaymentMethods.isEmpty()) {
                    paymentMethod = customerPaymentMethods.get(random.nextInt(customerPaymentMethods.size()));
                } else {
                    paymentMethod = cashPaymentMethod;
                    isOnline = false;
                }
            } else {
                paymentMethod = cashPaymentMethod;
            }

            // Select shipping address for online orders
            ShippingAddresses shippingAddress = null;
            if (isOnline) {
                List<ShippingAddresses> customerAddresses = shippingAddresses.stream()
                        .filter(addr -> addr.getUser() != null && addr.getUser().getId().equals(customer.getId()))
                        .toList();

                if (!customerAddresses.isEmpty()) {
                    shippingAddress = customerAddresses.get(random.nextInt(customerAddresses.size()));
                }
            }

            // Generate order status with realistic distribution
            Constants.OrderStatusEnum status;
            int statusRandom = random.nextInt(100);
            if (statusRandom < 20) {
                status = Constants.OrderStatusEnum.PENDING;
            } else if (statusRandom < 40) {
                status = Constants.OrderStatusEnum.PROCESSING;
            } else if (statusRandom < 90) {
                status = Constants.OrderStatusEnum.COMPLETED;
            } else {
                status = Constants.OrderStatusEnum.CANCELLED;
            }

            // Create order
            Order order = Order.builder()
                    .orderStatus(status)
                    .orderTrackingNumber(CreateTrackingNumber.createTrackingNumber("ORD"))
                    .orderTotalCost(BigDecimal.ZERO) // Will be updated after adding items
                    .orderDiscountCost(BigDecimal.ZERO)
                    .orderTotalCostAfterDiscount(BigDecimal.ZERO)
                    .user(customer)
                    .employee(employee)
                    .paymentMethod(paymentMethod)
                    .shippingAddress(shippingAddress)
                    .build();

            orders.add(order);
        }

        orderRepository.saveAll(orders);
        log.info("Created {} orders", orders.size());

        createOrderDetails(orders, productVariants);
    }

    private void createOrderDetails(List<Order> orders, List<ProductVariant> productVariants) {
        log.info("Creating order details...");

        Random random = new Random();
        List<OrderDetail> allOrderDetails = new ArrayList<>();

        for (Order order : orders) {
            int itemCount = random.nextInt(10) + 1;
            List<OrderDetail> orderDetails = new ArrayList<>();
            BigDecimal orderTotal = BigDecimal.ZERO;
            BigDecimal orderDiscount = BigDecimal.ZERO;

            // Create random unique items for this order
            List<ProductVariant> availableVariants = new ArrayList<>(productVariants);
            for (int i = 0; i < itemCount && !availableVariants.isEmpty(); i++) {
                int variantIndex = random.nextInt(availableVariants.size());
                ProductVariant variant = availableVariants.get(variantIndex);
                availableVariants.remove(variantIndex); // Ensure no duplicates

                int quantity = random.nextInt(3) + 1;
                BigDecimal unitPrice = variant.getVariantPrice();

                // Apply random discount (0-15%)
                int discountPercent = random.nextInt(16);
                BigDecimal discountAmount = unitPrice.multiply(BigDecimal.valueOf(discountPercent))
                        .divide(BigDecimal.valueOf(100));
                BigDecimal unitPriceAfterDiscount = unitPrice.subtract(discountAmount);

                OrderDetail detail = OrderDetail.builder()
                        .order(order)
                        .productVariant(variant)
                        .orderDetailQuantity(quantity)
                        .orderDetailUnitPrice(unitPrice)
                        .orderDetailDiscountCost(discountAmount.multiply(BigDecimal.valueOf(quantity)))
                        .orderDetailUnitPriceAfterDiscount(unitPriceAfterDiscount)
                        .build();

                orderDetails.add(detail);

                // Update order totals
                BigDecimal itemTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
                BigDecimal itemDiscount = discountAmount.multiply(BigDecimal.valueOf(quantity));

                orderTotal = orderTotal.add(itemTotal);
                orderDiscount = orderDiscount.add(itemDiscount);
            }

            // Update order with calculated totals
            order.setOrderTotalCost(orderTotal);
            order.setOrderDiscountCost(orderDiscount);
            order.setOrderTotalCostAfterDiscount(orderTotal.subtract(orderDiscount));
            order.setOrderDetails(orderDetails);

            allOrderDetails.addAll(orderDetails);
        }

        orderDetailRepository.saveAll(allOrderDetails);
        orderRepository.saveAll(orders);

        log.info("Created {} order details across {} orders", allOrderDetails.size(), orders.size());
    }

    private void createTransfer() {
        log.info("Creating transfers...");

        List<Warehouse> warehouses = warehouseRepository.findAll();
        List<Ingredient> ingredients = ingredientRepository.findAll();
        List<Branch> branches = branchRepository.findAll();

        if (branches.isEmpty() || warehouses.isEmpty() || ingredients.isEmpty()) {
            log.error("Cannot create transfers: Missing required data");
            return;
        }

        Random random = new Random();
        List<Transfer> transfers = new ArrayList<>();
        int transferCount = 100;

        // Create transfers
        for (int i = 0; i < transferCount; i++) {
            Branch branch = branches.get(random.nextInt(branches.size()));
            Warehouse warehouse = warehouses.get(random.nextInt(warehouses.size()));

            Transfer transfer = Transfer.builder()
                    .transferDescription("Transfer from warehouse to branch " + (i + 1))
                    .transferTrackingNumber(CreateTrackingNumber.createTrackingNumber("TRF"))
                    .transferTotalCost(BigDecimal.ZERO) // Will be updated after adding items
                    .branch(branch)
                    .warehouse(warehouse)
                    .transferDetails(new ArrayList<>())
                    .build();

            transfers.add(transfer);
        }

        transferRepository.saveAll(transfers);
        log.info("Created {} transfers", transfers.size());

        createTransferDetail(transfers, ingredients);
    }

    private void createTransferDetail(List<Transfer> transfers, List<Ingredient> ingredients) {
        log.info("Creating transfer details...");

        Random random = new Random();
        List<TransferDetail> allTransferDetails = new ArrayList<>();
        String[] units = {"kg", "g", "l", "ml", "piece", "box", "package"};

        for (Transfer transfer : transfers) {
            int detailCount = random.nextInt(10) + 1; // 1-10 details per transfer
            List<TransferDetail> transferDetails = new ArrayList<>();
            BigDecimal transferTotal = BigDecimal.ZERO;

            // Create random unique items for this transfer
            List<Ingredient> availableIngredients = new ArrayList<>(ingredients);
            for (int i = 0; i < detailCount && !availableIngredients.isEmpty(); i++) {
                int ingredientIndex = random.nextInt(availableIngredients.size());
                Ingredient ingredient = availableIngredients.get(ingredientIndex);
                availableIngredients.remove(ingredientIndex); // Ensure no duplicates

                int quantity = random.nextInt(50) + 1; // 1-50 units
                String unit = units[random.nextInt(units.length)];

                TransferDetail detail = TransferDetail.builder()
                        .transferDetailQuantity(quantity)
                        .transferDetailUnit(unit)
                        .ingredient(ingredient)
                        .transfer(transfer)
                        .build();

                transferDetails.add(detail);

                // Update transfer total cost
                BigDecimal itemCost = ingredient.getIngredientPrice().multiply(BigDecimal.valueOf(quantity));
                transferTotal = transferTotal.add(itemCost);
            }

            // Update transfer with calculated total and details
            transfer.setTransferTotalCost(transferTotal);
            transfer.setTransferDetails(transferDetails);

            allTransferDetails.addAll(transferDetails);
        }

        transferDetailRepository.saveAll(allTransferDetails);
        transferRepository.saveAll(transfers);

        log.info("Created {} transfer details across {} transfers", allTransferDetails.size(), transfers.size());
    }

    private void createInvoice() {
        log.info("Creating invoices...");

        // Get all necessary data
        List<Supplier> suppliers = supplierRepository.findAll();
        List<Warehouse> warehouses = warehouseRepository.findAll();
        List<Ingredient> ingredients = ingredientRepository.findAll();

        if (suppliers.isEmpty() || warehouses.isEmpty() || ingredients.isEmpty()) {
            log.error("Cannot create invoices: Missing required data");
            return;
        }

        Random random = new Random();
        List<Invoice> invoices = new ArrayList<>();
        int invoiceCount = 100;

        // Create invoices
        for (int i = 0; i < invoiceCount; i++) {
            Supplier supplier = suppliers.get(random.nextInt(suppliers.size()));
            Warehouse warehouse = warehouses.get(random.nextInt(warehouses.size()));

            Invoice invoice = Invoice.builder()
                    .invoiceDescription("Purchase from supplier " + (i + 1))
                    .invoiceTrackingNumber(CreateTrackingNumber.createTrackingNumber("INV"))
                    .invoiceTransferTotalCost(BigDecimal.ZERO) // Will be updated after adding items
                    .supplier(supplier)
                    .warehouse(warehouse)
                    .invoiceDetails(new ArrayList<>())
                    .build();

            invoices.add(invoice);
        }

        invoiceRepository.saveAll(invoices);
        log.info("Created {} invoices", invoices.size());

        createInvoiceDetail(invoices, ingredients);
    }

    private void createInvoiceDetail(List<Invoice> invoices, List<Ingredient> ingredients) {
        log.info("Creating invoice details...");

        Random random = new Random();
        List<InvoiceDetail> allInvoiceDetails = new ArrayList<>();
        String[] units = {"kg", "g", "l", "ml", "miếng", "hộp", "túi"};

        for (Invoice invoice : invoices) {
            int detailCount = random.nextInt(10) + 1; // 1-10 details per invoice
            List<InvoiceDetail> invoiceDetails = new ArrayList<>();
            BigDecimal invoiceTotal = BigDecimal.ZERO;

            // Create random items for this invoice (allowing duplicates)
            for (int i = 0; i < detailCount; i++) {
                // Select random ingredient (possible duplicates)
                int ingredientIndex = random.nextInt(ingredients.size());
                Ingredient ingredient = ingredients.get(ingredientIndex);

                int quantity = random.nextInt(100) + 10; // 10-109 units
                String unit = units[random.nextInt(units.length)];

                InvoiceDetail detail = InvoiceDetail.builder()
                        .invoiceDetailQuantity(quantity)
                        .invoiceDetailUnit(unit)
                        .ingredient(ingredient)
                        .invoice(invoice)
                        .build();

                invoiceDetails.add(detail);

                // Update invoice total cost
                BigDecimal itemCost = ingredient.getIngredientPrice().multiply(BigDecimal.valueOf(quantity));
                invoiceTotal = invoiceTotal.add(itemCost);
            }

            // Update invoice with calculated total and details
            invoice.setInvoiceTransferTotalCost(invoiceTotal);
            invoice.setInvoiceDetails(invoiceDetails);

            allInvoiceDetails.addAll(invoiceDetails);
        }

        invoiceDetailRepository.saveAll(allInvoiceDetails);
        invoiceRepository.saveAll(invoices);

        log.info("Created {} invoice details across {} invoices", allInvoiceDetails.size(), invoices.size());
    }

    private void createFavoriteDrink() {
        log.info("Creating favorite drinks...");

        // Get all necessary data
        List<User> customers = userRepository.findAllByRoleName(Constants.RoleEnum.CUSTOMER);
        List<ProductVariant> productVariants = productVariantRepository.findAll();

        if (customers.isEmpty() || productVariants.isEmpty()) {
            log.error("Cannot create favorite drinks: Missing required data");
            return;
        }

        Random random = new Random();
        List<FavoriteDrink> favoriteDrinks = new ArrayList<>();

        // For each user, create 1-10 favorite drinks
        for (User user : customers) {
            int favoriteCount = random.nextInt(10) + 1; // 1-10 favorite drinks

            // Create random unique favorites for this user
            List<ProductVariant> availableVariants = new ArrayList<>(productVariants);
            for (int i = 0; i < favoriteCount && !availableVariants.isEmpty(); i++) {
                int variantIndex = random.nextInt(availableVariants.size());
                ProductVariant variant = availableVariants.get(variantIndex);
                availableVariants.remove(variantIndex); // Ensure no duplicates

                // Get the parent product of the variant
                if (variant.getProduct() == null) {
                    continue;
                }

                FavoriteDrink favoriteDrink = FavoriteDrink.builder()
                        .user(user)
                        .product(variant.getProduct())
                        .build();

                favoriteDrinks.add(favoriteDrink);
            }
        }

        favoriteDrinkRepository.saveAll(favoriteDrinks);
        log.info("Created {} favorite drinks for {} users", favoriteDrinks.size(), customers.size());
    }
}