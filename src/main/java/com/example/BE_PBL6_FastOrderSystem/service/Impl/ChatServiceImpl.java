package com.example.BE_PBL6_FastOrderSystem.service.Impl;

import com.example.BE_PBL6_FastOrderSystem.entity.Chat;
import com.example.BE_PBL6_FastOrderSystem.entity.User;
import com.example.BE_PBL6_FastOrderSystem.repository.ChatRepository;
import com.example.BE_PBL6_FastOrderSystem.repository.UserRepository;
import com.example.BE_PBL6_FastOrderSystem.request.ChatRequest;
import com.example.BE_PBL6_FastOrderSystem.response.ChatResponse;
import com.example.BE_PBL6_FastOrderSystem.response.UserResponse;
import com.example.BE_PBL6_FastOrderSystem.security.user.FoodUserDetails;
import com.example.BE_PBL6_FastOrderSystem.service.IChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements IChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Chat> getAllChats() {
        return chatRepository.findAll();
    }

    @Override
    public List<Chat> getChatsBySender(Long senderId) {
        Optional<User> sender = userRepository.findById(senderId);
        return sender.map(chatRepository::findAllBySender).orElseGet(List::of);
    }

    @Override
    public List<Chat> getChatsByReceiver(Long receiverId) {
        Optional<User> receiver = userRepository.findById(receiverId);
        return receiver.map(chatRepository::findAllByReceiver).orElseGet(List::of);
    }
    private User getCurrentUserId() {
        Long userId = FoodUserDetails.getCurrentUserId();
        User user = userRepository.findById(userId).orElse(null);
        return user;
    }
    @Override
    // Phương thức để lấy tin nhắn giữa người gửi và người nhận
    public List<ChatResponse> getChatHistory(Long receiverId) {
        User sender = getCurrentUserId();
        User receiver = userRepository.findById(receiverId).orElse(null);

        if (sender != null && receiver != null) {
            List<Chat> chats =  chatRepository.findAllBySenderAndReceiverOrSenderAndReceiver(sender,receiver,receiver,sender);
            // Chuyển đổi danh sách Chat thành ChatResponse
            return chats.stream().map(chat -> {
                ChatResponse response = new ChatResponse();
                response.setMessage(chat.getMessage());
                response.setSender(chat.getSender().getId());
                response.setReceiver(chat.getReceiver().getId());
                response.setLocal_time(chat.getLocal_time());
                response.setImage(chat.getImage());
                return response;
            }).collect(Collectors.toList());
        }

        return List.of();
    }

    @Override
    public Chat saveChat(ChatRequest chatRequest) {
        // Tìm user gửi
        User sender = userRepository.findById(chatRequest.getSender())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        // Tìm user nhận
        User receiver = userRepository.findById(chatRequest.getReceiver())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        String base64Image = null;
        if (chatRequest.getImage() != null && !chatRequest.getImage().isEmpty()) {
            try {
                // Mã hóa ảnh dưới dạng chuỗi Base64
                base64Image = Base64.getEncoder().encodeToString(chatRequest.getImage().getBytes());
            } catch (IOException e) {
                // Xử lý lỗi khi chuyển đổi ảnh thành Base64
                throw new RuntimeException("Failed to encode image to Base64", e);
            }
        }
        // Tạo đối tượng Chat với ảnh nếu có
        Chat chat = Chat.builder()
                .sender(sender)
                .receiver(receiver)
                .local_time(LocalDateTime.now())
                .message(chatRequest.getMessage())
                .isRead(chatRequest.getIsRead())
                .image(base64Image) // Gán ảnh nếu có
                .build();

        // Lưu tin nhắn vào cơ sở dữ liệu
        return chatRepository.save(chat);
    }


    public void deleteChat(Long id) {
        chatRepository.deleteById(id);
    }

    @Override
    public Set<User> getUsersReceivedFrom(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return new HashSet<>();
        }
        User user = userOpt.get();
        List<Chat> chats = chatRepository.findAllByReceiver(user);
        return chats.stream()
                .map(Chat::getSender)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<User> getUsersSentTo(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return new HashSet<>();
        }
        User user = userOpt.get();
        List<Chat> chats = chatRepository.findAllBySender(user);
        return chats.stream()
                .map(Chat::getReceiver)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<User> getUsersInteractedWith(Long userId) {
        Set<User> users = new HashSet<>();
        users.addAll(getUsersReceivedFrom(userId));
        users.addAll(getUsersSentTo(userId));
        return users;
    }
    @Override
    public List<User> getUserMessagesUnRead() {
        User user = getCurrentUserId();
        if (user == null) {
            return new ArrayList<>();
        }
        return chatRepository.findUsersWithUnreadMessages(user);
    }
    @Override
    public void markMessagesAsReadForUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return;
        }
        User user = userOpt.get();
        User currentUser = getCurrentUserId(); // Lấy người dùng hiện tại
        if (currentUser != null) {
            chatRepository.markMessagesAsRead(currentUser,user);  // Cập nhật tất cả tin nhắn là đã đọc
        }
    }
    @Override
    public List<UserResponse> getAllUsersByChatInteraction() {
        User currentUser = getCurrentUserId();
        List<User> users = chatRepository.findAllUsersByChatInteraction(currentUser.getId());
        System.out.println("chutfksjd: ");
        System.out.println(users.size());
        return users.stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }


}
