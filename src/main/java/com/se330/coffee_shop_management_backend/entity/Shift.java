package com.se330.coffee_shop_management_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shifts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "shift_id"))
})
public class Shift extends AbstractBaseEntity {
    @Column(name = "shift_start_time", nullable = false)
    private LocalDateTime shiftStartTime;

    @Column(name = "shift_end_time", nullable = false)
    private LocalDateTime shiftEndTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "employee_id",
            foreignKey = @ForeignKey(
                    name = "fk_shift_employee",
                    foreignKeyDefinition = "FOREIGN KEY (employee_id) REFERENCES employees (employee_id) ON DELETE CASCADE"
            )
    )
    private Employee employee;

    @Column(name = "month", nullable = false)
    private int month;

    @Column(name = "year", nullable = false)
    private int year;

    @Column(name = "shift_salary", nullable = false)
    private BigDecimal shiftSalary;

    @OneToMany(mappedBy = "shift", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<Checkin> checkins = new ArrayList<>();
}