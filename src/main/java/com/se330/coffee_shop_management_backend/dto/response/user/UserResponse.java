package com.se330.coffee_shop_management_backend.dto.response.user;

import com.se330.coffee_shop_management_backend.entity.User;
import com.se330.coffee_shop_management_backend.util.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
public class UserResponse {
    @Schema(
        name = "id",
        description = "UUID",
        type = "String",
        example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;

    @Schema(
        name = "email",
        description = "E-mail of the user",
        type = "String",
        example = "mail@example.com"
    )
    private String email;

    @Schema(
        name = "name",
        description = "Name of the user",
        type = "String",
        example = "John"
    )
    private String name;

    @Schema(
        name = "lastName",
        description = "Lastname of the user",
        type = "String",
        example = "DOE"
    )
    private String lastName;

    private String gender;
    private String phoneNumber;
    private LocalDateTime birthday;

    private String role;
    private String branchId;

    @Schema(
        name = "emailVerifiedAt",
        description = "Date time field of user e-mail verified",
        type = "LocalDateTime",
        example = "2022-09-29T22:37:31"
    )
    private LocalDateTime emailVerifiedAt;

    @Schema(
        name = "blockedAt",
        description = "Date time field of user blocked",
        type = "LocalDateTime",
        example = "2022-09-29T22:37:31"
    )
    private LocalDateTime blockedAt;

    @Schema(
        name = "createdAt",
        description = "Date time field of user creation",
        type = "LocalDateTime",
        example = "2022-09-29T22:37:31"
    )
    private LocalDateTime createdAt;

    private String avatar;

    @Schema(
        name = "updatedAt",
        type = "LocalDateTime",
        description = "Date time field of user update",
        example = "2022-09-29T22:37:31"
    )
    private LocalDateTime updatedAt;




    /**
     * Convert User to UserResponse
     * @param user User
     * @return UserResponse
     */
    public static UserResponse convert(User user) {

        String branchId = null;
        if (user.getEmployee() == null && user.getRole().getName() == Constants.RoleEnum.EMPLOYEE) {
            throw new IllegalArgumentException("How the fuck?");
        }
        else
            branchId = user.getEmployee().getBranch().getId().toString();

        return UserResponse.builder()
            .id(user.getId().toString())
            .email(user.getEmail())
            .name(user.getName())
            .lastName(user.getLastName())
            .role(String.valueOf(user.getRole().getName()))
            .branchId(branchId)
            .emailVerifiedAt(user.getEmailVerifiedAt())
            .gender(user.getGender())
            .phoneNumber(user.getPhoneNumber())
            .birthday(user.getBirthDate())
            .blockedAt(user.getBlockedAt())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .avatar(user.getAvatar())
            .build();
    }
}
