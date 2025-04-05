package com.se330.coffee_shop_management_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "suppliers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "supplier_id"))
})
public class Supplier extends AbstractBaseEntity {
    @Column(name = "supplier_name", columnDefinition = "VARCHAR(255)", nullable = false)
    private String supplierName;

    @Column(name = "supplier_phone", columnDefinition = "VARCHAR(255)", nullable = false)
    private String supplierPhone;

    @Column(name = "supplier_email", columnDefinition = "VARCHAR(255)", nullable = false)
    private String supplierEmail;

    @Column(name = "supplier_address", columnDefinition = "VARCHAR(255)", nullable = false)
    private String supplierAddress;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<Stock> stocks = new ArrayList<>();

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<Invoice> invoices = new ArrayList<>();
}