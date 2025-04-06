package com.se330.coffee_shop_management_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "discounts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "discount_id"))
})
public class Discount extends AbstractBaseEntity {
    @Column(name = "discount_name", nullable = false)
    private String discountName;

    @Column(name = "discount_description", nullable = false)
    private String discountDescription;

    @Column(name = "discount_type", nullable = false)
    private String discountType;

    @Column(name = "discount_value", nullable = false)
    private BigDecimal discountValue;

    @Column(name = "discount_code", nullable = false)
    private String discountCode;

    @Column(name = "discount_start_date", nullable = false)
    private Date discountStartDate;

    @Column(name = "discount_end_date", nullable = false)
    private Date discountEndDate;

    @Column(name = "discount_max_users",nullable = false)
    private int discountMaxUsers;

    @Column(name = "discount_user_count", nullable = false)
    private int discountUserCount;

    @Column(name = "discount_max_per_user", nullable = false)
    private int discountMaxPerUser;

    @Column(name = "discount_min_order_value", nullable = false)
    private int discountMinOrderValue;

    @Column(name = "discount_is_active", nullable = false)
    private boolean discountIsActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "branch_id",
            foreignKey = @ForeignKey(
                    name = "fk_inventory_branch",
                    foreignKeyDefinition = "FOREIGN KEY (branch_id) REFERENCES branches (branch_id) ON DELETE CASCADE"
            )
    )
    private Branch branch;
}