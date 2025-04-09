package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.Notification;
import com.se330.coffee_shop_management_backend.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID>, JpaSpecificationExecutor<Notification> {

    @Query("SELECT n FROM Notification n WHERE n.sender = :user OR n.receiver = :user")
    Page<Notification> findAllByUser(@Param("user") User user, Pageable pageable);

    Page<Notification> findAllBySender(User sender, Pageable pageable);
    Page<Notification> findAllByReceiver(User receiver, Pageable pageable);
}
