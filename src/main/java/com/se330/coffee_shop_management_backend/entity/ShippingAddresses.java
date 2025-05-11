package com.se330.coffee_shop_management_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shipping_addresses")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "shipping_address_id"))
})
public class ShippingAddresses extends AbstractBaseEntity {
    @Column(name = "address_line", nullable = false)
    private String addressLine;

    @Column(name = "address_city",  nullable = false)
    private String addressCity;

    @Column(name = "address_district", nullable = false)
    private String addressDistrict;

    @Column(name = "address_is_default", nullable = false)
    private boolean addressIsDefault;

    @OneToOne(mappedBy = "shippingAddress", fetch = FetchType.EAGER)
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(
                    name = "fk_shipping_address_user",
                    foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE"
            )
    )
    private User user;
}