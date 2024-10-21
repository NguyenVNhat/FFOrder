package com.example.BE_PBL6_FastOrderSystem.service;

import com.example.BE_PBL6_FastOrderSystem.entity.Chat;
import com.example.BE_PBL6_FastOrderSystem.entity.User;
import com.example.BE_PBL6_FastOrderSystem.request.ChatRequest;
import com.example.BE_PBL6_FastOrderSystem.response.ChatResponse;
import com.example.BE_PBL6_FastOrderSystem.response.UserResponse;

import java.util.List;
import java.util.Set;

public interface IChatService {
    List<Chat> getAllChats();

    List<Chat> getChatsBySender(Long senderId);

    List<Chat> getChatsByReceiver(Long receiverId);

    // Phương thức để lấy tin nhắn giữa người gửi và người nhận
    List<ChatResponse> getChatHistory(Long receiverId);

    Chat saveChat(ChatRequest chatRequest);

    Set<User> getUsersReceivedFrom(Long userId);

    Set<User> getUsersSentTo(Long userId);

    Set<User> getUsersInteractedWith(Long userId);

    List<User> getUserMessagesUnRead();

    void markMessagesAsReadForUser(Long userId);

    List<UserResponse> getAllUsersByChatInteraction();
}
