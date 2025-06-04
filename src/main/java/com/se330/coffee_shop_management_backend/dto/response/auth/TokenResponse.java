package com.se330.coffee_shop_management_backend.dto.response.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class TokenResponse {
    @Schema(
        name = "token",
        description = "Token",
        type = "String",
        example = "eyJhbGciOiJIUzUxMiJ9..."
    )
    private String accessToken;

    @Schema(
        name = "refreshToken",
        description = "Refresh Token",
        type = "String",
        example = "eyJhbGciOiJIUzUxMiJ9..."
    )
    private String refreshToken;

    @Schema(
        name = "userId",
        description = "User ID",
        type = "String",
        example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String userId;

    @Schema(
        name = "roles",
        description = "Roles of the user",
        type = "String",
        example = "[\"USER\", \"ADMIN\"]")
    private List<String> roles;
}
