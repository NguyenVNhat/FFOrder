package com.example.BE_PBL6_FastOrderSystem.repository;

import com.example.BE_PBL6_FastOrderSystem.model.Chat;
import com.example.BE_PBL6_FastOrderSystem.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findAllBySender(User sender);
    List<Chat> findAllByReceiver(User receiver);
    List<Chat> findAllBySenderAndReceiverOrSenderAndReceiver(User sender1, User receiver1, User sender2, User receiver2);

    @Query("SELECT u FROM User u WHERE u.id IN (" +
            "SELECT DISTINCT c.receiver.id FROM Chat c WHERE c.sender.id = :userId " +
            "UNION " +
            "SELECT DISTINCT c.sender.id FROM Chat c WHERE c.receiver.id = :userId " +
            ") " +
            "ORDER BY (" +
            "SELECT MAX(c.local_time) FROM Chat c " +
            "WHERE (c.sender.id = u.id AND c.receiver.id = :userId) " +
            "   OR (c.sender.id = :userId AND c.receiver.id = u.id) " +
            ") DESC")
    List<User> findAllUsersByChatInteraction(@Param("userId") Long userId);

    @Query("SELECT DISTINCT m.sender FROM Chat m WHERE m.receiver = :receiver AND m.isRead = false")
    List<User> findUsersWithUnreadMessages(@Param("receiver") User receiver);

    @Modifying
    @Transactional
    @Query("UPDATE Chat m SET m.isRead = true WHERE m.receiver = :receiver AND m.sender = :sender AND m.isRead = false")
    void markMessagesAsRead(@Param("receiver") User receiver, @Param("sender") User sender);
}
