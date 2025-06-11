package com.se330.coffee_shop_management_backend.service.dummydataservices.domain;

import com.se330.coffee_shop_management_backend.dto.request.user.CreateUserRequest;
import com.se330.coffee_shop_management_backend.entity.PaymentMethods;
import com.se330.coffee_shop_management_backend.entity.Role;
import com.se330.coffee_shop_management_backend.entity.ShippingAddresses;
import com.se330.coffee_shop_management_backend.entity.User;
import com.se330.coffee_shop_management_backend.repository.PaymentMethodsRepository;
import com.se330.coffee_shop_management_backend.repository.ShippingAddressesRepository;
import com.se330.coffee_shop_management_backend.repository.UserRepository;
import com.se330.coffee_shop_management_backend.service.RoleService;
import com.se330.coffee_shop_management_backend.service.UserService;
import com.se330.coffee_shop_management_backend.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserBasicInfo {

    private final RoleService roleService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ShippingAddressesRepository shippingAddressesRepository;
    private final PaymentMethodsRepository paymentMethodsRepository;

    @Transactional
    public void create() throws BindException {
        createRoles();
        createUsers();
        createShippingAddresses();
        createPaymentMethods();
    }

    private void createRoles() {
        List<Role> roleList = new ArrayList<>();
        roleList.add(Role.builder().name(Constants.RoleEnum.ADMIN).build());
        roleList.add(Role.builder().name(Constants.RoleEnum.CUSTOMER).build());
        roleList.add(Role.builder().name(Constants.RoleEnum.MANAGER).build());
        roleList.add(Role.builder().name(Constants.RoleEnum.EMPLOYEE).build());

        roleService.saveList(roleList);
    }

    private void createUsers() throws BindException {
        String defaultPassword = "P@sswd123.";
        String defaultAdminAvatarUrl = "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749142598/dummy_avatar_2_t1pd3u.jpg";
        String defaultCustomerAvatarUrl = "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749142597/dummy_avatar_4_wrzhjf.jpg";
        String defaultManagerAvatarUrl = "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749142596/dummy_avatar_1_no9xzs.jpg";
        String defaultEmployeeAvatarUrl = "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749142598/dummy_avatar_3_krtmjn.jpg";

        log.info("Tạo người dùng quản trị viên (1 ADMIN)...");
        User adminUser = userService.create(CreateUserRequest.builder()
                .email("admin@example.com")
                .password(defaultPassword)
                .name("Nguyễn Văn")
                .lastName("Admin")
                .role(Constants.RoleEnum.ADMIN)
                .isEmailVerified(true)
                .phoneNumber("098" + getRandomDigits())
                .gender("Nam")
                .birthDate(LocalDateTime.now().minusYears(30 + (int)(Math.random() * 10)))
                .isBlocked(false)
                .build());
        adminUser.setAvatar(defaultAdminAvatarUrl);
        userRepository.save(adminUser);

        log.info("Tạo người dùng khách hàng (10 CUSTOMER)...");
        String[] customerFirstNames = {"Trần Thị", "Lê Văn", "Phạm Minh", "Hoàng Thị", "Ngô Đức", "Vũ Thị", "Đặng Văn", "Bùi Thị", "Đỗ Minh", "Hồ Thị"};
        String[] customerLastNames = {"Hương", "Thành", "Linh", "Đạt", "Mai", "Tuấn", "Hà", "Dũng", "Thảo", "Nam"};

        for (int i = 0; i < 10; i++) {
            String gender = customerFirstNames[i].contains("Thị") ? "Female" : "Male";
            User customerUser = userService.create(CreateUserRequest.builder()
                    .email("customer" + (i + 1) + "@example.com")
                    .password(defaultPassword)
                    .name(customerFirstNames[i])
                    .lastName(customerLastNames[i])
                    .role(Constants.RoleEnum.CUSTOMER)
                    .isEmailVerified(true)
                    .phoneNumber("09" + (7 + i % 3) + getRandomDigits())
                            .gender(gender)
                            .birthDate(LocalDateTime.now().minusYears(18 + (int)(Math.random() * 42)))
                    .isBlocked(false)
                    .build());
            customerUser.setAvatar(defaultCustomerAvatarUrl);
            userRepository.save(customerUser);
        }

        log.info("Tạo người dùng quản lý (5 MANAGER)...");
        String[] managerFirstNames = {"Phan Văn", "Lý Thị", "Trương Minh", "Mai Thị", "Dương Văn"};
        String[] managerLastNames = {"Quang", "Hòa", "Tâm", "Phương", "Khoa"};

        for (int i = 0; i < 5; i++) {
            String gender = managerFirstNames[i].contains("Thị") ? "Nữ" : "Nam";
            User managerUser = userService.create(CreateUserRequest.builder()
                    .email("manager" + (i + 1) + "@example.com")
                    .password(defaultPassword)
                    .name(managerFirstNames[i])
                    .lastName(managerLastNames[i])
                    .role(Constants.RoleEnum.MANAGER)
                    .isEmailVerified(true)
                    .isBlocked(false)
                    .phoneNumber("09" + (1 + i % 3) + getRandomDigits())
                    .gender(gender)
                    .birthDate(LocalDateTime.now().minusYears(28 + (int)(Math.random() * 15)))
                    .build());
            managerUser.setAvatar(defaultManagerAvatarUrl);
            userRepository.save(managerUser);
        }

        log.info("Tạo người dùng nhân viên (30 EMPLOYEE)...");
        String[] employeeFirstNames = {
                "Nguyễn Thị", "Trần Văn", "Lê Thị", "Phạm Văn", "Hoàng Thị",
                "Huỳnh Minh", "Võ Thị", "Đặng Văn", "Bùi Minh", "Đỗ Thị",
                "Hồ Văn", "Ngô Thị", "Dương Văn", "Lý Thị", "Trương Văn",
                "Mai Thị", "Phan Văn", "Vũ Minh", "Tô Thị", "Đinh Văn",
                "Trinh Thị", "Chu Văn", "Lương Thị", "Chung Minh", "Thái Thị",
                "Diệp Văn", "Tạ Thị", "Hà Văn", "Thạch Thị", "Kim Văn"
        };

        String[] employeeLastNames = {
                "Anh", "Bình", "Cường", "Dung", "Em",
                "Giang", "Hải", "Khánh", "Lâm", "Minh",
                "Ngọc", "Oanh", "Phúc", "Quân", "Sơn",
                "Tuyết", "Uyên", "Vân", "Xuân", "Yến",
                "Thắng", "Hùng", "Nhung", "Trung", "Thúy",
                "Phong", "Hạnh", "Tiến", "Trang", "Long"
        };

        for (int i = 0; i < 30; i++) {
            String gender = employeeFirstNames[i].contains("Thị") ? "Nữ" : "Nam";
            User employeeUser = userService.create(CreateUserRequest.builder()
                    .email("employee" + (i + 1) + "@example.com")
                    .password(defaultPassword)
                    .name(employeeFirstNames[i])
                    .lastName(employeeLastNames[i])
                    .role(Constants.RoleEnum.EMPLOYEE)
                    .isEmailVerified(true)
                    .isBlocked(false)
                    .phoneNumber("09" + (3 + i % 6) + getRandomDigits())
                    .gender(gender)
                    .birthDate(LocalDateTime.now().minusYears(20 + (int)(Math.random() * 25)))
                    .build());
            employeeUser.setAvatar(defaultEmployeeAvatarUrl);
            userRepository.save(employeeUser);
        }
    }

    private void createShippingAddresses() {
        log.info("Creating shipping addresses...");

        // Get only CUSTOMER users
        List<User> customerUsers = userRepository.findAllByRoleName(Constants.RoleEnum.CUSTOMER);

        if (customerUsers.isEmpty()) {
            log.error("Cannot create shipping addresses: No CUSTOMER users found");
            return;
        }

        List<ShippingAddresses> addresses = new ArrayList<>();

        // Vietnamese cities and districts
        String[][] cityDistricts = {
                {"Hà Nội", "Hoàn Kiếm", "Ba Đình", "Đống Đa", "Cầu Giấy", "Hai Bà Trưng", "Hoàng Mai"},
                {"Hồ Chí Minh", "Quận 1", "Quận 3", "Quận 5", "Quận 7", "Phú Nhuận", "Bình Thạnh", "Thủ Đức"},
                {"Đà Nẵng", "Hải Châu", "Thanh Khê", "Sơn Trà", "Ngũ Hành Sơn", "Liên Chiểu"},
                {"Hải Phòng", "Hồng Bàng", "Ngô Quyền", "Lê Chân", "Kiến An", "Đồ Sơn"},
                {"Cần Thơ", "Ninh Kiều", "Bình Thủy", "Cái Răng", "Ô Môn", "Thốt Nốt"}
        };

        // Vietnamese street names
        String[] streetNames = {
                "Nguyễn Huệ", "Lê Lợi", "Trần Hưng Đạo", "Nguyễn Thị Minh Khai",
                "Võ Văn Kiệt", "Phan Xích Long", "Lý Thường Kiệt", "Điện Biên Phủ",
                "Nguyễn Đình Chiểu", "Hoàng Văn Thụ", "Nguyễn Công Trứ", "Lê Thánh Tôn",
                "Phạm Ngũ Lão", "Trần Phú", "Bùi Viện", "Hàm Nghi", "Tôn Thất Thiệp"
        };

        // Create exactly 10 shipping addresses (1 per customer, if we have 10 customers)
        int addressCount = Math.min(10, customerUsers.size());

        for (int i = 0; i < addressCount; i++) {
            User customer = customerUsers.get(i % customerUsers.size());

            // Select random city and district
            int cityIdx = i % cityDistricts.length;
            String city = cityDistricts[cityIdx][0];
            String district = cityDistricts[cityIdx][1 + (i % (cityDistricts[cityIdx].length - 1))];

            // Create a Vietnamese address
            int houseNumber = 10 + (i * 7) % 90;
            String street = streetNames[i % streetNames.length];
            String addressLine = "Số " + houseNumber + " " + street;

            ShippingAddresses address = ShippingAddresses.builder()
                    .addressLine(addressLine)
                    .addressCity(city)
                    .addressDistrict(district)
                    .addressIsDefault(true) // One address per customer, so make it default
                    .user(customer)
                    .build();

            addresses.add(address);
        }

        shippingAddressesRepository.saveAll(addresses);
        log.info("Created {} shipping addresses", addresses.size());
    }

    private void createPaymentMethods() {
        log.info("Creating payment methods...");

        // Get only CUSTOMER users
        List<User> customerUsers = userRepository.findAllByRoleName(Constants.RoleEnum.CUSTOMER);

        if (customerUsers.isEmpty()) {
            log.error("Cannot create payment methods: No CUSTOMER users found");
            return;
        }

        List<PaymentMethods> paymentMethods = new ArrayList<>();

        // Vietnamese banks
        String[] banks = {
                "Vietcombank", "BIDV", "VPBank", "Techcombank", "MB Bank",
                "ACB", "Sacombank", "VIB", "TPBank", "HDBank"
        };

        // Payment method types with Vietnamese names
        String[][] methodTypes = {
                {"Thẻ tín dụng", "VISA", "Mastercard", "JCB"},
                {"Thẻ ghi nợ", "NAPAS", "Visa Debit", "ATM"},
                {"Ví điện tử", "Momo", "ZaloPay", "VNPay", "ShopeePay"}
        };

        // Creating 1 cash method (default method)
        PaymentMethods cashMethod = PaymentMethods.builder()
                .methodType("Tiền mặt")
                .methodDetails("Thanh toán khi nhận hàng")
                .methodIsDefault(true)
                .user(null) // Cash is a global payment method, not tied to a user
                .build();

        paymentMethods.add(cashMethod);

        // Create 3 payment methods for each customer (30 total if we have 10 customers)
        for (User customer : customerUsers) {
            // For each customer, create one payment method of each type: credit card, debit card, e-wallet
            for (int typeIdx = 0; typeIdx < methodTypes.length; typeIdx++) {
                String methodType = methodTypes[typeIdx][0];
                String provider = methodTypes[typeIdx][1 + (customerUsers.indexOf(customer) % (methodTypes[typeIdx].length - 1))];
                String bank = banks[customerUsers.indexOf(customer) % banks.length];

                // Format details based on type
                String details;
                if (typeIdx == 0) { // Credit card
                    details = provider + " **** **** **** " + (1000 + (customerUsers.indexOf(customer) * 731) % 9000);
                } else if (typeIdx == 1) { // Debit card
                    details = bank + " - " + provider + " **** " + (1000 + (customerUsers.indexOf(customer) * 157) % 9000);
                } else { // E-wallet
                    details = provider + " - " + customer.getEmail();
                }

                PaymentMethods method = PaymentMethods.builder()
                        .methodType(methodType)
                        .methodDetails(details)
                        .methodIsDefault(typeIdx == 0) // Make credit card the default for each user
                        .user(customer)
                        .build();

                paymentMethods.add(method);
            }
        }

        paymentMethodsRepository.saveAll(paymentMethods);
        log.info("Created {} payment methods", paymentMethods.size());
    }

    private String getRandomDigits() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            sb.append((int)(Math.random() * 10));
        }
        return sb.toString();
    }
}
