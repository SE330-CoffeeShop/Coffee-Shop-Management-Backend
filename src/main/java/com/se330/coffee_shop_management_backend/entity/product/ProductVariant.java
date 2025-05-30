package com.se330.coffee_shop_management_backend.entity.product;

import com.se330.coffee_shop_management_backend.entity.AbstractBaseEntity;
import com.se330.coffee_shop_management_backend.entity.Discount;
import com.se330.coffee_shop_management_backend.entity.OrderDetail;
import com.se330.coffee_shop_management_backend.entity.Recipe;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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

    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<Recipe> recipes = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "product_variant_discounts",
            joinColumns = @JoinColumn(name = "var_id"),
            inverseJoinColumns = @JoinColumn(name = "discount_id")
    )
    @Builder.Default
    private List<Discount> discounts = new ArrayList<>();
}
