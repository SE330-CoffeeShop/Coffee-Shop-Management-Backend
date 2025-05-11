package com.se330.coffee_shop_management_backend.service.notificationservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Notification;
import com.se330.coffee_shop_management_backend.entity.User;
import com.se330.coffee_shop_management_backend.repository.NotificationRepository;
import com.se330.coffee_shop_management_backend.repository.UserRepository;
import com.se330.coffee_shop_management_backend.service.notificationservices.INotificationService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ImpNotificationService implements INotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public ImpNotificationService(
            NotificationRepository notificationRepository,
            UserRepository userRepository
    ) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Notification findByIdNotification(UUID id) {
        return notificationRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Notification> findAllNotifications(Pageable pageable) {
        return notificationRepository.findAll(pageable);
    }

    @Override
    public Page<Notification> findAllNotificationsByUserId(UUID userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return notificationRepository.findAllByUser(user, pageable);
    }

    @Override
    public Page<Notification> findAllSentNotificationsByUserId(Pageable pageable, UUID senderId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        return notificationRepository.findAllBySender(sender, pageable);
    }

    @Override
    public Page<Notification> findAllReceivedNotificationsByUserId(Pageable pageable, UUID userId) {
        User recipient = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Recipient not found"));
        return notificationRepository.findAllByReceiver(recipient, pageable);
    }

    @Override
    public Notification createNotification(NotificationCreateRequestDTO notificationCreateRequestDTO) {
        User sender = userRepository.findById(UUID.fromString(notificationCreateRequestDTO.getSenderId()))
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(UUID.fromString(notificationCreateRequestDTO.getReceiverId()))
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        return notificationRepository.save(
                Notification.builder()
                        .notificationContent(notificationCreateRequestDTO.getNotificationContent())
                        .notificationType(notificationCreateRequestDTO.getNotificationType())
                        .receiver(receiver)
                        .sender(sender)
                        .build()
        );
    }

    @Transactional
    @Override
    public Notification updateNotification(NotificationUpdateRequestDTO notificationUpdateRequestDTO) {
        User sender = userRepository.findById(UUID.fromString(notificationUpdateRequestDTO.getSenderId()))
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(UUID.fromString(notificationUpdateRequestDTO.getReceiverId()))
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Notification existingNotification = notificationRepository.findById(notificationUpdateRequestDTO.getNotificationId())
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (existingNotification.getSender() != null) {
            existingNotification.getSender().getSentNotifications().remove(existingNotification);
            existingNotification.setSender(sender);
            sender.getSentNotifications().add(existingNotification);
        }

        if (existingNotification.getReceiver() != null) {
            existingNotification.getReceiver().getReceivedNotifications().remove(existingNotification);
            existingNotification.setReceiver(receiver);
            receiver.getReceivedNotifications().add(existingNotification);
        }

        existingNotification.setNotificationContent(notificationUpdateRequestDTO.getNotificationContent());
        existingNotification.setNotificationType(notificationUpdateRequestDTO.getNotificationType());

        return notificationRepository.save(existingNotification);
    }

    @Transactional
    @Override
    public void deleteNotification(UUID id) {
        Notification existingNotification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (existingNotification.getSender() != null) {
            existingNotification.getSender().getSentNotifications().remove(existingNotification);
            existingNotification.setSender(null);
        }

        if (existingNotification.getReceiver() != null) {
            existingNotification.getReceiver().getReceivedNotifications().remove(existingNotification);
            existingNotification.setReceiver(null);
        }

        notificationRepository.deleteById(id);
    }
}
