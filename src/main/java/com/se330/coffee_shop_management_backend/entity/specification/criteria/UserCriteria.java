package com.se330.coffee_shop_management_backend.entity.specification.criteria;

import com.se330.coffee_shop_management_backend.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class UserCriteria {
    private List<Constants.RoleEnum> roles;

    private Boolean isAvatar;

    private LocalDateTime createdAtStart;

    private LocalDateTime createdAtEnd;

    private Boolean isEmailActivated;

    private Boolean isBlocked;

    private String q;
}
