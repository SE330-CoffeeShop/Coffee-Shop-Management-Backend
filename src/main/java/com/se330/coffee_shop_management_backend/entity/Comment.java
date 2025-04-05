package com.se330.coffee_shop_management_backend.entity;

import com.se330.coffee_shop_management_backend.entity.product.Product;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "comments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", updatable = false, nullable = false)
    private int commentId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "comment_content", nullable = false, columnDefinition = "VARCHAR(1000)")
    private String commentContent;

    @Column(name = "comment_left", nullable = false, columnDefinition = "INTEGER(10)")
    private int commentLeft;

    @Column(name = "comment_right", nullable = false, columnDefinition = "INTEGER(10)")
    private int commentRight;

    @Column(name = "comment_is_deleted", nullable = false)
    private boolean commentIsDeleted;

    @Column(name = "comment_date", nullable = false)
    private Date commentDate;

    @Column(name = "comment_rating", nullable = false, precision = 2, scale = 2)
    private BigDecimal commentRating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "product_id",
            foreignKey = @ForeignKey(
                    name = "fk_comment_product",
                    foreignKeyDefinition = "FOREIGN KEY (product_id) REFERENCES products (product_id) ON DELETE CASCADE"
            )
    )
    private Product product;
}