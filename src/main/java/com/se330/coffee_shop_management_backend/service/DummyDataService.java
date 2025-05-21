package com.se330.coffee_shop_management_backend.service;

import com.se330.coffee_shop_management_backend.dto.request.user.CreateUserRequest;
import com.se330.coffee_shop_management_backend.entity.Role;
import com.se330.coffee_shop_management_backend.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DummyDataService implements CommandLineRunner {
    private final RoleService roleService;

    private final UserService userService;

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
    }

    /**
     * Create roles.
     */
    private void createRoles() {
        List<Role> roleList = new ArrayList<>();
        roleList.add(Role.builder().name(Constants.RoleEnum.ADMIN).build());
        roleList.add(Role.builder().name(Constants.RoleEnum.CUSTOMER).build());

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
}
