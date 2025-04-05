package com.se330.coffee_shop_management_backend.dto.request.branch;

import com.se330.coffee_shop_management_backend.entity.AbstractBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class BranchUpdateRequestDTO extends AbstractBaseEntity {
    private String branchName;
    private String branchAddress;
    private String branchPhone;
    private String branchEmail;
}