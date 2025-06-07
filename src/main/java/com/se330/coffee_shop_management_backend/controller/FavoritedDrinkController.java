package com.se330.coffee_shop_management_backend.controller;

import com.se330.coffee_shop_management_backend.dto.response.ErrorResponse;
import com.se330.coffee_shop_management_backend.dto.response.PageResponse;
import com.se330.coffee_shop_management_backend.dto.response.SingleResponse;
import com.se330.coffee_shop_management_backend.service.favoritedrinkservices.IFavoriteDrinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

import static com.se330.coffee_shop_management_backend.util.Constants.SECURITY_SCHEME_NAME;
import static com.se330.coffee_shop_management_backend.util.CreatePageHelper.createPageable;

@RestController
@RequestMapping("/favorite-drinks")
public class FavoritedDrinkController {

    private final IFavoriteDrinkService favoriteDrinkService;

    public FavoritedDrinkController(
            IFavoriteDrinkService favoriteDrinkService) {
        this.favoriteDrinkService = favoriteDrinkService;
    }

    @PostMapping("/{userId}/{drinkId}")
    @Operation(
            summary = "Add a drink to user's favorites",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Successfully added to favorites",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid user ID or drink ID",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User or drink not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<Map<String, UUID>>> addFavoriteDrink(
            @PathVariable UUID userId,
            @PathVariable UUID drinkId) {

        UUID favoriteId = favoriteDrinkService.addFavoriteDrink(userId, drinkId);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new SingleResponse<>(
                        HttpStatus.CREATED.value(),
                        "Drink added to favorites successfully",
                        Map.of("favoriteId", favoriteId)
                )
        );
    }

    @DeleteMapping("/{userId}/{drinkId}")
    @Operation(
            summary = "Remove a drink from user's favorites",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Successfully removed from favorites"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Favorite drink not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<Void> removeFavoriteDrink(
            @PathVariable UUID userId,
            @PathVariable UUID drinkId) {

        favoriteDrinkService.removeFavoriteDrink(userId, drinkId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    @Operation(
            summary = "Get all favorite drinks for a user",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved favorite drinks",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<UUID>> getUserFavoriteDrinks(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy) {

        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);

        Page<UUID> productIds = favoriteDrinkService.findAllFavoriteDrinksByUserId(userId, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "User's favorite drinks retrieved successfully",
                        productIds.toList(),
                        new PageResponse.PagingResponse(
                                productIds.getNumber(),
                                productIds.getSize(),
                                productIds.getTotalElements(),
                                productIds.getTotalPages()
                        )
                )
        );
    }

    @GetMapping("/most-favorited")
    @Operation(
            summary = "Get the most favorited drinks",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved most favorited drinks",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<UUID>> getMostFavoritedDrinks(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "count") String sortBy) {

        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);

        Page<UUID> productIds = favoriteDrinkService.findTheMostFavoritedDrink(pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Most favorited drinks retrieved successfully",
                        productIds.toList(),
                        new PageResponse.PagingResponse(
                                productIds.getNumber(),
                                productIds.getSize(),
                                productIds.getTotalElements(),
                                productIds.getTotalPages()
                        )
                )
        );
    }
}