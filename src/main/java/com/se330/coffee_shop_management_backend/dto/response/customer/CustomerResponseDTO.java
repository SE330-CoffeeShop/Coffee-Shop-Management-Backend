package com.se330.coffee_shop_management_backend.dto.response.customer;

import com.se330.coffee_shop_management_backend.entity.Order;
import com.se330.coffee_shop_management_backend.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@SuperBuilder
public class CustomerResponseDTO {
    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;

    @Schema(
            name = "email",
            description = "E-mail of the customer",
            type = "String",
            example = "customer@example.com"
    )
    private String email;

    @Schema(
            name = "name",
            description = "Name of the customer",
            type = "String",
            example = "John"
    )
    private String name;

    @Schema(
            name = "lastName",
            description = "Lastname of the customer",
            type = "String",
            example = "DOE"
    )
    private String lastName;
    private LocalDateTime lastBuyAt;

    /**
     * Convert Customer to CustomerResponseDTO
     * @param user User
     * @return CustomerResponseDTO sorted by the most recent order
     */
    public static CustomerResponseDTO convert(User user) {

        if (user.getOrders() == null || user.getOrders().isEmpty())
            return null;

        LocalDateTime lastBuyAt = user.getOrders().stream()
                .map(Order::getCreatedAt)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        return CustomerResponseDTO.builder()
                .id(user.getId().toString())
                .email(user.getEmail())
                .name(user.getName())
                .lastName(user.getLastName())
                .lastBuyAt(lastBuyAt)
                .build();
    }

    /**
     * Convert List of Customer to List of CustomerResponseDTO
     * @param users List of User
     * @return List of CustomerResponseDTO sorted by the most recent buy
     */
    public static List<CustomerResponseDTO> convert(List<User> users) {
        if (users == null || users.isEmpty()) {
            return List.of();
        }

        return users.stream()
                .map(CustomerResponseDTO::convert)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(CustomerResponseDTO::getLastBuyAt,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
    }
}
