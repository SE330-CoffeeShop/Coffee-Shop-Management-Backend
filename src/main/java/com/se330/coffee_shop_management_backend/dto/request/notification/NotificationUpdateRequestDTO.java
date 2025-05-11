package com.se330.coffee_shop_management_backend.dto.request.notification;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class NotificationUpdateRequestDTO {
    private UUID notificationId;
    private int notificationType;
    private String notificationContent;
    private String senderId;
    private String receiverId;
}
