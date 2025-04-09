package com.se330.coffee_shop_management_backend.controller;

import com.se330.coffee_shop_management_backend.dto.request.ingredient.IngredientCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.ingredient.IngredientUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.response.ErrorResponse;
import com.se330.coffee_shop_management_backend.dto.response.PageResponse;
import com.se330.coffee_shop_management_backend.entity.Ingredient;
import com.se330.coffee_shop_management_backend.service.ingredientservices.IIngredientService;
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

import java.util.UUID;

import static com.se330.coffee_shop_management_backend.util.Constants.SECURITY_SCHEME_NAME;
import static com.se330.coffee_shop_management_backend.util.CreatePageHelper.createPageable;

@RestController
@RequestMapping("/ingredient")
public class IngredientController {

    private final IIngredientService ingredientService;

    public IngredientController(IIngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get ingredient detail",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved ingredient",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Ingredient.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid ID format",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Ingredient not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<Ingredient> findByIdIngredient(@PathVariable UUID id) {
        return ResponseEntity.ok(ingredientService.findByIdIngredient(id));
    }

    @GetMapping("/all")
    @Operation(
            summary = "Get all ingredients with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved ingredient list",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
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
    public ResponseEntity<PageResponse<Ingredient>> findAllIngredients(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "vi") String lan,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<Ingredient> ingredientPage = ingredientService.findAllIngredients(pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        ingredientPage.getContent(),
                        ingredientPage.getTotalElements(),
                        ingredientPage.getNumber(),
                        ingredientPage.getSize()
                )
        );
    }

    @PostMapping("/")
    @Operation(
            summary = "Create new ingredient",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Ingredient created successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Ingredient.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
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
    public ResponseEntity<Ingredient> createIngredient(@RequestBody IngredientCreateRequestDTO ingredientCreateRequestDTO) {
        Ingredient createdIngredient = ingredientService.createIngredient(ingredientCreateRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdIngredient);
    }

    @PatchMapping("/")
    @Operation(
            summary = "Update ingredient",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ingredient updated successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Ingredient.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Ingredient not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<Ingredient> updateIngredient(@RequestBody IngredientUpdateRequestDTO ingredientUpdateRequestDTO) {
        Ingredient updatedIngredient = ingredientService.updateIngredient(ingredientUpdateRequestDTO);
        return ResponseEntity.ok(updatedIngredient);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete ingredient",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Ingredient deleted successfully"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Ingredient not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<Void> deleteIngredient(@PathVariable UUID id) {
        ingredientService.deleteIngredient(id);
        return ResponseEntity.noContent().build();
    }
}