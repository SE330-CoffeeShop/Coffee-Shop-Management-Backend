package com.se330.coffee_shop_management_backend.service.notificationservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationForManyCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Notification;
import com.se330.coffee_shop_management_backend.entity.User;
import com.se330.coffee_shop_management_backend.repository.NotificationRepository;
import com.se330.coffee_shop_management_backend.repository.UserRepository;
import com.se330.coffee_shop_management_backend.service.notificationservices.INotificationService;
import com.se330.coffee_shop_management_backend.util.Constants;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

        User sender = null;

        if (notificationCreateRequestDTO.getSenderId() != null) {
            sender = userRepository.findById(notificationCreateRequestDTO.getSenderId())
                    .orElseThrow(() -> new RuntimeException("Sender not found"));
        }

        User receiver = userRepository.findById(notificationCreateRequestDTO.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Notification newNoti = notificationRepository.save(
                Notification.builder()
                        .notificationContent(notificationCreateRequestDTO.getNotificationContent())
                        .notificationType(notificationCreateRequestDTO.getNotificationType())
                        .receiver(receiver)
                        .sender(sender)
                        .isRead(notificationCreateRequestDTO.isRead())
                        .build()
        );

        // TODO: Add push notification to receiver and sender

        return newNoti;
    }

    @Transactional
    @Override
    public Notification updateNotification(NotificationUpdateRequestDTO notificationUpdateRequestDTO) {

        User sender = null;

        if (notificationUpdateRequestDTO.getSenderId() != null) {
            sender = userRepository.findById(notificationUpdateRequestDTO.getSenderId())
                    .orElseThrow(() -> new RuntimeException("Sender not found"));
        }

        User receiver = userRepository.findById(notificationUpdateRequestDTO.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Notification existingNotification = notificationRepository.findById(notificationUpdateRequestDTO.getNotificationId())
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (existingNotification.getSender() != null) {
            existingNotification.getSender().getSentNotifications().remove(existingNotification);
            existingNotification.setSender(sender);
            if (sender != null)
                sender.getSentNotifications().add(existingNotification);
        }

        if (existingNotification.getReceiver() != null) {
            existingNotification.getReceiver().getReceivedNotifications().remove(existingNotification);
            existingNotification.setReceiver(receiver);
            receiver.getReceivedNotifications().add(existingNotification);
        }

        existingNotification.setNotificationContent(notificationUpdateRequestDTO.getNotificationContent());
        existingNotification.setNotificationType(notificationUpdateRequestDTO.getNotificationType());
        existingNotification.setRead(notificationUpdateRequestDTO.isRead());

        return notificationRepository.save(existingNotification);
    }

    @Override
    public Page<Notification> sendNotificationToMany(NotificationForManyCreateRequestDTO notificationForManyCreateRequestDTO) {
        List<Notification> returnedNotifications = new ArrayList<>();

        List<User> users = userRepository.findAllById(notificationForManyCreateRequestDTO.getReceiverId());
        User sender = null;
        if (notificationForManyCreateRequestDTO.getSenderId() != null) {
            sender = userRepository.findById(notificationForManyCreateRequestDTO.getSenderId())
                    .orElseThrow(() -> new RuntimeException("Sender not found"));
        }
        for (User user : users) {
            Notification notification = Notification.builder()
                    .notificationContent(notificationForManyCreateRequestDTO.getNotificationContent())
                    .notificationType(notificationForManyCreateRequestDTO.getNotificationType())
                    .receiver(user)
                    .sender(sender)
                    .build();
            returnedNotifications.add(notificationRepository.save(notification));

            // TODO: Add push notification to receivers
        }

        // TODO: Add push notification to sender if applicable
        return new PageImpl<>(returnedNotifications);
    }

    @Override
    public Page<Notification> sendNotificationToAllUsers(NotificationCreateRequestDTO notificationCreateRequestDTO) {
        List<Notification> returnedNotifications = new ArrayList<>();

        List<User> users = userRepository.findAll();
        User sender = null;
        if (notificationCreateRequestDTO.getSenderId() != null) {
            sender = userRepository.findById(notificationCreateRequestDTO.getSenderId())
                    .orElseThrow(() -> new RuntimeException("Sender not found"));
        }
        for (User user : users) {
            Notification notification = Notification.builder()
                    .notificationContent(notificationCreateRequestDTO.getNotificationContent())
                    .notificationType(notificationCreateRequestDTO.getNotificationType())
                    .receiver(user)
                    .sender(sender)
                    .build();
            returnedNotifications.add(notificationRepository.save(notification));
            // TODO: Add push notification to receivers
        }

        // TODO: Add push notification to sender if applicable

        return new PageImpl<>(returnedNotifications);
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
