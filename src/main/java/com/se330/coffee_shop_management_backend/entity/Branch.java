package com.se330.coffee_shop_management_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "branches")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "branch_id"))
})
public class Branch extends AbstractBaseEntity {
    @Column(name = "branch_name", nullable = false, unique = true, columnDefinition = "VARCHAR(255)")
    private String branchName;

    @Column(name = "branch_address", nullable = false, columnDefinition = "VARCHAR(255)")
    private String branchAddress;

    @Column(name = "branch_phone", nullable = false, columnDefinition = "VARCHAR(255)")
    private String branchPhone;

    @Column(name = "branch_email", nullable = false, columnDefinition = "VARCHAR(255)")
    private String branchEmail;

    // One branch has many employees
    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<Employee> employees = new ArrayList<>();

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<Transfer> transfers = new ArrayList<>();

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<Inventory> inventories = new ArrayList<>();

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<Discount> discounts = new ArrayList<>();
}
