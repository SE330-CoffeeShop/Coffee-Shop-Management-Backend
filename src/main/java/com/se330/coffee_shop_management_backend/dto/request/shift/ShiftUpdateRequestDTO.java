package com.se330.coffee_shop_management_backend.dto.request.shift;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ShiftUpdateRequestDTO {
    private UUID shiftId;
    private LocalDateTime shiftStartTime;
    private LocalDateTime shiftEndTime;
    private UUID employeeId;
}
