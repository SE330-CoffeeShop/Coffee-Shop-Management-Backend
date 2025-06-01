package com.se330.coffee_shop_management_backend.dto.response.branch;

import com.se330.coffee_shop_management_backend.dto.response.AbstractBaseResponse;
import com.se330.coffee_shop_management_backend.entity.Branch;
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
public class BranchResponseDTO extends AbstractBaseResponse {

    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;

    @Schema(
            name = "createdAt",
            description = "Date time field of branch creation",
            type = "LocalDateTime",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime createdAt;

    @Schema(
            name = "updatedAt",
            type = "LocalDateTime",
            description = "Date time field of branch update",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime updatedAt;

    private String branchName;
    private String branchAddress;
    private String branchPhone;
    private String branchEmail;
    private String managerId;
    private List<String> employeeIds;
    private List<String> transferIds;
    private List<String> inventoryIds;
    private List<String> discountIds;

    public static BranchResponseDTO convert(Branch branch) {
        return BranchResponseDTO.builder()
                .id(branch.getId().toString())
                .createdAt(branch.getCreatedAt())
                .updatedAt(branch.getUpdatedAt())
                .branchName(branch.getBranchName())
                .branchAddress(branch.getBranchAddress())
                .branchPhone(branch.getBranchPhone())
                .branchEmail(branch.getBranchEmail())
                .managerId(branch.getManager() != null ? branch.getManager().getId().toString() : null)
                .employeeIds(branch.getEmployees() != null ? branch.getEmployees().stream()
                        .map(employee -> employee.getId().toString())
                        .collect(Collectors.toList()) : Collections.emptyList())
                .transferIds(branch.getTransfers() != null ? branch.getTransfers().stream()
                        .map(transfer -> transfer.getId().toString())
                        .collect(Collectors.toList()) : Collections.emptyList())
                .inventoryIds(branch.getInventories() != null ? branch.getInventories().stream()
                        .map(inventory -> inventory.getId().toString())
                        .collect(Collectors.toList()) : Collections.emptyList())
                .discountIds(branch.getDiscounts() != null ? branch.getDiscounts().stream()
                        .map(discount -> discount.getId().toString())
                        .collect(Collectors.toList()) : Collections.emptyList())
                .build();
    }

    public static List<BranchResponseDTO> convert(List<Branch> branches) {
        if (branches == null || branches.isEmpty()) {
            return Collections.emptyList();
        }

        return branches.stream()
                .map(BranchResponseDTO::convert)
                .collect(Collectors.toList());
    }
}