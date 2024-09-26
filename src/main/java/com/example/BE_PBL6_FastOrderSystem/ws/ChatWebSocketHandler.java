package com.example.BE_PBL6_FastOrderSystem.ws;

import com.example.BE_PBL6_FastOrderSystem.request.ChatRequest;
import com.example.BE_PBL6_FastOrderSystem.service.IChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Map<Long, List<WebSocketSession>> userSessions = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private IChatService chatService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
//        System.out.println("New WebSocket connection established");
//        Long userId = getUserIdFromSession(session);
//        System.out.println("userId AFFTERR = " + userId);
//        if (userId != null) {
//            userSessions.computeIfAbsent(userId, k -> new ArrayList<>()).add(session);
////            sendOnlineUsersUpdate(session);
////            sendStatusAllUsers(userId, true);
//        }
    }


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        try {
            System.out.println("Received message: " + message.getPayload());
            Map<String, Object> payload = objectMapper.readValue(message.getPayload(), Map.class);
//            Long userId = Long.valueOf(payload.get("userId").toString());

            if ("identify".equals(payload.get("type"))) {
                Long userId = Long. valueOf(payload.get("userId").toString());
                session.getAttributes().put("userId", userId);
                userSessions.computeIfAbsent(userId, k -> new ArrayList<>()).add(session);
                sendOnlineUsersUpdate(session);
                sendStatusAllUsers(userId, true);
                return;
            }

            if ("loadImage".equals(payload.get("type"))) {
                Long receiverI = Long. valueOf(payload.get("receiver").toString());
                List<WebSocketSession> receiverSessions = userSessions.get(receiverI);
                if (receiverSessions != null) {
                    for (WebSocketSession receiverSession : receiverSessions) {
                        if (receiverSession.isOpen()) {
                            receiverSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(payload)));
                        }
                    }
                }

// Kiểm tra nếu người gửi và người nhận không phải là cùng một người
                if (!receiverI.equals(Long.valueOf(payload.get("sender").toString()))) {
                    List<WebSocketSession> listSessionsByUser = userSessions.get(Long.valueOf(payload.get("sender").toString()));
                    if (listSessionsByUser != null) {
                        for (WebSocketSession userSession : listSessionsByUser) {
                            if (userSession.isOpen()) {
                                userSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(payload)));
                            }
                        }
                    }
                }
                return;
            }

            Long receiverId = Long.valueOf(payload.get("receiver").toString());
            Long senderId = Long.valueOf(payload.get("sender").toString());
            String chatMessage = payload.containsKey("message") ? payload.get("message").toString() : null;

            chatService.saveChat(new ChatRequest(senderId, receiverId, chatMessage,null,false));
            List<WebSocketSession> receiverSessions = userSessions.get(receiverId);
            if (receiverSessions != null) {
                for (WebSocketSession receiverSession : receiverSessions) {
                    if (receiverSession.isOpen()) {
                        receiverSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(payload)));
                    }
                }
            }

// Kiểm tra nếu người gửi và người nhận không phải là cùng một người
            if (!receiverId.equals(Long.valueOf(payload.get("sender").toString()))) {
                List<WebSocketSession> listSessionsByUser = userSessions.get(Long.valueOf(payload.get("sender").toString()));
                if (listSessionsByUser != null) {
                    for (WebSocketSession userSession : listSessionsByUser) {
                        if (userSession.isOpen()) {
                            userSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(payload)));
                        }
                    }
                }
            }

            System.out.println("Message saved successfully");
        } catch (Exception e) {
            System.out.println("Error processing message: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws IOException {
        System.out.println("WebSocket connection closed: " + status);
        Long userId = getUserIdFromSession(session);
        System.out.println("userId AFFTERRClose = " + userId);

        if (userId != null) {
            List<WebSocketSession> sessions = userSessions.get(userId);
            if (sessions != null) {
                sessions.remove(session);
                if (sessions.isEmpty()) {
                    userSessions.remove(userId);
                    sendStatusAllUsers(userId, false);
                }
            }

        }
    }


    private void sendOnlineUsersUpdate(WebSocketSession session) throws IOException {
        List<Long> onlineUsers = new ArrayList<>(userSessions.keySet());

        // Tạo đối tượng message để gửi
        Map<String, Object> message = new HashMap<>();
        message.put("type", "onlineUsers");
        message.put("onlineUsers", onlineUsers);

        String jsonMessage = objectMapper.writeValueAsString(message);

        session.sendMessage(new TextMessage(jsonMessage));
    }

    private void sendStatusAllUsers(Long userId, boolean status) throws IOException {
        Map<String, Object> notification = new HashMap<>();
        notification.put("type", "online");
        notification.put("userId", userId);  // Người dùng cần cập nhật trạng thái
        notification.put("status", status);  // Trạng thái online/offline

        String jsonMessage = objectMapper.writeValueAsString(notification);

        // Gửi thông báo tới tất cả các phiên kết nối của **mọi** người dùng
        for (List<WebSocketSession> sessions : userSessions.values()) {
            for (WebSocketSession wsSession : sessions) {
                if (wsSession.isOpen()) {
                    wsSession.sendMessage(new TextMessage(jsonMessage));
                }
            }
        }
    }



    private Long getUserIdFromSession(WebSocketSession session) {
        // Giả sử bạn lưu userId trong session attributes khi người dùng kết nối
        return (Long) session.getAttributes().get("userId");
    }

    private List<WebSocketSession> getUserSessions(Long userId) {
        return userSessions.get(userId);
    }
}
