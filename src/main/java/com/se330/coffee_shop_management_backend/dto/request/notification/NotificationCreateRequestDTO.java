package com.se330.coffee_shop_management_backend.dto.request.notification;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotificationCreateRequestDTO {
    private int notificationType;
    private String notificationContent;
    private String senderId;
    private String receiverId;
}
