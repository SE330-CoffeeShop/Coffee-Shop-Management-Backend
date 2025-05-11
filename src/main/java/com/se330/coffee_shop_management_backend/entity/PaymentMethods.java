package com.se330.coffee_shop_management_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment_methods")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "payment_method_id"))
})
public class PaymentMethods extends AbstractBaseEntity {
    @Column(name = "method_type", nullable = false)
    private String methodType;

    @Column(name = "method_details", nullable = false)
    private String methodDetails;

    @Column(name = "method_is_default", nullable = false)
    private boolean methodIsDefault;

    @OneToOne(mappedBy = "paymentMethod", fetch = FetchType.EAGER)
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(
                    name = "fk_payment_method_user",
                    foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE"
            )
    )
    private User user;
}