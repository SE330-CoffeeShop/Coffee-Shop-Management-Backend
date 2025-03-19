package com.se330.coffee_shop_management_backend.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.se330.coffee_shop_management_backend.entity.AbstractBaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "pro_id"))
})
public class Product extends AbstractBaseEntity {
    @Column(name = "pro_name", nullable = false)
    private String proName;

    @Column(name = "pro_thumb", nullable = false)
    private String proThumb;

    @Column(name = "pro_description", nullable = false, length = 1000)
    private String proDescription;

    @Column(name = "pro_price", nullable = false)
    private BigDecimal proPrice;

    @Column(name = "pro_slug", nullable = false)
    private String proSlug;

    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "10.0", inclusive = true)
    @Digits(integer = 2, fraction = 1)
    @Column(name = "pro_ratings_average", nullable = false, precision = 3, scale = 1)
    private String proRatingsAverage;

    @Column(name = "pro_is_published", nullable = false)
    private Boolean proIsPublished;

    @Column(name = "pro_is_deleted", nullable = false)
    private Boolean proIsDeleted;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ProductVariant> productVariants = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "fk_product_product-category")
    private ProductCategory productCategory;
}
