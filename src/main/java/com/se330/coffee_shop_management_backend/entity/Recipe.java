package com.se330.coffee_shop_management_backend.entity;

import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recipes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "recipe_id"))
})
public class Recipe extends AbstractBaseEntity {
    @Column(name = "recipe_quantity", columnDefinition = "INTEGER(10)", nullable = false)
    private int recipeQuantity;

    @Column(name = "recipe_unit", columnDefinition = "VARCHAR(255)", nullable = false)
    private String recipeUnit;

    @Column(name = "recipe_is_topping", columnDefinition = "BOOLEAN", nullable = false)
    private boolean recipeIsTopping;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "ingredient_id",
            foreignKey = @ForeignKey(
                    name = "fk_recipe_ingredient",
                    foreignKeyDefinition = "FOREIGN KEY (ingredient_id) REFERENCES ingredients (ingredient_id) ON DELETE CASCADE"
            )
    )
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "var_id",
            foreignKey = @ForeignKey(
                    name = "fk_recipe_product_variant",
                    foreignKeyDefinition = "FOREIGN KEY (var_id) REFERENCES product_variants (var_id) ON DELETE CASCADE"
            )
    )
    private ProductVariant productVariant;
}