package com.se330.coffee_shop_management_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

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
}