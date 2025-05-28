package com.se330.coffee_shop_management_backend.dto.request.notification;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class NotificationUpdateRequestDTO {
    private UUID notificationId;
    private String notificationType;
    private String notificationContent;
    private UUID senderId;
    private UUID receiverId;
}
