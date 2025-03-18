package com.se330.coffee_shop_management_backend.entity.product;

import com.se330.coffee_shop_management_backend.entity.AbstractBaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "product_variants")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "var_id"))
})
public class ProductVariant extends AbstractBaseEntity {
    @Column(name = "var_tier_idx", nullable = false)
    private String varTierIdx;

    @Column(name = "var_default", nullable = false)
    private Boolean varDefault;

    @Column(name = "var_slug", nullable = false)
    private String varSlug;

    @Column(name = "var_sort", nullable = false)
    private int varSort;

    @Column(name = "var_price", nullable = false)
    private Long varPrice;

    @Column(name = "var_stock", nullable = false)
    private int varStock;

    @Column(name = "var_is_published", nullable = false)
    private Boolean varIsPublished;

    @Column(name = "var_is_deleted", nullable = false)
    private Boolean varIsDeleted;

    @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product_ProductVariant> product_ProductVariants;
}
