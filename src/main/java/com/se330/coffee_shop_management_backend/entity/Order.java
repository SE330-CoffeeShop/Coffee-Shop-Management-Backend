package com.se330.coffee_shop_management_backend.entity;

import com.se330.coffee_shop_management_backend.util.Constants;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "order_id"))
})
public class Order extends AbstractBaseEntity {

    @Column(name = "order_total_cost_after_discount", nullable = false)
    private BigDecimal orderTotalCostAfterDiscount;

    @Column(name = "order_discount_cost", nullable = false)
    private BigDecimal orderDiscountCost;

    @Column(name = "order_total_cost", nullable = false)
    private BigDecimal orderTotalCost;

    @Column(name = "order_status", nullable = false)
    @Enumerated(EnumType.STRING)
    @NaturalId
    private Constants.OrderStatusEnum orderStatus;

    @Column(name = "order_tracking_number", nullable = false)
    private String orderTrackingNumber;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "employee_id",
            foreignKey = @ForeignKey(
                    name = "fk_order_employee",
                    foreignKeyDefinition = "FOREIGN KEY (employee_id) REFERENCES employees (employee_id) ON DELETE CASCADE"
            )
    )
    private Employee employee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "payment_method_id",
            foreignKey = @ForeignKey(
                    name = "fk_order_payment_method",
                    foreignKeyDefinition = "FOREIGN KEY (payment_method_id) REFERENCES payment_methods (payment_method_id) ON DELETE CASCADE"
            )
    )
    private PaymentMethods paymentMethod;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(
                    name = "fk_order_user",
                    foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE"
            )
    )
    private User user;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "shipping_address_id",
            foreignKey = @ForeignKey(
                    name = "fk_order_shipping_address",
                    foreignKeyDefinition = "FOREIGN KEY (shipping_address_id) REFERENCES shipping_addresses (shipping_address_id) ON DELETE CASCADE"
            )
    )
    private ShippingAddresses shippingAddress;
}