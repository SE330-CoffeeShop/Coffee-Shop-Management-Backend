package com.se330.coffee_shop_management_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stocks")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "stock_id"))
})
public class Stock extends AbstractBaseEntity {
    @Column(name = "stock_quantity", columnDefinition = "INTEGER(10)", nullable = false)
    private int stockQuantity;

    @Column(name = "stock_unit", columnDefinition = "INTEGER(10)", nullable = false)
    private int stockUnit;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "ingredient_id",
            foreignKey = @ForeignKey(
                    name = "fk_stock_ingredient",
                    foreignKeyDefinition = "FOREIGN KEY (ingredient_id) REFERENCES ingredients (ingredient_id) ON DELETE CASCADE"
            )
    )
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "warehouse_id",
            foreignKey = @ForeignKey(
                    name = "fk_stock_warehouse",
                    foreignKeyDefinition = "FOREIGN KEY (warehouse_id) REFERENCES warehouses (warehouse_id) ON DELETE CASCADE"
            )
    )
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "supplier_id",
            foreignKey = @ForeignKey(
                    name = "fk_stock_supplier",
                    foreignKeyDefinition = "FOREIGN KEY (supplier_id) REFERENCES suppliers (supplier_id) ON DELETE CASCADE"
            )
    )
    private Supplier supplier;


}