package com.se330.coffee_shop_management_backend.service.notificationservices;

import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface INotificationService {
    Notification findByIdNotification(UUID id);
    Page<Notification> findAllNotifications(Pageable pageable);
    Page<Notification> findAllNotificationsByUserId(UUID userId, Pageable pageable);
    Page<Notification> findAllSentNotificationsByUserId(Pageable pageable, UUID userId);
    Page<Notification> findAllReceivedNotificationsByUserId(Pageable pageable, UUID userId);
    Notification createNotification(NotificationCreateRequestDTO notificationCreateRequestDTO);
    Notification updateNotification(NotificationUpdateRequestDTO notificationUpdateRequestDTO);
    void deleteNotification(UUID id);
}
