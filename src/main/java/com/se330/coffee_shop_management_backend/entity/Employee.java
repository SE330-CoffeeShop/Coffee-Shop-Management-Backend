package com.se330.coffee_shop_management_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "employees")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "employee_id"))
})
public class Employee extends AbstractBaseEntity {
    @Column(name = "employee_position", nullable = false)
    private String employeePosition = "";

    @Column(name = "employee_department", nullable = false)
    private String employeeDepartment = "";

    @Column(name = "employee_hire_date", nullable = false)
    private LocalDateTime employeeHireDate;

    // Many employees can belong to one branch
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "branch_id",
            foreignKey = @ForeignKey(
                    name = "fk_employee_branch",
                    foreignKeyDefinition = "FOREIGN KEY (branch_id) REFERENCES branches (branch_id) ON DELETE CASCADE"
            )
    )
    private Branch branch;

    // One employee is associated with exactly one user
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "user_id",
            unique = true,
            foreignKey = @ForeignKey(
                    name = "fk_employee_user",
                    foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE"
            )
    )
    private User user;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Order order;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<Shift> shifts = new ArrayList<>();
}
