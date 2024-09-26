package com.example.BE_PBL6_FastOrderSystem.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    private String message;
    private String image; // Để nhận hình ảnh từ React (nếu có)
    private Long sender;
    private Long receiver;
    private LocalDateTime local_time;
}
