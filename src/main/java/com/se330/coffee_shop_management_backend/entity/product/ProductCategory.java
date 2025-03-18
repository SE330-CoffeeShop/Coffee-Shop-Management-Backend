package com.se330.coffee_shop_management_backend.entity.product;

import com.se330.coffee_shop_management_backend.entity.AbstractBaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "product_categories")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "cat_id"))
})
public class ProductCategory extends AbstractBaseEntity {
    @Column(name = "cat_name", nullable = false)
    private String catName;

    @Column(name = "cat_description", nullable = false)
    private String catDescription;

    @OneToMany(mappedBy = "productCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;
}
