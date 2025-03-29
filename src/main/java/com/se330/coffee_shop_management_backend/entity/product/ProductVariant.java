package com.se330.coffee_shop_management_backend.entity.product;

import com.se330.coffee_shop_management_backend.entity.AbstractBaseEntity;
import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "variant_tier_idx", nullable = false)
    private String variantTierIdx = "";

    @Column(name = "variant_default", nullable = false)
    private Boolean variantDefault = false;

    @Column(name = "variant_slug", nullable = false)
    private String variantSlug = "";

    @Column(name = "variant_sort", nullable = false)
    private int variantSort = 0;

    @Column(name = "variant_price", nullable = false)
    private Long variantPrice = 0L;

    @Column(name = "variant_stock", nullable = false)
    private int variantStock = 0;

    @Column(name = "variant_is_published", nullable = false)
    private Boolean variantIsPublished = false;

    @Column(name = "variant_is_deleted", nullable = false)
    private Boolean variantIsDeleted = false;

    @ManyToOne
    @JoinColumn(name = "fk_product-variant_product")
    private Product product;
}
