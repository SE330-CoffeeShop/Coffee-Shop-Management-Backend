package com.se330.coffee_shop_management_backend.controller;

import com.se330.coffee_shop_management_backend.dto.request.shift.ShiftCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.shift.ShiftUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.response.ErrorResponse;
import com.se330.coffee_shop_management_backend.dto.response.PageResponse;
import com.se330.coffee_shop_management_backend.dto.response.SingleResponse;
import com.se330.coffee_shop_management_backend.dto.response.shift.ShiftResponseDTO;
import com.se330.coffee_shop_management_backend.entity.Shift;
import com.se330.coffee_shop_management_backend.service.shiftservices.IShiftService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.se330.coffee_shop_management_backend.util.Constants.SECURITY_SCHEME_NAME;
import static com.se330.coffee_shop_management_backend.util.CreatePageHelper.createPageable;

@RestController
@RequestMapping("/shift")
public class ShiftController {

    private final IShiftService shiftService;

    public ShiftController(IShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'EMPLOYEE')")
    @Operation(
            summary = "Get shift detail",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved shift",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
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
                            responseCode = "404",
                            description = "Shift not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<ShiftResponseDTO>> findByIdShift(@PathVariable UUID id) {
        ShiftResponseDTO shift = ShiftResponseDTO.convert(shiftService.findByIdShift(id));
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "Shift retrieved successfully",
                        shift
                )
        );
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'EMPLOYEE')")
    @Operation(
            summary = "Get all shifts with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved shift list",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<ShiftResponseDTO>> findAllShifts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<Shift> shiftPage = shiftService.findAllShifts(pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Shifts retrieved successfully",
                        ShiftResponseDTO.convert(shiftPage.getContent()),
                        new PageResponse.PagingResponse(
                                shiftPage.getNumber(),
                                shiftPage.getSize(),
                                shiftPage.getTotalElements(),
                                shiftPage.getTotalPages()
                        )
                )
        );
    }

    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'EMPLOYEE')")
    @Operation(
            summary = "Create new shift",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Shift created successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<ShiftResponseDTO>> createShift(@RequestBody ShiftCreateRequestDTO shiftCreateRequestDTO) {
        ShiftResponseDTO shift = ShiftResponseDTO.convert(shiftService.createShift(shiftCreateRequestDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new SingleResponse<>(
                        HttpStatus.CREATED.value(),
                        "Shift created successfully",
                        shift
                )
        );
    }

    @PatchMapping("/")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'EMPLOYEE')")
    @Operation(
            summary = "Update shift",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Shift updated successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
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
                            responseCode = "404",
                            description = "Shift not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<ShiftResponseDTO>> updateShift(@RequestBody ShiftUpdateRequestDTO shiftUpdateRequestDTO) {
        ShiftResponseDTO shift = ShiftResponseDTO.convert(shiftService.updateShift(shiftUpdateRequestDTO));
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "Shift updated successfully",
                        shift
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'EMPLOYEE')")
    @Operation(
            summary = "Delete shift",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Shift deleted successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Shift not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<Void> deleteShift(@PathVariable UUID id) {
        shiftService.deleteShift(id);
        return ResponseEntity.noContent().build();
    }
}