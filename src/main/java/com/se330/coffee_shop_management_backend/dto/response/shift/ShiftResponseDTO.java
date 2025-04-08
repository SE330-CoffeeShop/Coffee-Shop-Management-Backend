package com.se330.coffee_shop_management_backend.dto.response.shift;

import com.se330.coffee_shop_management_backend.dto.response.AbstractBaseResponse;
import com.se330.coffee_shop_management_backend.entity.Shift;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
public class ShiftResponseDTO extends AbstractBaseResponse {

    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;

    @Schema(
            name = "createdAt",
            description = "Date time field of shift creation",
            type = "LocalDateTime",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime createdAt;

    @Schema(
            name = "updatedAt",
            type = "LocalDateTime",
            description = "Date time field of shift update",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime updatedAt;

    private LocalDateTime shiftStartTime;
    private LocalDateTime shiftEndTime;

    // Related entities
    private String employeeId;

    public static ShiftResponseDTO convert(Shift shift) {
        return ShiftResponseDTO.builder()
                .id(shift.getId().toString())
                .createdAt(shift.getCreatedAt())
                .updatedAt(shift.getUpdatedAt())
                .shiftStartTime(shift.getShiftStartTime())
                .shiftEndTime(shift.getShiftEndTime())
                .employeeId(shift.getEmployee() != null ? shift.getEmployee().getId().toString() : null)
                .build();
    }

    public static List<ShiftResponseDTO> convert(List<Shift> shifts) {
        if (shifts == null || shifts.isEmpty()) {
            return Collections.emptyList();
        }

        return shifts.stream()
                .map(ShiftResponseDTO::convert)
                .collect(Collectors.toList());
    }
}