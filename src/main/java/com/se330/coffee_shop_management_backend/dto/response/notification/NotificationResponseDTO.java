package com.se330.coffee_shop_management_backend.dto.response.notification;

import com.se330.coffee_shop_management_backend.dto.response.AbstractBaseResponse;
import com.se330.coffee_shop_management_backend.entity.Notification;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
public class NotificationResponseDTO extends AbstractBaseResponse {
    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;

    @Schema(
            name = "createdAt",
            description = "Date time field of employee creation",
            type = "LocalDateTime",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime createdAt;

    @Schema(
            name = "updatedAt",
            type = "LocalDateTime",
            description = "Date time field of employee update",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime updatedAt;

    private int notificationType;
    private String notificationContent;
    private String senderId;
    private String receiverId;

    public static NotificationResponseDTO convert(Notification notification) {
        return NotificationResponseDTO.builder()
                .id(notification.getId().toString())
                .createdAt(notification.getCreatedAt())
                .updatedAt(notification.getUpdatedAt())
                .notificationType(notification.getNotificationType())
                .notificationContent(notification.getNotificationContent())
                .senderId(notification.getSender() != null ? notification.getSender().getId().toString() : null)
                .receiverId(notification.getReceiver() != null ? notification.getReceiver().getId().toString() : null)
                .build();
    }

    public static List<NotificationResponseDTO> convert(List<Notification> notifications) {
        if (notifications == null || notifications.isEmpty()) {
            return Collections.emptyList();
        }

        return notifications.stream()
                .map(NotificationResponseDTO::convert)
                .collect(Collectors.toList());
    }
}
