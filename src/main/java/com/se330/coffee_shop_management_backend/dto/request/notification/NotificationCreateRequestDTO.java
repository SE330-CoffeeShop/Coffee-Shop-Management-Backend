package com.se330.coffee_shop_management_backend.dto.request.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationCreateRequestDTO {
    private String notificationType;
    private String notificationContent;
    private UUID senderId;
    private UUID receiverId;
    private boolean isRead;
}