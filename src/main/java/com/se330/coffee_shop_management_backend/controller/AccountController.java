package com.se330.coffee_shop_management_backend.controller;

import com.se330.coffee_shop_management_backend.dto.request.user.UpdatePasswordRequest;
import com.se330.coffee_shop_management_backend.dto.response.DetailedErrorResponse;
import com.se330.coffee_shop_management_backend.dto.response.ErrorResponse;
import com.se330.coffee_shop_management_backend.dto.response.SuccessResponse;
import com.se330.coffee_shop_management_backend.dto.response.user.UserResponse;
import com.se330.coffee_shop_management_backend.entity.User;
import com.se330.coffee_shop_management_backend.service.MessageSourceService;
import com.se330.coffee_shop_management_backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import static com.se330.coffee_shop_management_backend.util.Constants.SECURITY_SCHEME_NAME;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
@Tag(name = "002. Account", description = "Account API")
@Slf4j
public class AccountController extends AbstractBaseController {
    private final UserService userService;

    private final MessageSourceService messageSourceService;

    @GetMapping("/me")
    @Operation(
        summary = "Me endpoint",
        security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successful operation",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Bad credentials",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
                )
            )
        }
    )
    public ResponseEntity<UserResponse> me() {
        return ResponseEntity.ok(UserResponse.convert(userService.getUser()));
    }

    @PostMapping("/password")
    @Operation(
        summary = "Password update endpoint",
        security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successful operation",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SuccessResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Bad credentials",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "422",
                description = "Validation failed",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = DetailedErrorResponse.class)
                )
            )
        }
    )
    public ResponseEntity<SuccessResponse> password(
        @Parameter(description = "Request body to update password", required = true)
        @RequestBody @Valid UpdatePasswordRequest request
    ) throws BindException {
        userService.updatePassword(request);

        return ResponseEntity.ok(SuccessResponse.builder()
            .message(messageSourceService.get("your_password_updated"))
            .build());
    }

    @GetMapping("/resend-email-verification")
    @Operation(
        summary = "Resend e-mail verification endpoint",
        security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successful operation",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SuccessResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad request",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Bad credentials",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
                )
            )
        }
    )
    public ResponseEntity<SuccessResponse> resendEmailVerificationMail() {
        userService.resendEmailVerificationMail();

        return ResponseEntity.ok(SuccessResponse.builder()
            .message(messageSourceService.get("verification_email_sent"))
            .build());
    }

    @PostMapping("/avatar")
    @Operation(
            summary = "Upload user avatar image",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Avatar uploaded successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Map.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request or empty file",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<?> uploadAvatar(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty or not provided");
        }

        try {
            User currentUser = userService.getUser();
            String imageUrl = userService.uploadUserAvatar(currentUser.getId(), file);
            return ResponseEntity.ok(Map.of(
                    "status", 200,
                    "message", "Avatar uploaded successfully",
                    "url", imageUrl
            ));
        } catch (Exception e) {
            log.error("Failed to upload avatar: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error uploading avatar: " + e.getMessage());
        }
    }

    @DeleteMapping("/avatar")
    @Operation(
            summary = "Delete user avatar image",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Avatar deleted successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Map.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<?> deleteAvatar() {
        try {
            User currentUser = userService.getUser();
            String defaultAvatarUrl = userService.deleteUserAvatar(currentUser.getId());
            return ResponseEntity.ok(Map.of(
                    "status", 200,
                    "message", "Avatar deleted successfully",
                    "url", defaultAvatarUrl
            ));
        } catch (Exception e) {
            log.error("Failed to delete avatar: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error deleting avatar: " + e.getMessage());
        }
    }
}
