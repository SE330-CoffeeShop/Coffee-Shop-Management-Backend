package com.se330.coffee_shop_management_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "notification_id"))
})
public class Notification extends AbstractBaseEntity {
    @Column(name = "notification_type", nullable = false)
    private int notificationType;

    @Column(name = "notification_date", nullable = false)
    private Date notificationDate;

    @Column(name = "notification_content", nullable = false)
    private int notificationContent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "user_sender_id",
            foreignKey = @ForeignKey(
                    name = "fk_notification_sender",
                    foreignKeyDefinition = "FOREIGN KEY (user_sender_id) REFERENCES users (id) ON DELETE CASCADE"
            )
    )
    private User sender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "user_receiver_id",
            foreignKey = @ForeignKey(
                    name = "fk_notification_receiver",
                    foreignKeyDefinition = "FOREIGN KEY (user_receiver_id) REFERENCES users (id) ON DELETE CASCADE"
            )
    )
    private User receiver;
}